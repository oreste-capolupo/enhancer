
package it.enhancer.enhancer.withIdModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Argument_ {

    @SerializedName("scope")
    @Expose
    private Scope_ scope;
    @SerializedName("name")
    @Expose
    private Name____ name;
    @SerializedName("type")
    @Expose
    private String type;

    public Scope_ getScope() {
        return scope;
    }

    public void setScope(Scope_ scope) {
        this.scope = scope;
    }

    public Name____ getName() {
        return name;
    }

    public void setName(Name____ name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scope", scope).append("name", name).append("type", type).toString();
    }

}
