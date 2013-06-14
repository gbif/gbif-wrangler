package org.gbif.wrangler.lock;

import java.util.concurrent.TimeUnit;

/**
 * A lock guarding access to a host.
 *
 * All these methods may throw a {@link LockingException}.
 */
public interface Lock {

  /**
   * Gets the lock and waits until it can be acquired.
   */
  void lock();

  /**
   * Tries to acquire the lock and returns if it was successful. This doesn't block and returns immediately.
   *
   * @return whether it was able to acquire the lock
   */
  boolean tryLock();

  /**
   * Tries to acquire the lock for a certain period of time and giving up if it couldn't acquire it.
   *
   * @param time how long to wait for the lock
   * @param unit of the time
   *
   * @return whether it was able to acquire the lock
   */
  boolean tryLock(long time, TimeUnit unit);

  /**
   * Releases a lock.
   */
  void unlock();

}
