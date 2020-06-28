package dev.pauldavies.goustomarketplace

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.pauldavies.goustomarketplace.di.NetworkModule
import dev.pauldavies.goustomarketplace.util.FourOhFourDispatcher
import dev.pauldavies.goustomarketplace.util.MockWebServerTestCase
import dev.pauldavies.goustomarketplace.util.ProductListDispatcher
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class ProductListTest : MockWebServerTestCase() {

    @Module
    @InstallIn(ApplicationComponent::class)
    class TestNetworkModule : NetworkModule() {
        override fun baseUrl() = "http://localhost:8080/"
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testRule = ActivityTestRule(
        MainActivity::class.java, false, false
    )

    @Test
    fun listOfResultsShouldBeDisplayedWhenRetrieved() {
        testRule.launchActivity(null)

        assertNoResultsShown(false)
        assertProductListShown(true)
    }

    @Test
    fun searchForProductByTitleKeepsProductVisibleInList() {
        testRule.launchActivity(null)

        performSearch("Love Shortie")

        assertNoResultsShown(false)
        assertProductListShown(true)

        onView(withText("Love Shortie All Butter Shortbread"))
            .check(matches(isDisplayed()))
        onView(withText("Borsao Macabeo"))
            .check(doesNotExist())
    }

    @Test
    fun searchForProductByTitleFindsNoResultsShowsNoResultsState() {
        testRule.launchActivity(null)

        performSearch("This product doesn't exist")
        assertNoResultsState()
    }

    @Test
    fun noResultsLabelShouldBeDisplayedWhenNoResultsRetrieved() {
        mockWebServer.dispatcher = ProductListDispatcher("valid_empty_product_list_response.json")
        testRule.launchActivity(null)

        assertNoResultsState()
    }

    @Test
    fun noResultsLabelShouldBeDisplayedWhenNoResultsRetrievedAndUnableToSync() {
        mockWebServer.dispatcher = FourOhFourDispatcher
        testRule.launchActivity(null)

        assertNoResultsState()
    }

    private fun assertNoResultsState() {
        assertNoResultsShown(true)
        assertProductListShown(false)
    }

    private fun assertProductListShown(shown: Boolean) {
        if (shown) {
            onView(withId(R.id.productListRecyclerView))
                .check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.productListRecyclerView))
                .check(matches(not(isDisplayed())))
        }
    }

    private fun assertNoResultsShown(shown: Boolean) {
        viewShown(R.id.productListNoResultsLabel, shown)
    }

    private fun performSearch(queryText: String) {
        onView(withId(R.id.productListSearch))
            .perform(click())

        onView(withId(R.id.search_src_text))
            .perform(typeText(queryText))
    }
}

internal fun viewShown(viewId: Int, shown: Boolean): ViewInteraction? {
    return if (shown) {
        onView(withId(viewId))
            .check(matches(isDisplayed()))
    } else {
        onView(withId(viewId))
            .check(matches(not(isDisplayed())))
    }
}