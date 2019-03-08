package MainTool;

import java.io.File;

public class GenerationConfig {
    private static GenerationConfig instance = new GenerationConfig();
    public String serviceInterfaceFile;

    public String serviceRouterTemplate;
    public String microServiceInterfaceTemplate;

    public String generationRootPath;
    public String routerNameSpace;
    public String microServiceNameSpace;
    public String jsTemplate;

    private GenerationConfig() {
        //no instance
    }

    public static GenerationConfig getInstance() {
        return instance;
    }

    public static void LoadFromFile(String cfgFn) {
        File file = new File(cfgFn);
        if (file.exists()){
            GenerationConfig tmp = MiscUtils.ReadJson(file, GenerationConfig.class);
            if (tmp != null)
            {
                GenerationConfig.instance = tmp;
                System.out.println("config load success from "+cfgFn);
                return;
            }
        }
        System.out.println("failed to load "+cfgFn);
    }

    public static void SaveConfig(String fn){
        MiscUtils.WriteJson(instance,fn);
    }

    public static void SaveConfig(){
        SaveConfig("InterfaceManagerConfig.json");
    }
}
