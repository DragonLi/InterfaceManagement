package typemodel;

import java.util.List;

public class EnumTypeDecl extends PrimitiveTypeDecl {
    public List<String> LabelList;
    public String enumTypeName;

    @Override
    public String GetTypeName() {
        return enumTypeName;
    }
}
