package org.gbif.wrangler.lock;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test unit for the {@link Mutex} and {@link ReadWriteMutexFactory} classes.
 */
public class ReadWriteMutexFactoryTest {

  /**
   * {@link ReadWriteMutexFactory} that does nothing.
   */
  private class NoLockReadWriteMutexFactory implements ReadWriteMutexFactory {

    public class NoOpMutex implements Mutex {

      @Override
      public void acquire() {
        //do nothing
      }

      @Override
      public boolean acquire(long time, TimeUnit unit) {
        return false;
      }

      @Override
      public void release() {
        //do nothing
      }
    }

    @Override
    public Mutex createReadMutex(String name) {
      return new NoOpMutex();
    }

    @Override
    public Mutex createWriteMutex(String name) {
      return new NoOpMutex();
    }
  }

  /**
   * Performs a execution using to {@link Mutex#doInLock(Mutex.Action)} method.
   */
  @Test
  public void doInLockTest() {
    ReadWriteMutexFactory factory = new NoLockReadWriteMutexFactory();
    Mutex mutex = factory.createReadMutex("read");
    AtomicInteger atomicInteger = new AtomicInteger(0);
    mutex.doInLock(() -> {
      atomicInteger.incrementAndGet();
    });
    Assert.assertEquals(1, atomicInteger.get());
  }
}
