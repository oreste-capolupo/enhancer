
package it.enhancer.enhancer.withIdModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Scope {

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("arguments")
    @Expose
    private List<Argument> arguments = null;
    @SerializedName("type")
    @Expose
    private String type;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
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
        return new ToStringBuilder(this).append("name", name).append("arguments", arguments).append("type", type).toString();
    }

}