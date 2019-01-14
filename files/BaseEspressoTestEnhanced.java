/*
 * Copyright (C) 2018 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.feio.android.omninotes;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Rect;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.runner.screenshot.Screenshot;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;
import java.io.File;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;
import it.feio.android.omninotes.TOGGLETools;

public class BaseEspressoTestEnhanced extends BaseAndroidTestCase {

    private Activity currentActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mActivityTestRule.launchActivity(null);
    }

    @Test
    public void testTest() {
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        UiDevice device = UiDevice.getInstance(instr);
        Date now = new Date();
        Activity activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "fab_expand_menu_button", "longclick");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        ViewInteraction vi = onView(withId(R.id.fab_expand_menu_button)).perform(longClick(), longClick()).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "fab_expand_menu_button", "longclick");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        ViewInteraction vi = onView(withId(R.id.fab_expand_menu_button)).perform(longClick(), longClick()).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "fab_expand_menu_button", "check");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        ViewInteraction vi = onView(withId(R.id.fab_expand_menu_button)).perform(longClick(), longClick()).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "fab_expand_menu_button", "longclick");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.fab_expand_menu_button)).perform(longClick());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "detail_title", "typeintofocused", "IntoFocused");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(typeTextIntoFocusedView("IntoFocused"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        String espressoKeyVal38 = String.valueOf(KeyEvent.KEYCODE_P);
        String[] espressoKeyArray38 = espressoKeyVal38.split(",");
        if (espressoKeyArray38.length > 1) {
            int espressoKeyArrayIndex38 = espressoKeyArray38[0].indexOf(":");
            espressoKeyVal38 = espressoKeyArray38[0].substring(espressoKeyArrayIndex38 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "id", "detail_title", "presskey", espressoKeyVal38);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_P));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        String espressoKeyVal48 = String.valueOf(KeyEvent.KEYCODE_R);
        String[] espressoKeyArray48 = espressoKeyVal48.split(",");
        if (espressoKeyArray48.length > 1) {
            int espressoKeyArrayIndex48 = espressoKeyArray48[0].indexOf(":");
            espressoKeyVal48 = espressoKeyArray48[0].substring(espressoKeyArrayIndex48 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "id", "detail_title", "presskey", espressoKeyVal48);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_R));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        String espressoKeyVal58 = String.valueOf(KeyEvent.KEYCODE_E);
        String[] espressoKeyArray58 = espressoKeyVal58.split(",");
        if (espressoKeyArray58.length > 1) {
            int espressoKeyArrayIndex58 = espressoKeyArray58[0].indexOf(":");
            espressoKeyVal58 = espressoKeyArray58[0].substring(espressoKeyArrayIndex58 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "id", "detail_title", "presskey", espressoKeyVal58);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_E));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        String espressoKeyVal68 = String.valueOf(KeyEvent.KEYCODE_S);
        String[] espressoKeyArray68 = espressoKeyVal68.split(",");
        if (espressoKeyArray68.length > 1) {
            int espressoKeyArrayIndex68 = espressoKeyArray68[0].indexOf(":");
            espressoKeyVal68 = espressoKeyArray68[0].substring(espressoKeyArrayIndex68 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "id", "detail_title", "presskey", espressoKeyVal68);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        String espressoKeyVal78 = String.valueOf(KeyEvent.KEYCODE_S);
        String[] espressoKeyArray78 = espressoKeyVal78.split(",");
        if (espressoKeyArray78.length > 1) {
            int espressoKeyArrayIndex78 = espressoKeyArray78[0].indexOf(":");
            espressoKeyVal78 = espressoKeyArray78[0].substring(espressoKeyArrayIndex78 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "id", "detail_title", "presskey", espressoKeyVal78);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "detail_content", "typetext", "content");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_content)).perform(typeText("content"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        int textToBeReplacedLength95 = ((TextView) activityTOGGLETools.findViewById(R.id.detail_content)).getText().length();
        TOGGLETools.LogInteraction(now, "id", "detail_content", "replacetext", String.valueOf(textToBeReplacedLength95) + ";" + "ciao");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_content)).perform(replaceText("ciao"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "content-desc", "drawer open", "click");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withContentDescription("drawer open")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "text", "IntoFocusedpress", "check");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withText("IntoFocusedpress")).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "content-desc", "drawer open", "click");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withContentDescription("drawer open")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "settings_view", "click");
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.settings_view)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);
        TOGGLETools.LogInteraction(now, "-", "-", "fullcheck", currdisp.bottom + ";" + currdisp.top + ";" + currdisp.right + ";" + currdisp.left);
        TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);
        TOGGLETools.DumpScreen(now, device);
    // fullcheck
    /*Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        UiDevice device = UiDevice.getInstance(instr);








        Date now = new Date();
        Activity activity = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "fab_expand_menu_button", "longclick");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);

        onView(withId(R.id.fab_expand_menu_button)).perform(longClick());

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }



        now = new Date();
        activity = getActivityInstance();

        TOGGLETools.LogInteraction(now, "-", "-", "typeintofocused",  "IntoFocused");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);


        onView(withId(R.id.detail_title)).perform(typeTextIntoFocusedView("IntoFocused"));

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        now = new Date();
        activity = getActivityInstance();

        TOGGLETools.LogInteraction(now, "-", "-", "presskey",  String.valueOf(KeyEvent.KEYCODE_C));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_C));

        TOGGLETools.LogInteraction(now, "-", "-", "presskey",  String.valueOf(KeyEvent.KEYCODE_SPACE));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_SPACE));

        TOGGLETools.LogInteraction(now, "-", "-", "presskey",  String.valueOf(KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN));


        EspressoKey.Builder eb = new EspressoKey.Builder();
        eb.withKeyCode(KeyEvent.KEYCODE_C);
        EspressoKey ek = eb.build();

        TOGGLETools.LogInteraction(now, "-", "-", "presskey",  String.valueOf(ek.getKeyCode()));
        onView(withId(R.id.detail_title)).perform(pressKey(ek));


        now = new Date();
        activity = getActivityInstance();

        int texttobereplacedlength = ((TextView) activity.findViewById(R.id.detail_title)).getText().length();
        TOGGLETools.LogInteraction(now, "id", "detail_title", "replacetext", String.valueOf(texttobereplacedlength) + ";" + "Replacement");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);

        onView(withId(R.id.detail_title)).perform(replaceText("Replacement"));

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }






        now = new Date();
        activity = getActivityInstance();
        TOGGLETools.LogInteraction(now, "content-desc", "drawer open", "click");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);

        onView(withContentDescription("drawer open")).perform(click());


        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        now = new Date();
        activity = getActivityInstance();
        TOGGLETools.LogInteraction(now, "content-desc", "drawer open", "click");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);

        onView(withContentDescription("drawer open")).perform(click());

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        now = new Date();
        activity = getActivityInstance();
        TOGGLETools.LogInteraction(now, "id", "settings_view", "check");
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);


        onView(withId(R.id.settings_view)).check(matches(isDisplayed()));



        //check dell'intero schermo
        now = new Date();
        TOGGLETools.TakeScreenCapture(now, activity);
        TOGGLETools.DumpScreen(now, device);
        Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activity);
        TOGGLETools.LogInteraction(now, "-", "-", "fullcheck", currdisp.bottom+";"+currdisp.top+";"+currdisp.right+";"+currdisp.left);
        //Log.d("touchtest", now.getTime() + ", " + "fullcheck");
        //se trovo fullcheck non devo ritagliare ma soltanto aggiungere check di tutto lo schermo
*/
    }

    // for clicking exact coordinates (identify a parent view, then perform clickXY
    public static ViewAction clickXY(final int x, final int y) {
        return new GeneralClickAction(Tap.SINGLE, new CoordinatesProvider() {

            @Override
            public float[] calculateCoordinates(View view) {
                final int[] screenPos = new int[2];
                view.getLocationOnScreen(screenPos);
                final float screenX = screenPos[0] + x;
                final float screenY = screenPos[1] + y;
                float[] coordinates = { screenX, screenY };
                return coordinates;
            }
        }, Press.FINGER);
    }

    protected static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public Activity getActivityInstance() {
        getInstrumentation().runOnMainSync(new Runnable() {

            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });
        return currentActivity;
    }
}
