Infinispan-dlm
--------------

This is a simple DLM (Distributed Lock Manager) on top of Infinispan. It can be used to coordinate access to resources 
cluster wide; only one process can acquire the lock, and it'll be held until explicity released. 
When the lock holder crashes the DLM will detect it and remove the lock, making it available for acquiring again.

The lock acquisition and release are both non-blocking, and are supposed to be used for coarse grained process control,
like avoiding two process running at the same time in different machines

#### Usage ####

        package com.gustavonale.infinispan.dlm;

        DistributedLock distributedLock = DistributedLockBuilder.build();

        boolean lockAcquired = distributedLock.acquire();
        
        if(lockAcquired) {
            // Start some processing
        }
        
        distributedLock.release();

    



