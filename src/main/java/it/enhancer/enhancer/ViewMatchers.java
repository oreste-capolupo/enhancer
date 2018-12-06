package it.enhancer.enhancer;

public enum ViewMatchers {
	withContentDescription("content-desc"), withId("id"), withText("text");
	
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
		
//		int min = 0;
//        int max = vm.length  - 1;
//        int i;
//        do {
//            i = (min + max) / 2;
//            String val = ((ViewMatchers) vm[i]).name();
//            if (matcher.compareTo(val) < 0) {
//                max = i;
//            } else if (matcher.compareTo(val) > 0) {
//                if (i + 1 < vm.length && matcher.compareTo(((ViewMatchers) vm[i + 1]).getValue()) < 0) {
//                    break;
//                }
//                min = i + 1;
//            }
//        } while (!matcher.equals(((ViewMatchers)vm[i]).name()) && min < max);
//        if (min == max) {
//            return ((ViewMatchers)vm[max]).getValue();
//        }
//        return ((ViewMatchers)vm[i]).getValue();
        
      return "";
	}
}
