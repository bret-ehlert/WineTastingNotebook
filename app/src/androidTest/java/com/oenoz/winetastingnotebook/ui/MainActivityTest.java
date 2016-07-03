package com.oenoz.winetastingnotebook.ui;

import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.oenoz.winetastingnotebook.MainActivity;
import com.oenoz.winetastingnotebook.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends AndroidTestCase {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Test
    public void testNewTasting() {

        Espresso.onView(ViewMatchers.withId(R.id.newTastingButton))
                .perform(ViewActions.click());

        //Espresso.onView(ViewMatchers.withId(R.id.main_content))
         //       .check(ViewAssertions.matches(ViewMatchers.withText("TastingActivity")));

        Intents.intended(IntentMatchers.hasComponent(new ComponentName(getContext(), TastingActivity.class)));

        //Espresso.onView(ViewMatchers.withId(R.id.tastingViewPager)).perform(ViewActions.swipeRight())
        // Type text and then press the button.
         //Espresso.onView(ViewMatchers.withId(R.id.test))
         //        .perform(ViewActions.clearText(), ViewActions.typeText("Testing"), ViewActions.closeSoftKeyboard())
         //        .check(ViewAssertions.matches(ViewMatchers.withText("Testing")));
        //onView(withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
        //onView(withId(R.id.test))
        //        .check(ViewAssertions.matches(ViewMatchers.withText(("Testing"))));
    }
}