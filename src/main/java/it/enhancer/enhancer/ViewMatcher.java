package it.enhancer.enhancer;

public enum ViewMatcher {
	withId("id"), withText("text"), withContentDescription("content-desc");
	
	private String value;
	
	ViewMatcher(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String getOperation(String matcher) {
		Object[] vm = ViewMatcher.values();

		for (Object o : vm)
			if(((ViewMatcher)o).name().equals(matcher))
				return ((ViewMatcher) o).getValue();
		return "";
	}
}
