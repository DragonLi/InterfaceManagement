package typemodel;

import MainTool.MiscUtils;

public class PackageDefinition {
    public String NameSpace;
    public String[] Dependency;
    public RecordTypeDefinition[] TypList;

    public void NormalizeName() {
        if (Dependency == null)
            Dependency= MiscUtils.EmptyStringArray();
        if (TypList == null)
            TypList=MiscUtils.EmptyRecTyDefArray();
        if (NameSpace != null)
            NameSpace = NameSpace.trim();

        for (int i = 0, typListLength = TypList.length; i < typListLength; i++) {
            RecordTypeDefinition typDef = TypList[i];
            typDef.NormalizeName();
        }
    }
}
