
package it.enhancer.enhancer.withTextModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Argument__ {

	@SerializedName("name")
	@Expose
	private Name___ name;
	@SerializedName("type")
	@Expose
	private String type;

	public Name___ getName() {
		return name;
	}

	public void setName(Name___ name) {
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
		return new ToStringBuilder(this).append("name", name).append("type", type).toString();
	}

}
