package homesnap.automation.framework;

public class FrameworkException extends Exception {
	
	/**
	 * Custom Framework exception. This exception should be thrown with appropriate message
	 * for all framework related failures.
	 */
	private static final long serialVersionUID = 5362430687198404270L;

	public FrameworkException() {}

    public FrameworkException(String message)
    {
       super(message);
    }

}
