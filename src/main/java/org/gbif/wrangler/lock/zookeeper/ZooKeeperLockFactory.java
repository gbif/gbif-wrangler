package org.gbif.wrangler.lock.zookeeper;

import org.gbif.wrangler.lock.Lock;
import org.gbif.wrangler.lock.LockFactory;
import org.gbif.wrangler.lock.LockingException;

import java.util.concurrent.TimeUnit;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.imps.CuratorFrameworkState;
import com.netflix.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import com.netflix.curator.framework.recipes.locks.Lease;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Uses ZooKeeper to hold locks.
 * <p/>
 * It creates uses a path <em>/lockedUrls</em> to manage those locks under whichever namespace you chose. But the
 * implementation might be more complex than just creating a single node in here because it is implemented using
 * Curator.
 */
public class ZooKeeperLockFactory implements LockFactory {

  private static class ZooKeeperLock implements Lock {

    private final InterProcessSemaphoreV2 semaphore;

    private Lease lease;

    private ZooKeeperLock(InterProcessSemaphoreV2 semaphore) {
      this.semaphore = semaphore;
    }

    @Override
    public void lock() {
      try {
        lease = semaphore.acquire();
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

    @Override
    public boolean tryLock() {
      try {
        lease = semaphore.acquire(1, TimeUnit.MILLISECONDS);
        return lease != null;
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
      try {
        lease = semaphore.acquire(time, unit);
        return lease != null;
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }

    @Override
    public void unlock() {
      try {
        semaphore.returnLease(lease);
      } catch (Exception e) {
        throw new LockingException(e);
      }
    }
  }

  private static final int DEFAULT_LEASES = 1;

  private final String lockingPath;

  private final CuratorFramework curator;

  private final int maxLeases;

  public ZooKeeperLockFactory(CuratorFramework curator, int maxLeases, String lockingPath) {
    this.curator = checkNotNull(curator, "curator can't be null");
    checkArgument(curator.getState() == CuratorFrameworkState.STARTED, "curator has to be started");

    checkArgument(maxLeases > 0, "maxLeases has to be greater than zero");
    this.maxLeases = maxLeases;
    this.lockingPath = lockingPath;
  }

  public ZooKeeperLockFactory(CuratorFramework curator, String lockingPath) {
    this(curator, DEFAULT_LEASES, lockingPath);
  }

  @Override
  public void clearLock(String name) {
    try {
      curator.delete().forPath(lockingPath + name);
    } catch (Exception e) {
      throw new LockingException(e);
    }
  }

  @Override
  public Lock makeLock(String name) {
    InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(curator, lockingPath + name, maxLeases);
    return new ZooKeeperLock(semaphore);
  }
}
