package dev.pauldavies.goustomarketplace

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.pauldavies.goustomarketplace.di.NetworkModule
import dev.pauldavies.goustomarketplace.util.MockWebServerTestCase
import dev.pauldavies.goustomarketplace.view.details.ProductDetailsFragment
import dev.pauldavies.goustomarketplace.view.list.ProductListFragment
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class NavigationTests : MockWebServerTestCase() {

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
    fun clickOnProductShowsProductDetails() {
        testRule.launchActivity(null)

        clickShortbreadProduct()

        assertTrue(fragmentAtTopOfBackStack() is ProductDetailsFragment)
        assertShortbreadProductShown()
    }

    @Test
    fun clickBackOnProductDetailsShowsProductList() {
        testRule.launchActivity(null)

        clickShortbreadProduct()
        pressBack()

        assertTrue(fragmentAtTopOfBackStack() is ProductListFragment)
        assertShortbreadProductShown()
    }

    private fun assertShortbreadProductShown(): ViewInteraction {
        return onView(withText("Love Shortie All Butter Shortbread"))
            .check(matches(isDisplayed()))
    }

    private fun clickShortbreadProduct() {
        assertShortbreadProductShown().perform(click())
    }

    private fun fragmentAtTopOfBackStack(): Fragment? {
        return testRule.activity.supportFragmentManager.fragments.lastOrNull()
    }

}