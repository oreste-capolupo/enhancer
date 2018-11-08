
package it.enhancer.enhancer.withIdModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expression {

    @SerializedName("scope")
    @Expose
    private Scope scope;
    @SerializedName("name")
    @Expose
    private Name_____ name;
    @SerializedName("arguments")
    @Expose
    private List<Argument__> arguments = null;
    @SerializedName("type")
    @Expose
    private String type;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Name_____ getName() {
        return name;
    }

    public void setName(Name_____ name) {
        this.name = name;
    }

    public List<Argument__> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument__> arguments) {
        this.arguments = arguments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
