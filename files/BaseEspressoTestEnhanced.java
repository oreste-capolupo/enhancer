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
import com.fsck.k9.TOGGLETools;
import java.util.concurrent.FutureTask;
import android.support.test.runner.lifecycle.Stage.RESUMED;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

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
        FutureTask<Boolean> capture_task = null;
        boolean res_task = false;
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        UiDevice device = UiDevice.getInstance(instr);
        Date now = new Date();
        Activity activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "fab_expand_menu_button", "check");
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.fab_expand_menu_button)).check(matches(isDisplayed())).perform(click(), replaceText(method.call("/").toString()), pressImeActionButton());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "fab_expand_menu_button", "click");
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.fab_expand_menu_button)).check(matches(isDisplayed())).perform(click(), replaceText(method.call("/").toString()), pressImeActionButton());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        int textToBeReplacedLength23 = ((TextView) activityTOGGLETools.findViewById(R.id.fab_expand_menu_button)).getText().length();
        TOGGLETools.LogInteraction(now, "testTest", "id", "fab_expand_menu_button", "replacetext", String.valueOf(textToBeReplacedLength23) + ";" + method.call("/").toString());
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.fab_expand_menu_button)).check(matches(isDisplayed())).perform(click(), replaceText(method.call("/").toString()), pressImeActionButton());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "fab_expand_menu_button", "pressIme");
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.fab_expand_menu_button)).check(matches(isDisplayed())).perform(click(), replaceText(method.call("/").toString()), pressImeActionButton());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "fab_expand_menu_button", "longclick");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.fab_expand_menu_button)).perform(longClick());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "-", "-", "typeintofocused");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_title)).perform(typeTextIntoFocusedView("IntoFocused"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String espressoKeyVal56 = String.valueOf(KeyEvent.KEYCODE_P);
        String[] espressoKeyArray56 = espressoKeyVal56.split(",");
        if (espressoKeyArray56.length > 1) {
            int espressoKeyArrayIndex56 = espressoKeyArray56[0].indexOf(":");
            espressoKeyVal56 = espressoKeyArray56[0].substring(espressoKeyArrayIndex56 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_title", "presskey", espressoKeyVal56);
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_P));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String espressoKeyVal67 = String.valueOf(KeyEvent.KEYCODE_R);
        String[] espressoKeyArray67 = espressoKeyVal67.split(",");
        if (espressoKeyArray67.length > 1) {
            int espressoKeyArrayIndex67 = espressoKeyArray67[0].indexOf(":");
            espressoKeyVal67 = espressoKeyArray67[0].substring(espressoKeyArrayIndex67 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_title", "presskey", espressoKeyVal67);
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_R));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String espressoKeyVal78 = String.valueOf(KeyEvent.KEYCODE_E);
        String[] espressoKeyArray78 = espressoKeyVal78.split(",");
        if (espressoKeyArray78.length > 1) {
            int espressoKeyArrayIndex78 = espressoKeyArray78[0].indexOf(":");
            espressoKeyVal78 = espressoKeyArray78[0].substring(espressoKeyArrayIndex78 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_title", "presskey", espressoKeyVal78);
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_E));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String espressoKeyVal89 = String.valueOf(KeyEvent.KEYCODE_S);
        String[] espressoKeyArray89 = espressoKeyVal89.split(",");
        if (espressoKeyArray89.length > 1) {
            int espressoKeyArrayIndex89 = espressoKeyArray89[0].indexOf(":");
            espressoKeyVal89 = espressoKeyArray89[0].substring(espressoKeyArrayIndex89 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_title", "presskey", espressoKeyVal89);
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String espressoKeyVal100 = String.valueOf(KeyEvent.KEYCODE_S);
        String[] espressoKeyArray100 = espressoKeyVal100.split(",");
        if (espressoKeyArray100.length > 1) {
            int espressoKeyArrayIndex100 = espressoKeyArray100[0].indexOf(":");
            espressoKeyVal100 = espressoKeyArray100[0].substring(espressoKeyArrayIndex100 + 1).trim();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_title", "presskey", espressoKeyVal100);
        TOGGLETools.DumpScreen(now, device);
        onView(withId(R.id.detail_title)).perform(pressKey(KeyEvent.KEYCODE_S));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_content", "typetext", "content");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_content)).perform(typeText("content"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        int textToBeReplacedLength119 = ((TextView) activityTOGGLETools.findViewById(R.id.detail_content)).getText().length();
        TOGGLETools.LogInteraction(now, "testTest", "id", "detail_content", "replacetext", String.valueOf(textToBeReplacedLength119) + ";" + "ciao");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.detail_content)).perform(replaceText("ciao"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "content-desc", "drawer open", "click");
        TOGGLETools.DumpScreen(now, device);
        onView(withContentDescription("drawer open")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "text", "IntoFocusedpress", "check");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withText("IntoFocusedpress")).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "content-desc", "drawer open", "click");
        TOGGLETools.DumpScreen(now, device);
        onView(withContentDescription("drawer open")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "id", "settings_view", "click");
        TOGGLETools.DumpScreen(now, device);
        // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
        onView(withId(R.id.settings_view)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        now = new Date();
        activityTOGGLETools = getActivityInstance();
        capture_task = new FutureTask<Boolean>(new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));
        Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);
        try {
            runOnUiThread(capture_task);
            res_task = capture_task.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        TOGGLETools.LogInteraction(now, "testTest", "-", "-", "fullcheck", currdisp.bottom + ";" + currdisp.top + ";" + currdisp.right + ";" + currdisp.left);
        TOGGLETools.DumpScreen(now, device);
    // onView(withContentDescription(obj.getDesc(par).getNameById(pip, 2, "val").drawerDesc(pippo, "value"))).perform(click()).check(matches(isEnabled()));
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
