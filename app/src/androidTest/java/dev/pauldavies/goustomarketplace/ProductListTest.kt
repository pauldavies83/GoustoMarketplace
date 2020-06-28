package dev.pauldavies.goustomarketplace

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class ProductListTest: MockWebServerTestCase() {

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

        onView(withId(R.id.productListNoResultsLabel))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.productListRecyclerView))
            .check(matches(isDisplayed()))
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
        onView(withId(R.id.productListNoResultsLabel))
            .check(matches(isDisplayed()))

        onView(withId(R.id.productListRecyclerView))
            .check(matches(not(isDisplayed())))
    }
}