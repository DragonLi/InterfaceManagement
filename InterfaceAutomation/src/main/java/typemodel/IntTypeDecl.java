package typemodel;

public class IntTypeDecl extends PrimitiveTypeDecl {

    public static IntTypeDecl getInstance() {
        return new IntTypeDecl();
    }
    @Override
    public String GetTypeName() {
        return "int";
    }
}
