package org.gbif.wrangler.lock;

/**
 * Simple factory to create Read or Write Mutex to synchronize critical executions.
 */
public interface ReadWriteMutexFactory {

  /**
   * Creates a Read mutex using a curator framework instances and a ZK path.
   *
   * @param name to create the mutex for
   *
   * @return a read mutex
   */
  Mutex createReadMutex(String name);

  /**
   * Creates a Write mutex using a curator framework instances and a ZK path.
   *
   * @param name to create the mutex for
   *
   * @return a read mutex
   */

  Mutex createWriteMutex(String name);
}
