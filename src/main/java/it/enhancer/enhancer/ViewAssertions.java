package it.enhancer.enhancer;

public enum ViewAssertions {
	matches("matches"), isLeftOf("isleftof"), isRightOf("isrightof"), isLeftAlignedWith("isleftalignedwith"),
	isRightAlignedWith("isrightalignedwith"), isAbove("isabove"), isBelow("isbelow"), isBottomAlignedWith("isbottomalignedwith"),
	isTopAlignedWith("istopalignedwith");

	private String value;

	ViewAssertions(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static String getSearchType(String matcher) {
		Object[] va = ViewAssertions.values();

		for (Object o : va)
			if (((ViewAssertions) o).name().equals(matcher))
				return ((ViewAssertions) o).getValue();

		return "";
	}
}
