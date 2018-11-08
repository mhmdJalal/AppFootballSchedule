package com.mahlultech.footballclubs3

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        const val RECYCLER_VIEW = R.id.recyclerView
        const val RECYCLER_VIEW_LAST = R.id.recyclerViewLast
        const val BOTTOM_NAVIGATION_VIEW = R.id.bottom_navigation
        const val ADD_FAVORITE = R.id.add_to_favorite
        const val MATCH = R.id.matchs
        const val TEAM = R.id.teams
        const val FAVORITE = R.id.favorite
        const val VIEW_PAGER = R.id.viewPager
        const val SPINNER = R.id.spinner
        const val DESC_TEAM = R.id.descTeam
        const val RECYCLER_FAVORITE_MATCH = R.id.recyclerFavMatch
    }

    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testAppBehaviour() {
        onView(withId(BOTTOM_NAVIGATION_VIEW)).check(matches(isDisplayed()))
        onView(withId(MATCH)).perform(click()).perform()
        onView(withId(VIEW_PAGER)).check(matches(isDisplayed()))

        onView(allOf(withId(RECYCLER_VIEW), isDisplayed()))
        Thread.sleep(5000)
        onView(allOf(withId(RECYCLER_VIEW))).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Thread.sleep(2000)
        onView(allOf(withId(RECYCLER_VIEW))).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(ADD_FAVORITE)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(withId(ADD_FAVORITE)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))
        Thread.sleep(2000)
        pressBack()

        onView(withId(VIEW_PAGER)).perform(swipeLeft())
        onView(withId(RECYCLER_VIEW_LAST)).check(matches(isDisplayed()))

        Thread.sleep(3000)
        onView(withId(RECYCLER_VIEW_LAST)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(withId(RECYCLER_VIEW_LAST)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click())
        )

        onView(withId(ADD_FAVORITE)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(withId(ADD_FAVORITE)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))
        Thread.sleep(2000)
        pressBack()

        onView(withId(TEAM)).perform(click())

        onView(withId(SPINNER)).check(matches(isDisplayed()))
        onView(withId(SPINNER)).perform(click())
        Thread.sleep(5000)
        onView(withText("Spanish La Liga")).perform(click())
        Thread.sleep(5000)
        onView(withText("Barcelona")).check(matches(isDisplayed()))
        onView(withText("Barcelona")).perform(click())

        onView(withId(VIEW_PAGER)).check(matches(isDisplayed()))
        onView(allOf(withId(DESC_TEAM), isDisplayed()))
        Thread.sleep(2000)

        onView(withId(ADD_FAVORITE)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(withId(ADD_FAVORITE)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))
        Thread.sleep(2000)
        pressBack()

        Thread.sleep(2000)
        onView(withId(FAVORITE)).perform(click())
        onView(allOf(withId(RECYCLER_FAVORITE_MATCH), isDisplayed()))
        Thread.sleep(2000)
        onView(allOf(withId(RECYCLER_FAVORITE_MATCH))).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(ADD_FAVORITE)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(withId(ADD_FAVORITE)).perform(click())
        onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        Thread.sleep(2000)
        pressBack()

        onView(withId(VIEW_PAGER)).check(matches(isDisplayed()))
        onView(withId(VIEW_PAGER)).perform(swipeLeft())
        onView(withId(RECYCLER_VIEW)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(RECYCLER_VIEW)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(ADD_FAVORITE)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(withId(ADD_FAVORITE)).perform(click())
        onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}