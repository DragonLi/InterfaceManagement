package typemodel;

public abstract class LoadedTypeDecl {
    public abstract void ResolveTypeReference(TypeEnv typeEnv);

    public abstract String GetImportName();//TODO seperate loaded list type

    public abstract String GetTypeName();
}
