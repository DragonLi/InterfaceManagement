package typemodel;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Must register subclass,see MiscUtil static constructor
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "typeTag")
public abstract class TypeDecl implements Cloneable {
    public String Description;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void NormalizeName() {
    }

    public abstract String GetTypeName();

    public String ResolveName(){
        return GetTypeName();
    }
}
