
package it.enhancer.enhancer.withTextModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithTextModel {

    @SerializedName("expression")
    @Expose
    private Expression expression;
    @SerializedName("type")
    @Expose
    private String type;

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
