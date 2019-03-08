package typemodel;

import java.io.File;

public class LoadedListTypeDecl extends LoadedTypeDecl {
    private final RecordTypeDefinition elementTy;
    private final LoadedPackage pack;
    private final ListTypeDecl tyDecl;

    public LoadedListTypeDecl(RecordTypeDefinition eleDef, LoadedPackage loadedPackage, ListTypeDecl typeDecl) {
        super();
        elementTy=eleDef;
        pack = loadedPackage;
        tyDecl = typeDecl;
    }

    @Override
    public void ResolveTypeReference(TypeEnv typeEnv) {
    }

    @Override
    public String GetImportName() {
        return pack.pack.NameSpace+"."+tyDecl.ResolveName();
    }

    @Override
    public String GetTypeName() {
        return tyDecl.GetTypeName();
    }
}
