package typemodel;

public class DateTimeTypeDecl extends PrimitiveTypeDecl {
    public String Format;

    @Override
    public String GetTypeName() {
        return "Date";
    }
}
