package typemodel;

public class VarDecl {
    public TypeDecl declType;
    public String declName;
    public boolean IsNullable;
    public String DefaultValue;
    public String description;

    public void Normalize() {
        declType.NormalizeName();
        declName = declName.trim();
        //non primitive value dose not allow default value
        if (!(declType instanceof PrimitiveTypeDecl))
            DefaultValue=null;
    }

    public StringBuilder GenerateDefaultValue(StringBuilder b) {
        if (!(declType instanceof PrimitiveTypeDecl))
            return b;
        PrimitiveTypeDecl pDecl = (PrimitiveTypeDecl) declType;
        if (DefaultValue != null && !DefaultValue.isEmpty())
        {
            b.append("=");
            if (pDecl instanceof StringTypeDecl)
                b.append('\"').append(DefaultValue).append('\"');
            else
                b.append(DefaultValue);
        }
        return b;
    }
}
