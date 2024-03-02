public class InvalidConfigFileException extends Exception{


    // This exception is used in the config reader, in order to error-check and ensure that the formatting and information is valid
    public InvalidConfigFileException(String message) {
        super(message);
    }
}
