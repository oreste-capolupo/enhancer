
package it.enhancer.enhancer.withTextModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Expression {

    @SerializedName("scope")
    @Expose
    private Scope scope;
    @SerializedName("name")
    @Expose
    private Name__ name;
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

    public Name__ getName() {
        return name;
    }

    public void setName(Name__ name) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scope", scope).append("name", name).append("arguments", arguments).append("type", type).toString();
    }

}
