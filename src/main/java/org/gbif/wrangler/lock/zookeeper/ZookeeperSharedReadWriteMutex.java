package org.gbif.wrangler.lock.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import org.gbif.wrangler.lock.LockingException;
import org.gbif.wrangler.lock.Mutex;
import org.gbif.wrangler.lock.ReadWriteMutexFactory;

import java.util.concurrent.TimeUnit;

/**
 * Zookeeper factory for shared read and write mutexes.
 */
public class ZookeeperSharedReadWriteMutex implements ReadWriteMutexFactory {

  private final CuratorFramework curatorFramework;
  private final String lockingPath;

  /**
   * Creates a Zookeeper/Curator factory.
   *
   * @param curatorFramework curator framework instance
   * @param lockingPath      ZK path where the shared lock is created
   */
  public ZookeeperSharedReadWriteMutex(CuratorFramework curatorFramework, String lockingPath) {
    this.curatorFramework = curatorFramework;
    this.lockingPath = lockingPath;
  }

  /**
   * Creates a Read mutex using a InterProcessReadWriteLock.
   *
   * @param name to create the mutex for
   *
   * @return a read mutex
   */
  public Mutex createReadMutex(String name) {
    return new ZookeeperMutex(new InterProcessReadWriteLock(curatorFramework, lockingPath + name).readLock());
  }

  /**
   * Creates a Write mutex using a InterProcessReadWriteLock.
   *
   * @param name to create the mutex for
   *
   * @return a read mutex
   */
  public Mutex createWriteMutex(String name) {
    return new ZookeeperMutex(new InterProcessReadWriteLock(curatorFramework, lockingPath + name).writeLock());
  }

  /**
   * Mutex to support to hold a Zookeeper lock. All processes that use the same lock path will achieve an inter-process critical section.
   */
  public static class ZookeeperMutex implements Mutex {

    //Read or Write Mutex.
    private final InterProcessMutex mutex;

    /**
     * Creates a Read-or-Write mutex that will be wrapped by this class.
     *
     * @param mutex read or write mutex to be wrapped
     */
    private ZookeeperMutex(InterProcessMutex mutex) {
      this.mutex = mutex;
    }

    /**
     * Acquires a mutex/lock.
     */
    @Override
    public void acquire() {
      try {
        mutex.acquire();
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

    /**
     * Tries to get the mutex/lock in certain amount of time.
     *
     * @param time time to wait
     * @param unit time unit
     *
     * @return true if the mutex was acquired, false if not
     */
    @Override
    public boolean acquire(long time, TimeUnit unit) {
      try {
        return mutex.acquire(time, unit);
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

    /**
     * Performs the mutex/lock release.
     */
    @Override
    public void release() {
      try {
        mutex.release();
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

  }

}
