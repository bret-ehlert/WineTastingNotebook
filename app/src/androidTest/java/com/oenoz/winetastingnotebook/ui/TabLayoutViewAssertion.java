package com.oenoz.winetastingnotebook.ui;

import android.support.design.widget.TabLayout;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.test.MoreAsserts;
import android.view.View;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutViewAssertion implements ViewAssertion {
    private final int mExpectedSelectedTabIdex;
    private final String[] mExpectedTabs;

    public TabLayoutViewAssertion(int expectedSelectedTabIndex, String... expectedTabs) {
        mExpectedSelectedTabIdex = expectedSelectedTabIndex;
        mExpectedTabs = expectedTabs;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        TabLayout tabLayout = (TabLayout)view;
        List<String> tabNames = new ArrayList<>();
        for(int i = 0; i < tabLayout.getTabCount(); i++) {
            tabNames.add(tabLayout.getTabAt(i) .getText().toString());
        }
        Assert.assertTrue("Expected tab " + mExpectedSelectedTabIdex + " to be selected", tabLayout.getTabAt(mExpectedSelectedTabIdex).isSelected());
        MoreAsserts.assertContentsInOrder(tabNames, mExpectedTabs);
    }
}
