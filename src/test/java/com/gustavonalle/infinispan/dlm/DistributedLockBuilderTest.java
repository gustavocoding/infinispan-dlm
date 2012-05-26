package com.gustavonalle.infinispan.dlm;


import com.gustavonale.infinispan.dlm.DistributedLock;
import com.gustavonale.infinispan.dlm.DistributedLockBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class DistributedLockBuilderTest {

    @Test
    public void testBuild() throws Exception {
        DistributedLock lock = DistributedLockBuilder.build();
        assertNotNull(lock);
    }
}
