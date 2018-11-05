
package it.enhancer.enhancer.withIdModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Name____ {

    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("type")
    @Expose
    private String type;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("identifier", identifier).append("type", type).toString();
    }

}
