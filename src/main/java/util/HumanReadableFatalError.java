package util;

public class HumanReadableFatalError extends RuntimeException {


    private String message;
    public HumanReadableFatalError(String exceptionType, Exception ex)
    {
        message = "Fatal Error ocurred '"+exceptionType +"'\n"
                + "reason: \n" + ex.getMessage();
    }
    public HumanReadableFatalError(String exceptionType, String customMessage)
    {
        message = "Fatal Error ocurred '"+exceptionType +"'\n"
                + "reason: \n" + customMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
