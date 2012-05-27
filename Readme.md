Infinispan-dlm
--------------

This is a simple DLM (Distributed Lock Manager) on top of Infinispan. It can be used to coordinate access to resources 
cluster wide; only one process can acquire the lock, and it'll be held until explicity released or if the process crashes
or is shutdown, the lock will be removed, and can be acquired by another member.

The lock acquisition and releasing are both non-blocking, and are supposed to be used for coarse grained process control,
like avoiding two process running at the same time in different machines

#### Usage ####

        package com.gustavonale.infinispan.dlm;

        DistributedLock distributedLock = DistributedLockBuilder.build();

        boolean lockAcquired = distributedLock.acquire();
        
        if(lockAcquired) {
            // Do some task
        }
        
        distributedLock.release();

    



