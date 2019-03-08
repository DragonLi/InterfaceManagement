package typemodel;

public class VoidTypeDecl extends PrimitiveTypeDecl {
    private static VoidTypeDecl instance = new VoidTypeDecl();

    private VoidTypeDecl() {
        //no instance
    }

    public static VoidTypeDecl getInstance() {
        return instance;
    }
    @Override
    public String GetTypeName() {
        return "void";
    }
}
