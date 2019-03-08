package MainTool;

import typemodel.Parameter;

public class ServiceFunction {
    public String Name;
    public String Description;
    public String Address;
    public Parameter Input;
    public Parameter Output;
    public String BackedFunctionName;
    public TestCaseList TestCases;

    public void NormalizeName(){
        Name = Name.trim();
        Address=Address.trim();
        Input.NormalizeName();
        Output.NormalizeName();
        BackedFunctionName = BackedFunctionName.trim();
    }
}
