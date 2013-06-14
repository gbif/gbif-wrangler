package org.gbif.wrangler.lock;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NoLockFactoryTest {

  @Test
  public void testDuplicateLock() {

    LockFactory lockFactory = new NoLockFactory();

    Lock lock1 = lockFactory.makeLock("foo");
    Lock lock2 = lockFactory.makeLock("foo");

    assertThat(lock1.tryLock()).isTrue();
    assertThat(lock2.tryLock()).isTrue();
  }

  public void testClearLock() {
    LockFactory lockFactory = new NoLockFactory();

    lockFactory.clearLock("foo");

  }
}
