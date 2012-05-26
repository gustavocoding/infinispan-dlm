package com.gustavonalle.infinispan.dlm;


import com.gustavonale.infinispan.dlm.DistributedLock;
import com.gustavonale.infinispan.dlm.DistributedLockBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class InfinispanDistributedLockTest {

    @Test
    public void testAcquire() throws Exception {
        DistributedLock lock = DistributedLockBuilder.build();

        boolean acquired = lock.acquire();
        assertTrue(acquired, "Should acquire lock");

        boolean acquiredAgain = lock.acquire();
        assertFalse(acquiredAgain, "Should not reacquire the lock");

        boolean released = lock.release();
        assertTrue(released, "Should release lock");
        assertFalse(lock.isAcquired(), "Lock should be available");
        assertTrue(lock.acquire(), "Lock should be acquirable");

    }

}
