package MainTool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import typemodel.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.reflect.Array.newInstance;

public class MiscUtils {
    private static final LoadedTypeDecl[] emptyLoadedTyArr = new LoadedTypeDecl[0];
    private static final LoadedPackage[] emptyLoadedPackagesArray = new LoadedPackage[0];
    private static final RecordTypeDefinition[] emptyRecTyDefArr = new RecordTypeDefinition[0];
    private static final String[] emptyStringArr = new String[0];
    private static final ServiceFunction[] emptyServiceFunArr = new ServiceFunction[0];

    private static ObjectMapper json = new ObjectMapper();

    static {
        json.configure(SerializationFeature.INDENT_OUTPUT, true);
        json.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        json.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        json.registerSubtypes(
                new NamedType(EnumTypeDecl.class, "enum")
                , new NamedType(IntTypeDecl.class, "int")
                , new NamedType(FloatTypeDecl.class, "float")
                , new NamedType(DoubleTypeDecl.class, "double")
                , new NamedType(BoolTypeDecl.class, "boolean")
                , new NamedType(StringTypeDecl.class, "string")
                , new NamedType(ListTypeDecl.class, "List<>")
                , new NamedType(RecordTypeDecl.class, "class")
        );
    }

    public static JsonNode readTree(String fn) {
        try {
            return json.readTree(new File(fn));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T ConvertFromTree(JsonNode tree, Class<T> objType) {
        return json.convertValue(tree, objType);
    }

    public static void WriteJson(Object obj, String fn) {
        try {
            json.writeValue(new File(fn), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T ReadJson(String fn, Class<T> objType) {
        try {
            return json.readValue(new File(fn), objType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T ReadJson(File file, Class<T> objType) {
        try {
            return json.readValue(file, objType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void StdOutput(Object obj) {
        try {
            System.out.println(json.writeValueAsString(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetJsonString(Object p) {
        try {
            return json.writeValueAsString(p);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T FromJsonStr(String t, Class<T> typ) {
        try {
            return json.readValue(t, typ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StringBuilder readAllContent(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            String str = new String(bytes);
            StringBuilder b = new StringBuilder(str);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T[] CopyArray(ArrayList<T> arr, Class<T> classInfo) {
        @SuppressWarnings("unchecked")
        T[] r = (T[]) newInstance(classInfo, arr.size());
        return arr.toArray(r);
    }

    public static String[] EmptyStringArray() {
        return emptyStringArr;
    }

    public static RecordTypeDefinition[] EmptyRecTyDefArray() {
        return emptyRecTyDefArr;
    }

    public static LoadedTypeDecl[] EmptyLoadedTypeArray() {
        return emptyLoadedTyArr;
    }

    public static LoadedPackage[] EmptyLoadedPackageArray() {
        return emptyLoadedPackagesArray;
    }

    public static ServiceFunction[] EmptyServiceFunctionArray() {
        return emptyServiceFunArr;
    }

    public static void writeToFile(String path,StringBuilder b){
        writeToFile(new File(path),b);
    }

    public static void writeToFile(File file, StringBuilder b) {
        try {
            Files.createDirectories(Paths.get(file.getParentFile().getAbsolutePath()));
            Files.write(Paths.get(file.getAbsolutePath()), b.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.CREATE);
            System.out.println("Write Success:" + file);
        } catch (IOException e) {
            System.out.println("Write failed:" + file);
            e.printStackTrace();
        }
    }

    public static StringBuilder replaceWithContext(StringBuilder template, Map<String, String> ctx) {
        int startIndex = template.indexOf("#");
        while (startIndex != -1){
            int endIndex = template.indexOf("#",startIndex+1);
            if (endIndex == -1)
                throw new TagNotClosedException("expected closing tag:#");
            String key = template.substring(startIndex+1,endIndex);
            String val = ctx.get(key);
            if (val != null){
                template.replace(startIndex,endIndex+1,val);
                endIndex=startIndex+val.length();
            }else
                endIndex++;
            startIndex = template.indexOf("#",endIndex);
        }
        /*ctx.forEach((key, val) -> {
            String specifiedKey = '#' + key + '#';
            int keyIndex = template.indexOf(specifiedKey);
            int len = key.length() + 2;
            while (keyIndex != -1) {
                template.replace(keyIndex, keyIndex + len, val);
                keyIndex = template.indexOf(specifiedKey);
            }
        });*/
        return template;
    }

    public static void PrintMap(Map<String, String> globalCtx) {
        globalCtx.forEach((key, value) -> System.out.println(key + "->" + value));
    }
}
