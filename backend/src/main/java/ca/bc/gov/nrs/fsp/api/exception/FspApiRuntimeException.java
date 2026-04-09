package ca.bc.gov.nrs.fsp.api.exception;

/**
 * The type School api runtime exception.
 */
public class FspApiRuntimeException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 5241655513745148898L;

  /**
   * Instantiates a new School api runtime exception.
   *
   * @param message the message
   */
  public FspApiRuntimeException(String message) {
		super(message);
	}

  /**
   * Instantiates a new School api runtime exception.
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public FspApiRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
