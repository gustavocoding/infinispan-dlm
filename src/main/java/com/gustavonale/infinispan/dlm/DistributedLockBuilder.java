package com.gustavonale.infinispan.dlm;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

public final class DistributedLockBuilder {

    private DistributedLockBuilder() {}

    public static DistributedLock build() {

        GlobalConfiguration globalConfiguration = GlobalConfigurationBuilder.
                defaultClusteredBuilder()
                .transport()
                .addProperty("configurationFile", "jgroups-udp.xml").build();

        Configuration configuration = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .sync()
                .build();

        DefaultCacheManager defaultCacheManager = new DefaultCacheManager(globalConfiguration, configuration);
        defaultCacheManager.start();
        return new InfinispanDistributedLock(defaultCacheManager.getCache().getAdvancedCache());


    }


}
