
package it.enhancer.enhancer.withIdModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WithIdModel {

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("expression", expression).append("type", type).toString();
    }

}
