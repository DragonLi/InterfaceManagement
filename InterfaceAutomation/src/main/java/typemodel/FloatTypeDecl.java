package typemodel;

public class FloatTypeDecl extends PrimitiveTypeDecl {

    public static FloatTypeDecl getInstance() {
        return new FloatTypeDecl();
    }

    @Override
    public String GetTypeName() {
        return "float";
    }
}
