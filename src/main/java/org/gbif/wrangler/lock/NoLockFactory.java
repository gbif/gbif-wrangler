package org.gbif.wrangler.lock;

import java.util.concurrent.TimeUnit;

/**
 * A lock factory that provides no locking at all. This should not be used in production but is good for testing,
 * debugging or running a one-off crawl.
 */
public class NoLockFactory implements LockFactory {

  private static final Lock SINGLETON_LOCK = new NoOpLock();

  /**
   * This is a convenience method to just return a no-op lock.
   *
   * @return a lock that does nothing
   */
  public static Lock getLock() {
    return SINGLETON_LOCK;
  }

  @Override
  public void clearLock(String name) {
    //do nothing
  }

  @Override
  public Lock makeLock(String name) {
    return SINGLETON_LOCK;
  }

  private static class NoOpLock implements Lock {

    @Override
    public void lock() {
      //do nothing
    }

    @Override
    public boolean tryLock() {
      return true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
      return true;
    }

    @Override
    public void unlock() {
      //do nothing
    }
  }
}
