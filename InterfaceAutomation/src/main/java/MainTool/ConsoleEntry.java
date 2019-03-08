package MainTool;

public class ConsoleEntry {
    public static void main(String[] args) {
        if (args != null){
            for (int i = 0; i < args.length; i++) {
                String arg = args[i].trim();
                if ("-runwithconfig".equalsIgnoreCase(arg)){
                    if (i +1<args.length)
                    {
                        String cfgFn = args[i+1].trim();
                        GenerationConfig.LoadFromFile(cfgFn);
                        if (GenerateWithConfig()){
                            return;
                        }
                    }
                }
            }
        }

        ShowGUI();
    }

    private static void ShowGUI() {
    }

    private static boolean GenerateWithConfig() {
        GenerationConfig cfg = GenerationConfig.getInstance();
        LoadedServiceInterface loader = LoadedServiceInterface.Load(cfg.serviceInterfaceFile);

        loader.GenerateJavaDataDefinition(cfg.generationRootPath);

        StringBuilder template = MiscUtils.readAllContent(cfg.serviceRouterTemplate);
        loader.GenerateFromTemplate(template,
                cfg.generationRootPath
                        +"/"+cfg.routerNameSpace.replace('.','/')
                        +"/"+loader.definition.RouteInfo.ControllerName
                        +".java");

        template = MiscUtils.readAllContent(cfg.microServiceInterfaceTemplate);
        loader.GenerateFromTemplate(template,
                cfg.generationRootPath
                        +"/"+cfg.microServiceNameSpace.replace('.','/')
                        +"/" +loader.definition.RouteInfo.ModuleName
                        +".java");

        template = MiscUtils.readAllContent(cfg.jsTemplate);
        loader.GenerateFromTemplate(template,
                cfg.generationRootPath
                        +"/js/"+loader.definition.RouteInfo.ControllerName
                        +"Request.js");
        return true;
    }
}
