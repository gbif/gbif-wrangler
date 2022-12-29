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

/**
 * Factories to create locks.
 * <p/>
 * This is needed because for some of our datasets using more than one connection could overload the server. Locking is
 * thus expected to be on the host level but expects an arbitrary string.
 * <p/>
 * This is modeled after Lucene's LockFactory class.
 */
public interface LockFactory {

  /**
   * Tries to forcefully clear a lock if it exists. If it doesn't exist this request will be ignored.
   *
   * This should only throw a {@link LockingException} if there was an actual error from the underlying implementation.
   *
   * @param name the lock to clear
   */
  void clearLock(String name);

  /**
   * Returns a lock object for a name.
   *
   * This should only throw a {@link LockingException} if there was an actual error from the underlying implementation.
   *
   * @param name to create the lock for
   *
   * @return an object that can be used to acquire a lock for the given name
   */
  Lock makeLock(String name);

}
