package typemodel;

public class RecordTypeDecl extends TypeDecl {
    public String typName;

    @Override
    public void NormalizeName() {
        super.NormalizeName();
    }

    @Override
    public String GetTypeName() {
        return typName;
    }
}
