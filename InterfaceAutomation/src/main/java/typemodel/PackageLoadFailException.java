package typemodel;

public class PackageLoadFailException extends RuntimeException {
    public PackageLoadFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
