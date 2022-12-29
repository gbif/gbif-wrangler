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
