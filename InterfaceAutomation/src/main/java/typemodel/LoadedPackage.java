package typemodel;

import MainTool.MiscUtils;

import java.io.File;

public class LoadedPackage {
    public PackageDefinition pack;
    public File source;
    public LoadedPackage[] Dependency;

    private LoadedTypeDecl[] resolveLoadedList;
    private String[] depListJavaStyle;
    private String javaStylePath;

    public LoadedTypeDecl SearchTypeByName(TypeDecl typeDecl) {
        String typeName = typeDecl.GetTypeName();
        if (typeDecl instanceof ListTypeDecl)
        {
            ListTypeDecl lstTyDecl = (ListTypeDecl) typeDecl;
            typeName = lstTyDecl.ElementType.GetTypeName();
            RecordTypeDefinition eleDef = searchInLocalDef(typeName);
            return new LoadedListTypeDecl(eleDef,this,lstTyDecl);
        }
        RecordTypeDefinition refDef = searchInLocalDef(typeName);
        if (refDef != null)
            return new LoadedRecordTypeDecl(refDef, this);
        return null;
    }

    private RecordTypeDefinition searchInLocalDef(String typeName) {
        RecordTypeDefinition[] typList = pack.TypList;
        for (int i = 0, typListLength = typList.length; i < typListLength; i++) {
            RecordTypeDefinition typ = typList[i];
            if (typeName.equals(typ.typeName))
                return typ;
        }
        return null;
    }

    public void ResolveTypeReference(TypeEnv typeEnv) {
        RecordTypeDefinition[] typList = pack.TypList;
        if (typList == null || typList.length == 0) {
            resolveLoadedList = MiscUtils.EmptyLoadedTypeArray();
            return;
        }

        resolveLoadedList = new LoadedTypeDecl[typList.length];
        for (int i = 0, typListLength = typList.length; i < typListLength; i++) {
            RecordTypeDefinition typ = typList[i];
            LoadedTypeDecl loading = new LoadedRecordTypeDecl(typ, this);
            loading.ResolveTypeReference(typeEnv);
            resolveLoadedList[i] = loading;
        }
    }

    public StringBuilder TestGenerateJavaDef(StringBuilder b) {
        FormatAsPackageName();
        GeneratePackageHeader(b);

        RecordTypeDefinition[] typList = pack.TypList;
        for (int i = 0, typListLength = typList.length; i < typListLength; i++) {
            RecordTypeDefinition typ = typList[i];
            typ.GenerateJavaDef(b).append('\n');
        }
        return b;
    }

    public void GenerateJavaDataDefinition(String rootPath) {
        RecordTypeDefinition[] typList = pack.TypList;
        for (int i = 0, typListLength = typList.length; i < typListLength; i++) {
            StringBuilder b = new StringBuilder();
            FormatAsPackageName();
            GeneratePackageHeader(b);
            RecordTypeDefinition typ = typList[i];
            typ.GenerateJavaDef(b).append('\n');
            MiscUtils.writeToFile(new File(rootPath,javaStylePath+'/'+typ.typeName+".java"),b);
        }
    }

    private StringBuilder GeneratePackageHeader(StringBuilder b) {
        b.append("package ").append(pack.NameSpace).append(";\n");
        for (int i = 0, depListJavaStyleLength = depListJavaStyle.length; i < depListJavaStyleLength; i++) {
            String dep = depListJavaStyle[i];
            b.append("import ").append(dep).append(";\n");
        }
        b.append("import java.io.Serializable;\n");
        b.append("import java.util.List;\n");
        return b;
    }

    private void FormatAsPackageName() {
        if (depListJavaStyle != null)
            return;
        javaStylePath = pack.NameSpace.replace('.','/');

        String[] dependency = pack.Dependency;
        if (dependency == null || dependency.length == 0) {
            depListJavaStyle = MiscUtils.EmptyStringArray();
            return;
        }

        depListJavaStyle = new String[dependency.length];
        for (int i = 0, dependencyLength = dependency.length; i < dependencyLength; i++) {
            LoadedPackage dep = Dependency[i];
            depListJavaStyle[i] = dep.pack.NameSpace+".*";
        }
    }

    public void LoadDependence(TypeEnv env){
        String[] dependency = pack.Dependency;
        if (dependency == null || dependency.length == 0) {
            Dependency=MiscUtils.EmptyLoadedPackageArray();
            ResolveTypeReference(env);
            return;
        }
        Dependency = new LoadedPackage[dependency.length];
        for (int i = 0, dependencyLength = dependency.length; i < dependencyLength; i++) {
            String dep = dependency[i];
            Dependency[i] = env.LoadPackageFromFile(dep);
        }
        ResolveTypeReference(env);
    }
}
