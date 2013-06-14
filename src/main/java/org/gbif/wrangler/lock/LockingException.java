package org.gbif.wrangler.lock;

/**
 * Exception to communicates errors interacting and manipulating locks.
 */
public class LockingException extends RuntimeException {

  private static final long serialVersionUID = -1286514497160733184L;

  /**
   * Default constructor.
   */
  public LockingException(Exception e) {
    super(e);
  }

}
