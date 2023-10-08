package gradleplugin.exeptions;

public class UncommittedException extends RuntimeException{
    public UncommittedException(String message) {
        super(message);
    }
}
