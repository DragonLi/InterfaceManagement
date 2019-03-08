package typemodel;

public class StringTypeDecl extends PrimitiveTypeDecl {
    public static StringTypeDecl getInstance() {
        return new StringTypeDecl();
    }

    @Override
    public String GetTypeName() {
        return "String";
    }
}
