package MainTool;

import com.fasterxml.jackson.databind.JsonNode;
import typemodel.*;

import java.util.HashMap;
import java.util.Map;

public class LoadedServiceFunction {
    public LoadedServiceInterface serviceInt;
    public ServiceFunction definition;
    public TypeEnv env;
    public JsonNode localContext;
    private LoadedTypeDecl inputDeclInfo;
    private LoadedTypeDecl outputDeclInfo;

    public static LoadedServiceFunction Load(LoadedServiceInterface serviceInt, ServiceFunction definition, JsonNode child) {
        LoadedServiceFunction instance = new LoadedServiceFunction();
        TypeEnv env = new TypeEnv(serviceInt.sourceRoot);
        instance.serviceInt = serviceInt;
        instance.env = env;
        instance.localContext = child;
        instance.definition = definition;
        LoadedTypeDecl inTyp = env.LoadParameter(definition.Input);
        LoadedTypeDecl outTyp = env.LoadParameter(definition.Output);
        instance.inputDeclInfo = inTyp;
        instance.outputDeclInfo = outTyp;
        env.optimizeSpace();
        return instance;
    }

    public StringBuilder TestGenerateJavaType() {
        StringBuilder b = new StringBuilder();
        return env.TestGenerateJavaType(b);
    }

    public void GenerateJavaDataDefinition(String rootPath) {
        env.GenerateJavaDataDefinition(rootPath);
    }

    public StringBuilder GenerateFromTemplate(StringBuilder template){
        return GenerateFromTemplate(env,inputDeclInfo,outputDeclInfo,template);
    }

    private StringBuilder GenerateFromTemplate(TypeEnv env, LoadedTypeDecl inTyp, LoadedTypeDecl outTyp, StringBuilder template) {
        Map<String, String> shortCutCtx = new HashMap<>();
        String qName = inTyp.GetImportName();
        if (qName != null)
            shortCutCtx.put("importInputTypeNameSpace", "import " + qName + ";");
        qName = outTyp.GetImportName();
        if (qName != null)
            shortCutCtx.put("importOutputTypeNameSpace", "import " + qName + ";");

        shortCutCtx.put("BackedFunctionName", definition.BackedFunctionName);
        shortCutCtx.put("InputTypeName", inTyp.GetTypeName());
        shortCutCtx.put("OutputTypeName", outTyp.GetTypeName());
        shortCutCtx.put("InputVarName", definition.Input.decl.declName);
        shortCutCtx.put("Address", definition.Address);
        shortCutCtx.put("Name", definition.Name);
        shortCutCtx.put("Description", definition.Description);

//        System.out.println("global context for " + definition.BackedFunctionName);
//        MiscUtils.PrintMap(shortCutCtx);

        //Handle InputTypeFieldList
        final String openTag = "#InputTypeFieldList!";
        final String closeTag = "!InputTypeFieldList#";
        final String separatorKey = "separator:";
        final String separatorEndTag="!";

        int startIndex = template.indexOf(openTag);
        while (startIndex != -1){
            String[] fieldList = GenerateInputFieldList();
            int templateStart = startIndex + openTag.length();

            String separator="";
            int separatorStart = templateStart + separatorKey.length();
            String tmp = template.substring(templateStart, separatorStart);
            if (separatorKey.equalsIgnoreCase(tmp)){
                int separatorEnd = template.indexOf(separatorEndTag,separatorStart);
                if (separatorEnd == -1)
                    throw new TagNotClosedException("closing tag of separator is not found");
                separator = template.substring(separatorStart,separatorEnd);
                templateStart=separatorEnd+separatorEndTag.length();
            }

            int endIndex = template.indexOf(closeTag,templateStart);
            if (endIndex == -1)
                throw new TagNotClosedException("closing tag !InputTypeFieldList# not found");

            String repeatedTemp = template.substring(templateStart, endIndex);
            String lastPart = template.substring(endIndex+closeTag.length());
            template.delete(startIndex,template.length());
            Map<String,String> fieldMap = new HashMap();
            for (int i = 0, funcDefListLength = fieldList.length; i < funcDefListLength; i++) {
                String fieldName = fieldList[i];
                fieldMap.put("FieldName",fieldName);
                StringBuilder def = MiscUtils.replaceWithContext(new StringBuilder(repeatedTemp), fieldMap);
                template.append(def);
                if (i != funcDefListLength-1){
                    template.append(separator);
                }
            }
            template.append(lastPart);
            startIndex = template.indexOf(openTag,template.length()-lastPart.length());
        }


        MiscUtils.replaceWithContext(template, shortCutCtx);
        MiscUtils.replaceWithContext(template, serviceInt.globalContext);

        return template;
    }

    private String[] GenerateInputFieldList() {
        VarDecl decl = definition.Input.decl;
        if (decl.declType instanceof RecordTypeDecl){
            LoadedRecordTypeDecl recDef = (LoadedRecordTypeDecl) inputDeclInfo;
            VarDecl[] fieldTypeList = recDef.definition.fieldTypeList;
            String[] fieldList = new String[fieldTypeList.length];
            for (int i = 0, fieldTypeListLength = fieldTypeList.length; i < fieldTypeListLength; i++) {
                VarDecl varDecl = fieldTypeList[i];
                fieldList[i] = varDecl.declName;
            }
            return fieldList;
        }
        return new String[]{decl.declName};
    }
}
