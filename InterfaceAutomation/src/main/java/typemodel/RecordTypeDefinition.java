package typemodel;

public class RecordTypeDefinition {
    public String typeName;
    public VarDecl[] fieldTypeList;

    public void NormalizeName() {
        typeName = typeName.trim();
        for (VarDecl varDecl : fieldTypeList) {
            varDecl.Normalize();
        }
    }

    public StringBuilder GenerateJavaDef(StringBuilder b) {
        //TODO add description
        b.append("public class ").append(typeName).append(" implements Serializable {\n");
        for (VarDecl typ : fieldTypeList) {
            //TODO add description
            b.append("public ").append(typ.declType.GetTypeName()).append(' ').append(typ.declName);
            typ.GenerateDefaultValue(b).append(";\n");
        }
        b.append("}\n");
        return b;
    }
}
