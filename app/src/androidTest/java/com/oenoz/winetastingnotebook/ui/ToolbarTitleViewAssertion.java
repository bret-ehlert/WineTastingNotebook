package com.oenoz.winetastingnotebook.ui;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.Toolbar;
import android.view.View;

import junit.framework.Assert;

public class ToolbarTitleViewAssertion implements ViewAssertion {
    private final String mExpectedText;

    public ToolbarTitleViewAssertion(String expectedText) {
        mExpectedText = expectedText;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        Assert.assertEquals(mExpectedText, ((Toolbar)view).getTitle());
    }
}
