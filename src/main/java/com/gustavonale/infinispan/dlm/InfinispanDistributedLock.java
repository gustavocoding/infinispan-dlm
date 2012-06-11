package com.gustavonale.infinispan.dlm;

import org.infinispan.AdvancedCache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.Merged;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.MergeEvent;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.jgroups.MergeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A DLM (Distributed Lock Manager) on top of Infinispan.
 * It provided coarse grained lock on a per JVM basis. Once acquired, the lock
 * is retained until the release method is called or the JVM crashes or exists,
 * which will cause it to be released
 */
@Listener(sync = false)
public final class InfinispanDistributedLock implements DistributedLock {

    Logger logger = LoggerFactory.getLogger(InfinispanDistributedLock.class);

    private volatile int viewSize = 0;

    /**
     * The replicated cache that holds the lock
     */
    private final AdvancedCache<Object, Object> cache;

    /**
     * The lock key
     */
    private final static String LOCK_KEY = "lock";


    /**
     * Constructor
     *
     * @param cache The cache that holds the lock owner
     */
    InfinispanDistributedLock(AdvancedCache<Object, Object> cache) {
        this.cache = cache;
        cache.getCacheManager().addListener(this);

    }

    public boolean isAcquired() {
        return getAddress().equals(cache.get(LOCK_KEY));
    }

    /**
     * Detect nodes that leave the cluster nodes and remove the lock accordingly
     *
     * @param viewChangedEvent The topology changes
     */
    @ViewChanged
    public void viewChanged(ViewChangedEvent viewChangedEvent) {
        viewSize = viewChangedEvent.getNewMembers().size();
        logger.info("Membership changed. New cluster size is " + viewSize);
        List<Address> leftMembers = minus(viewChangedEvent.getNewMembers(), viewChangedEvent.getOldMembers());
        for (Address member : leftMembers) {
            logger.info("Member {} left the cluster", member);
            cache.removeAsync(LOCK_KEY, member.toString());
        }
    }

    @Merged
    public void merged(MergeEvent mergeEvent)   {
        cache.removeAsync(LOCK_KEY);

    }

    private List<Address> minus(List<Address> original, List<Address> another) {
        ArrayList<Address> result = new ArrayList(another);
        result.removeAll(original);
        return result;
    }

    private String getAddress() {
        return cache.getRpcManager().getAddress().toString();
    }


    @Override
    public boolean acquire() {
        if(isAcquired()) {
            return true;
        }
        String address = getAddress();
        Object previous = cache.putIfAbsent(LOCK_KEY, address);
        if (previous == null) {
            return true;
        }
        return false;
    }


    @Override
    public boolean release() {
        if (isAcquired()) {
            Object previous = cache.remove(LOCK_KEY, getAddress());
            if (previous != null) {
                return true;
            }
        }
        return false;
    }
}
