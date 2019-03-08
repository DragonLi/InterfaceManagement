package typemodel;

public class LoadedRecordTypeDecl extends LoadedTypeDecl {
    public LoadedPackage pack;
    public RecordTypeDefinition definition;


    public LoadedRecordTypeDecl(RecordTypeDefinition typ, LoadedPackage pack) {
        this.pack = pack;
        definition = typ;
    }

    @Override
    public void ResolveTypeReference(TypeEnv typeEnv) {
        for (int i = 0; i < definition.fieldTypeList.length; i++) {
            TypeDecl declType = definition.fieldTypeList[i].declType;
            LoadedTypeDecl t = typeEnv.SearchTypeByName(declType);
            if (t == null)
                throw new ParameterTypeNotFoundException(declType.GetTypeName(), pack.source.getAbsolutePath() + "," + definition.typeName);
        }
    }

    @Override
    public String GetImportName() {
        return pack.pack.NameSpace+"."+definition.typeName;
    }

    @Override
    public String GetTypeName() {
        return definition.typeName;
    }
}
