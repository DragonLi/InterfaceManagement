package typemodel;

public class ListTypeDecl extends TypeDecl {
    public TypeDecl ElementType;

    private String tyName;

    @Override
    public void NormalizeName() {
        super.NormalizeName();
        ElementType.NormalizeName();
        if ("void".equals(ElementType))
            throw new VoidCanNotBeElementException();
    }

    @Override
    public String GetTypeName() {
        if (tyName == null)
            tyName = "List<" + ElementType.GetTypeName() + ">";
        return tyName;
    }

    @Override
    public String ResolveName() {
        return ElementType.ResolveName();
    }
}
