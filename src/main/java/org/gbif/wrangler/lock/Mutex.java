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
 * Mutex to support to hold a Zookeeper lock. All processes that use the same lock path will achieve an inter-process critical section.
 */
public interface Mutex {

  /**
   * Functional Interface to a abstract an execution of arbitrary code in a critical section.
   */
  @FunctionalInterface
  interface Action {

    void execute();
  }

  /**
   * Acquires a mutex/lock.
   */
  void acquire();

  /**
   * Tries to get the mutex/lock in certain amount of time.
   *
   * @return a boolean flag indicating if the mutex was acquired
   */
  boolean acquire(long time, TimeUnit unit);

  /**
   * Performs the mutex/lock release.
   */
  void release();

  /**
   * Executes a critical action in the context of a Mutex/Lock.
   * In case of any unexpected error, the lock is forcibly released.
   *
   * @param action critical action to be executed
   */
  default void doInLock(Action action) {
    try {
      acquire();
      action.execute();
    } finally {
      release();
    }
  }
}
