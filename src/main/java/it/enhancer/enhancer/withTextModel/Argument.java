
package it.enhancer.enhancer.withTextModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Argument {

    @SerializedName("name")
    @Expose
    private Name_ name;
    @SerializedName("arguments")
    @Expose
    private List<Argument_> arguments = null;
    @SerializedName("type")
    @Expose
    private String type;

    public Name_ getName() {
        return name;
    }

    public void setName(Name_ name) {
        this.name = name;
    }

    public List<Argument_> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument_> arguments) {
        this.arguments = arguments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
