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
