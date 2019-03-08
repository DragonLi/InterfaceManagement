package MainTool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadedServiceInterface {
    public ServiceInterface definition;
    public File source;

    public JsonNode tree;
    public Map<String, String> globalContext;
    public String sourceRoot;
    public LoadedServiceFunction[] FuncDefList;

    public static LoadedServiceInterface Load(String path) {
        JsonNode tree = MiscUtils.readTree(path);
        ServiceInterface def = MiscUtils.ConvertFromTree(tree, ServiceInterface.class);
        def.NormalizeName();

        LoadedServiceInterface loader = new LoadedServiceInterface();
        loader.definition = def;
        loader.tree = tree;
        loader.source = new File(path);
        loader.sourceRoot = loader.source.getParentFile().getAbsolutePath();

        ArrayNode funcListNode = (ArrayNode) tree.findValue("FunctionList");
        Map<String, String> ctx = new HashMap();
        RouteMapping routeInfo = def.RouteInfo;
        ctx.put("ClientAccessUrl", routeInfo.ClientAccessUrl);
        ctx.put("ControllerName", routeInfo.ControllerName);
        ctx.put("CenterCode", routeInfo.CenterCode);
        ctx.put("ModuleName", routeInfo.ModuleName);
        ctx.put("VersionNum", def.VersionNum);
        loader.globalContext = ctx;

        ServiceFunction[] functionList = def.FunctionList;
        loader.FuncDefList = new LoadedServiceFunction[functionList.length];
        for (int i = 0, functionListLength = functionList.length; i < functionListLength; i++) {
            ServiceFunction serviceFunction = functionList[i];
            JsonNode child = funcListNode.get(i);
            loader.FuncDefList[i] = LoadedServiceFunction.Load(loader, serviceFunction, child);
        }
        return loader;
    }

    public void GenerateJavaDataDefinition(String rootPath){
        for (LoadedServiceFunction loadedServiceFunction : FuncDefList) {
            loadedServiceFunction.GenerateJavaDataDefinition(rootPath);
        }
    }

    public StringBuilder GenerateFromTemplate(StringBuilder template) {
        final String separatorKey = "separator:";
        int startIndex = template.indexOf("#!");
        while (startIndex != -1){
            int templateStart = startIndex + 2;

            String separator="";
            int separatorStart = templateStart + separatorKey.length();
            String tmp = template.substring(templateStart, separatorStart);
            if (separatorKey.equalsIgnoreCase(tmp)){
                int separatorEnd = template.indexOf("!", separatorStart);
                if (separatorEnd == -1)
                    throw new TagNotClosedException("closing tag of separator is not found");
                separator = template.substring(separatorStart,separatorEnd);
                templateStart=separatorEnd+1;
            }

            int endIndex = template.indexOf("!#",templateStart);
            if (endIndex == -1)
                throw new TagNotClosedException("closing tag !# not found");

            String repeatedTemp = template.substring(templateStart, endIndex);
            String lastPart = template.substring(endIndex+2);
            template.delete(startIndex,template.length());
            for (int i = 0, funcDefListLength = FuncDefList.length; i < funcDefListLength; i++) {
                LoadedServiceFunction loadedServiceFunction = FuncDefList[i];
                StringBuilder repeatedTemplate = new StringBuilder(repeatedTemp);
                StringBuilder def = loadedServiceFunction.GenerateFromTemplate(repeatedTemplate);
                template.append(def);
                if (i != funcDefListLength-1){
                    template.append(separator);
                }
            }
            template.append(lastPart);
            startIndex = template.indexOf("#!",template.length()-lastPart.length());
        }

        MiscUtils.replaceWithContext(template,globalContext);
        //System.out.println(template);
        return template;
    }

    public StringBuilder GenerateFromTemplate(StringBuilder template,String path) {
        GenerateFromTemplate(template);
        MiscUtils.writeToFile(new File(path),template);
        return template;
    }
}
