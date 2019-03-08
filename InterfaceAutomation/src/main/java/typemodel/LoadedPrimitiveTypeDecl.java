package typemodel;

import java.util.HashMap;
import java.util.Map;

public class LoadedPrimitiveTypeDecl extends LoadedTypeDecl {
    public PrimitiveTypeDecl typeDecl;
    private static Map<String, LoadedPrimitiveTypeDecl> primitiveMap;

    static {
        primitiveMap = new HashMap<>();
    }

    public static LoadedTypeDecl Find(TypeDecl typeDecl) {
        if (typeDecl instanceof PrimitiveTypeDecl) {
            String typNm = typeDecl.GetTypeName();
            LoadedPrimitiveTypeDecl loaded = primitiveMap.get(typNm);
            if (loaded == null) {
                //TODO date time type should not cache in this way, date must come with format!
                loaded = new LoadedPrimitiveTypeDecl((PrimitiveTypeDecl) typeDecl);
                primitiveMap.put(typNm, loaded);
            }
            return loaded;
        }
        return null;
    }

    public LoadedPrimitiveTypeDecl(PrimitiveTypeDecl typeDecl) {
        this.typeDecl = typeDecl;
    }

    @Override
    public void ResolveTypeReference(TypeEnv typeEnv) {
    }

    @Override
    public String GetImportName() {
        return "";
    }

    @Override
    public String GetTypeName() {
        return typeDecl.GetTypeName();
    }
}
