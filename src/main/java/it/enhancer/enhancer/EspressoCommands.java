package it.enhancer.enhancer;

public class EspressoCommands {
	private static String[] commands = { "actionWithAssertions", "addGlobalAssertion", "allOf", "anyOf", "assertThat",
			"clearGlobalAssertions", "clearText", "click", "closeSoftKeyboard", "doesNotExist", "doubleClick",
			"endsWith", "hasBackground", "hasChildCount", "hasContentDescription", "hasDescendant", "hasEllipsizedText",
			"hasErrorText", "hasFocus", "hasIMEAction", "hasLinks", "hasMinimumChildCount", "hasMultilineTest",
			"hasSibling", "hasTextColor", "hasWindowLayoutParams", "instanceOf", "is", "isAbove", "isAssignableFrom",
			"isBelow", "isBottomAlignedWith", "isChecked", "isClickable", "isCompletelyAbove", "isCompletelyBelow",
			"isCompletelyDisplayed", "isCompletelyLeftOf", "isCompletelyRightOf", "isDescendantOfA", "isDialog",
			"isDisplayed", "isDisplayingAtLeast", "isEnabled", "isFocusable", "isJavascriptEnabled",
			"isLeftAlignedWith", "isLeftOf", "isNotChecked", "isPartiallyAbove", "isPartiallyBelow",
			"isPartiallyLeftOf", "isPartiallyRightOf", "isPlatformPopup", "isRightAlignedWith", "isRightOf", "isRoot",
			"isSelected", "isSystemAlertWindow", "isTopAlignedWith", "isTouchable", "longClick", "matches",
			"noEllipseizedText", "noMultilineButtons", "noOverlaps", "not", "openLink", "openLinkWithText",
			"openLinkWithUri", "pressBack", "pressBackUnconditionally", "pressIMEActionButton", "pressKey",
			"pressMenuKey", "removeGlobalAssertion", "repeatedlyUntil", "replaceText", "scrollTo",
			"selectedDescendantsMatch", "startsWith", "supportsInputMethods", "swipeDown", "swipeLeft", "swipeRight",
			"swipeUp", "typeText", "typeTextIntoFocusedView", "withAlpha", "withChild", "withClassName",
			"withContentDescription", "withDecorView", "withEffectiveVisibility", "withHint", "withId", "withInputType",
			"withKey", "withParent", "withParentIndex", "withResourceName", "withRowBlob", "withRowDouble",
			"withRowFloat", "withRowInt", "withRowLong", "withRowShort", "withRowString", "withSpinnerText",
			"withSubstring", "withSummary", "withSummaryText", "withTagKey", "withTagValue", "withText", "withTitle",
			"withTitleText" };

	public static String[] getCommands() {
		return commands;
	}

}
