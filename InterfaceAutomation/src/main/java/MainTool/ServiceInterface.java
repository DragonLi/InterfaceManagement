package MainTool;

public class ServiceInterface {
    public String VersionNum;
    //use as micro service name in service stub implementation
    public RouteMapping RouteInfo;
    public ServiceFunction[] FunctionList;

    public void NormalizeName(){
        RouteInfo.NormalizeName();
        if (FunctionList == null)
            FunctionList=MiscUtils.EmptyServiceFunctionArray();
        for (int i = 0, functionListLength = FunctionList.length; i < functionListLength; i++) {
            ServiceFunction func = FunctionList[i];
            func.NormalizeName();
        }
    }
}
