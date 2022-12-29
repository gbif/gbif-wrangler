/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gbif.wrangler.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test unit for the {@link Mutex} and {@link ReadWriteMutexFactory} classes.
 */
public class ReadWriteMutexFactoryTest {

  /**
   * {@link ReadWriteMutexFactory} that does nothing.
   */
  private static class NoLockReadWriteMutexFactory implements ReadWriteMutexFactory {

    public static class NoOpMutex implements Mutex {

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
    mutex.doInLock(atomicInteger::incrementAndGet);
    Assert.assertEquals(1, atomicInteger.get());
  }
}
