package typemodel;

public class DoubleTypeDecl extends PrimitiveTypeDecl {
    public static DoubleTypeDecl getInstance() {
        return new DoubleTypeDecl();
    }

    @Override
    public String GetTypeName() {
        return "double";
    }
}
