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


public class BaseEspressoTest extends BaseAndroidTestCase {

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

        onView(withId(R.id.fab_expand_menu_button)).check(matches(isDisplayed())).perform(click(), replaceText(method.call("/").toString()), pressImeActionButton());

        //onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));

        onView(withId(R.id.fab_expand_menu_button)).perform(longClick());


        onView(withId(R.id.detail_title)).perform(typeTextIntoFocusedView("IntoFocused"));

        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_P));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_R));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_E));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));

        onView(withId(R.id.detail_content)).perform(typeText("content"));

        onView(withId(R.id.detail_content)).perform(replaceText("ciao"));

        onView(withContentDescription("drawer open")).perform(click());

        onView(withText("IntoFocusedpress")).check(matches(isDisplayed()));

        onView(withContentDescription("drawer open")).perform(click());

        onView(withId(R.id.settings_view)).perform(click());

        //fullcheck


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



    //for clicking exact coordinates (identify a parent view, then perform clickXY
    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};
                        return coordinates;
                    }
                },
                Press.FINGER);
    }



    protected static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }


            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public Activity getActivityInstance(){

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity;
    }

}
