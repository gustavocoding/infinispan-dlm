Infinispan dlm
--------------

This is a simple DLM (Distributed Lock Manager) on top of Infinispan. It can be used to coordinate access to resources 
cluster wide; only one node can acquire the lock, and it'll be held until it explicity releases it or if the node crashes 
the lock will be removed, and can be acquired by another member.

#### Usage ####

        package com.gustavonale.infinispan.dlm;

        DistributedLock distributedLock = DistributedLockBuilder.build();

        boolean lockAcquired = distributedLock.acquire();
        
        if(lockAcquired) {
            // Do some task
        }
        
        distributedLock.release();

    



