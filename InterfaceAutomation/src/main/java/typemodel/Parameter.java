package typemodel;

import java.util.List;

public class Parameter {
    public String CustomDependency;
    public VarDecl decl;
    public List<String> Examples;

    public void NormalizeName() {
        decl.Normalize();
        if (CustomDependency != null)
            CustomDependency = CustomDependency.trim();
    }
}
