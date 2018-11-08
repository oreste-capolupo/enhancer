
package it.enhancer.enhancer.withIdModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}
