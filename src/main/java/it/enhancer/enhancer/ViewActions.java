package it.enhancer.enhancer;

public enum ViewActions {
	check("check"), clearText("cleartext"), click("click"), closeSoftKeyboard("closekeyboard"), doubleClick("doubleclick"), longClick("longclick"), pressBack("back"), 
	pressBackUnconditionally("backunconditionally"), pressImeActionButton("pressIme"), pressKey("presskey"), pressMenuKey("pressmenukey"), replaceText("replacetext"), 
	scrollTo("scrollto"), swipeDown("swipedown"), swipeLeft("swipeLeft"), swipeRight("swiperight"), swipeUp("swipeup"), typeText("typetext"), 
	typeTextIntoFocusedView("typetextintofocusedview");
	
	private String value;
	
	ViewActions(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String getSearchType(String matcher) {
		Object[] va = ViewActions.values();

		for (Object o : va)
			if(((ViewActions)o).name().equals(matcher))
				return ((ViewActions) o).getValue();
		
		return "";
	}
	
}