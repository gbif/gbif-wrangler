package org.gbif.wrangler.lock;

import org.junit.Assert;
import org.junit.Test;

/**
 * LockFactory test class.
 */
public class NoLockFactoryTest {

  private static final String LOCK_NAME = "foo";

  @Test
  public void testClearLock() {
    LockFactory lockFactory = new NoLockFactory();
    lockFactory.clearLock(LOCK_NAME);
  }

  @Test
  public void testDuplicateLock() {
    LockFactory lockFactory = new NoLockFactory();

    Lock lock1 = lockFactory.makeLock(LOCK_NAME);
    Lock lock2 = lockFactory.makeLock(LOCK_NAME);

    Assert.assertTrue(lock1.tryLock());
    Assert.assertTrue(lock2.tryLock());
  }
}
