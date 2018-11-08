package it.enhancer.enhancer;

public enum ViewMatchers {
	withId("id"), withText("text"), withContentDescription("content-desc");
	
	private String value;
	
	ViewMatchers(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String getSearchType(String matcher) {
		Object[] vm = ViewMatchers.values();

		for (Object o : vm)
			if(((ViewMatchers)o).name().equals(matcher))
				return ((ViewMatchers) o).getValue();
		return "";
	}
}
