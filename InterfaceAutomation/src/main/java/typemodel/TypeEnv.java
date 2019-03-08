package typemodel;

import MainTool.MiscUtils;

import java.io.File;
import java.util.ArrayList;

public class TypeEnv {
    private final String rootPath;
    private ArrayList<LoadedPackage> loadedPackages;

    public TypeEnv(String parentPath) {
        rootPath = parentPath;
    }

    public StringBuilder TestGenerateJavaType(StringBuilder b) {
        for (LoadedPackage loaded : loadedPackages) {
            loaded.TestGenerateJavaDef(b).append('\n');
        }
        return b;
    }

    public void GenerateJavaDataDefinition(String rootPath) {
        for (LoadedPackage loaded : loadedPackages) {
            loaded.GenerateJavaDataDefinition(rootPath);
        }
    }

    public LoadedTypeDecl LoadParameter(Parameter param) {
        LoadPackageFromFile(param.CustomDependency);
        LoadedTypeDecl t = SearchTypeByName(param.decl.declType);
        if (t == null)
            throw new ParameterTypeNotFoundException(param.decl.declType.GetTypeName(), "Parameter " + param.decl.declName);
        return t;
    }

    public LoadedTypeDecl SearchTypeByName(TypeDecl typeDecl) {
        LoadedTypeDecl t = LoadedPrimitiveTypeDecl.Find(typeDecl);
        if (t != null)
            return t;
        if (loadedPackages == null)
            return null;
        //TODO check cache first
        for (LoadedPackage loaded : loadedPackages) {
            t = loaded.SearchTypeByName(typeDecl);
            if (t != null) {
                //TODO add to cache
                return t;
            }
        }
        return null;
    }

    public LoadedPackage LoadPackageFromFile(String path) {
        if (path == null || path.trim().isEmpty())
            return null;

        File source = new File(rootPath, path);
        LoadedPackage search = SearchLoaded(source);
        if (search != null) {
            return search;
        }
        /* don't skip non-existing file: all declare package must exists!
        if (!source.exists())
            return;
            */
        try {
            PackageDefinition pack = MiscUtils.ReadJson(source, PackageDefinition.class);
            pack.NormalizeName();
            LoadedPackage loaded = new LoadedPackage();
            loaded.pack = pack;
            loaded.source = source;
            if (loadedPackages == null)
                loadedPackages = new ArrayList<>();
            loadedPackages.add(loaded);
            loaded.LoadDependence(this);
            return loaded;
        } catch (Throwable ex) {
            throw new PackageLoadFailException("error loading package:" + source.getAbsolutePath(), ex);
        }
    }

    private LoadedPackage SearchLoaded(File path) {
        if (loadedPackages == null)
            return null;
        for (LoadedPackage loaded : loadedPackages) {
            if (loaded.source.equals(path))
                return loaded;
        }
        return null;
    }

    public void optimizeSpace() {
        loadedPackages.trimToSize();
    }
}
