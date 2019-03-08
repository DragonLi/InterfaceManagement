package typemodel;

public class BoolTypeDecl extends PrimitiveTypeDecl {
    public static BoolTypeDecl getInstance() {
        return new BoolTypeDecl();
    }

    @Override
    public String GetTypeName() {
        return "boolean";
    }
}
