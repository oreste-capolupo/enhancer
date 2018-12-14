package it.enhancer.enhancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Statistic {
	public static Map<String, Integer> populateInitialMap() {
		Map<String, Integer> statistic = new HashMap<String,Integer>();
		
		statistic.put("allOf", 0);
		statistic.put("anyOf", 0);
		statistic.put("assertThat", 0);
		statistic.put("endsWith", 0);
		statistic.put("hasBackground", 0);
		statistic.put("hasChildCount", 0);
		statistic.put("hasContentDescription", 0);
		statistic.put("hasDescendant", 0);
		statistic.put("hasEllipsizedText", 0);
		statistic.put("hasErrorText", 0);
		statistic.put("hasFocus", 0);
		statistic.put("hasIMEAction", 0);
		statistic.put("hasLinks", 0);
		statistic.put("hasMinimumChildCount", 0);
		statistic.put("hasMultilineTest", 0);
		statistic.put("hasSibling", 0);
		statistic.put("hasTextColor", 0);
		statistic.put("hasWindowLayoutParams", 0);
		statistic.put("instanceOf", 0);
		statistic.put("is", 0);
		statistic.put("isAssignableFrom", 0);
		statistic.put("isChecked", 0);
		statistic.put("isClickable", 0);
		statistic.put("isCompletelyDisplayed", 0);
		statistic.put("isDescendantOfA", 0);
		statistic.put("isDialog", 0);
		statistic.put("isDisplayed", 0);
		statistic.put("isDisplayingAtLeast", 0);
		statistic.put("isEnabled", 0);
		statistic.put("isFocusable", 0);
		statistic.put("isJavascriptEnabled", 0);
		statistic.put("isNotChecked", 0);
		statistic.put("isPlatformPopup", 0);
		statistic.put("isRoot", 0);
		statistic.put("isSelected", 0);
		statistic.put("isSystemAlertWindow", 0);
		statistic.put("isTouchable", 0);
		statistic.put("not", 0);
		statistic.put("startsWith", 0);
		statistic.put("supportsInputMethods", 0);
		statistic.put("withAlpha", 0);
		statistic.put("withChild", 0);
		statistic.put("withClassName", 0);
		statistic.put("withContentDescription", 0);
		statistic.put("withDecorView", 0);
		statistic.put("withEffectiveVisibility", 0);
		statistic.put("withHint", 0);
		statistic.put("withId", 0);
		statistic.put("withInputType", 0);
		statistic.put("withKey", 0);
		statistic.put("withParent", 0);
		statistic.put("withParentIndex", 0);
		statistic.put("withResourceName", 0);
		statistic.put("withRowBlob", 0);
		statistic.put("withRowDouble", 0);
		statistic.put("withRowFloat", 0);
		statistic.put("withRowInt", 0);
		statistic.put("withRowLong", 0);
		statistic.put("withRowShort", 0);
		statistic.put("withRowString", 0);
		statistic.put("withSpinnerText", 0);
		statistic.put("withSummary", 0);
		statistic.put("withSummaryText", 0);
		statistic.put("withSubstring", 0);
		statistic.put("withTagKey", 0);
		statistic.put("withTagValue", 0);
		statistic.put("withText", 0);
		statistic.put("withTitle", 0);
		statistic.put("withTitleText", 0);

		statistic.put("actionWithAssertions", 0);
		statistic.put("addGlobalAssertion", 0);
		statistic.put("clearGlobalAssertions", 0);
		statistic.put("clearText", 0);
		statistic.put("click", 0);
		statistic.put("closeSoftKeyboard", 0);
		statistic.put("doubleClick", 0);
		statistic.put("longClick", 0);
		statistic.put("openLink", 0);
		statistic.put("openLinkWithText", 0);
		statistic.put("openLinkWithUri", 0);
		statistic.put("pressBack", 0);
		statistic.put("pressBackUnconditionally", 0);
		statistic.put("pressIMEActionButton", 0);
		statistic.put("pressKey", 0);
		statistic.put("pressMenuKey", 0);
		statistic.put("removeGlobalAssertion", 0);
		statistic.put("repeatedlyUntil", 0);
		statistic.put("replaceText", 0);
		statistic.put("scrollTo", 0);
		statistic.put("swipeDown", 0);
		statistic.put("swipeLeft", 0);
		statistic.put("swipeRight", 0);
		statistic.put("swipeUp", 0);
		statistic.put("typeText", 0);
		statistic.put("typeTextIntoFocusedView", 0);

		statistic.put("doesNotExist", 0);
		statistic.put("isAbove", 0);
		statistic.put("isBelow", 0);
		statistic.put("isBottomAlignedWith", 0);
		statistic.put("isCompletelyAbove", 0);
		statistic.put("isCompletelyBelow", 0);
		statistic.put("isCompletelyLeftOf", 0);
		statistic.put("isCompletelyRightOf", 0);
		statistic.put("isLeftAlignedWith", 0);
		statistic.put("isLeftOf", 0);
		statistic.put("isPartiallyAbove", 0);
		statistic.put("isPartiallyBelow", 0);
		statistic.put("isPartiallyLeftOf", 0);
		statistic.put("isPartiallyRightOf", 0);
		statistic.put("isRightAlignedWith", 0);
		statistic.put("isRightOf", 0);
		statistic.put("isTopAlignedWith", 0);
		statistic.put("matches", 0);
		statistic.put("noEllipseizedText", 0);
		statistic.put("noMultilineButtons", 0);
		statistic.put("noOverlaps", 0);
		statistic.put("selectedDescendantsMatch", 0);
		
		statistic.put("inAdapterView", 0);
		statistic.put("atPosition", 0);
		statistic.put("onChildView", 0);
		
		return statistic;
	}

	public static void writeDataToFile(Map<String, Integer> statistic, String statisticFilePath) {
		BufferedWriter bw;
		LinkedHashMap<String, Integer> sorted = (LinkedHashMap<String, Integer>) sortByValue(statistic);

		try {
			bw = new BufferedWriter(new FileWriter(statisticFilePath));
			for (Entry<String, Integer> entry : sorted.entrySet()) {
				bw.write(entry.getKey() + ":" + entry.getValue());
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, Integer> readDataFromFile(String statisticFilePath) {
		Map<String, Integer> statistic = new HashMap<String, Integer>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(statisticFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String key = "";
				Integer value = 0;

				String[] splitted = line.split(":");
				key = splitted[0];
				value = Integer.valueOf(splitted[1]);
				statistic.put(key, value);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return statistic;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
