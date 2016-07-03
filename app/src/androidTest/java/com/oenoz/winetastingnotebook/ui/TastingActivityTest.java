package com.oenoz.winetastingnotebook.ui;

import android.support.design.widget.TabLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.AndroidTestCase;

import com.oenoz.winetastingnotebook.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TastingActivityTest extends AndroidTestCase {

    @Rule
    public ActivityTestRule<TastingActivity> mActivityRule = new ActivityTestRule<TastingActivity>(TastingActivity.class);

    @Test
    public void testNavigation() {
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar.class))
                .check(new ToolbarTitleViewAssertion("Appearance"));
        Espresso.onView(ViewMatchers.isAssignableFrom(TabLayout.class))
                .check(new TabLayoutViewAssertion(0, "Clarity", "Intensity", "Color", "> Nose"));

        Espresso.onView(ViewMatchers.withText("Intensity")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar.class))
                .check(new ToolbarTitleViewAssertion("Appearance"));
        Espresso.onView(ViewMatchers. isAssignableFrom(TabLayout.class))
                .check(new TabLayoutViewAssertion(1, "Clarity", "Intensity", "Color", "> Nose"));

        Espresso.onView(ViewMatchers.withText("Color")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar.class))
                .check(new ToolbarTitleViewAssertion("Appearance"));
        Espresso.onView(ViewMatchers. isAssignableFrom(TabLayout.class))
                .check(new TabLayoutViewAssertion(2, "Clarity", "Intensity", "Color", "> Nose"));

        Espresso.onView(ViewMatchers.withText("> Nose")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar.class))
                .check(new ToolbarTitleViewAssertion("Nose"));
        Espresso.onView(ViewMatchers. isAssignableFrom(TabLayout.class))
                .check(new TabLayoutViewAssertion(1, "Appearance <", "Condition", "Intensity", "Development", "Aromas", "> Palate"));
    }

    // TODO test save and restore state http://blog.sqisland.com/2015/10/espresso-save-and-restore-state.html
}
