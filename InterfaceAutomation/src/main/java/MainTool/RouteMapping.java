package MainTool;

public class RouteMapping {
    public String CenterCode;
    public String ModuleName;

    public String ControllerName;
    public String ClientAccessUrl;
    //public String ImplementationClassName;

    public void NormalizeName() {
        CenterCode=CenterCode.trim();
        ModuleName=ModuleName.trim();
        ControllerName=ControllerName.trim();
        ClientAccessUrl=ClientAccessUrl.trim();
        //ImplementationClassName=ImplementationClassName.trim();
    }
}
