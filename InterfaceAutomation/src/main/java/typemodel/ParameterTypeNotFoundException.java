package typemodel;

public class ParameterTypeNotFoundException extends RuntimeException {
    private final String typName;
    private final String loc;

    public ParameterTypeNotFoundException(String typeName,String location) {
        typName = typeName;
        loc = location;
    }

    @Override
    public String getMessage() {
        return typName+" not found, location:"+loc+"\n"+ super.getMessage();
    }
}
