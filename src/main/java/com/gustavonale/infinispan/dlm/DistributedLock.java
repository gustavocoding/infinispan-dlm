package com.gustavonale.infinispan.dlm;

/**
 * Distributed lock
 */
public interface DistributedLock {

    /**
     * Acquire the distributed lock
     *
     * @return true if lock is successfully acquired
     */
    public boolean acquire();

    /**
     * Release Lock
     *
     * @return true if lock successfully released
     */
    public boolean release();


    /**
     * Checks the lock
     *
     * @return true if the lock is acquired by any node
     */
    public boolean isAcquired();


}
