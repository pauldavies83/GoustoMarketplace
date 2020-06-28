# GoustoMarketplace
A client for browsing the Gousto Marketplace API

https://api.gousto.co.uk/products/v2.0/products

## **Solution**
I have based the architecture of the solution around the repository pattern, which abstracts the network interface and database away from the ViewModel layer of the app. 

Network responses from the Gousto API are placed in the database, and it is the database which is observed by the *ProductsListViewModel*. This ensures after the first API response, data is always available to be viewed by the user. 

A "sync" operation is attempted each time the Products List screen is created - this could be further enhanced by implementing a "swipe to refresh" pattern to trigger a refresh.

I've implemented both the *Products List* and *Products Details* screens in Fragments, and made use of single *MainActivity*. This allows the app to be easily extensible by introducing a Coordinator pattern within the *MainActivty* to add more Fragments/Screens in future. With the correct abstraction the Coordinator would then be easily unit tested without having to rely on Espresso tests for navigation testing as is in the current solution.

Search is implemented with the standard Android *Search View* widget in the *App Bar*, and the *RecyclerView* contents are updated reactively by changing the query performed on the database. 

Unit tests and Espresso tests are included. The Espresso test suite uses Hilt/Dagger to unload the production network configuration, and replace it with a test variant to allow API responses to be modified to suit the scenario under test. 

## Libraries

**Essential libraries**

I have chosen to use the following libraries, which were essential to meeting the brief of the test.
 - **Jetpack** (androidx/AppCompat) libraries to provide Lifecycle Components such as *ViewModel*, *LiveData* etc, and modern layout tools like *RecyclerView* and *ConstraintLayout*.
 - **Espresso** for Automated UI testing.

**Optional libraries**

I chose to use the following libraries to create the best solution possible in the time available. Most, if not all, could be removed and either interchanged for an alternative, or replaced with other manual techniques given more time, but this may decrease the maintainability, readability or testability of the solution.

I've avoided using libraries to create view components - all views are from the Android framework or Jetpack widgets. 

 - **Hilt** (now [recommended](https://developer.android.com/training/dependency-injection#hilt)  over **Dagger** by Google) for Dependency Injection.
 - **OkHttp** and **Retrofit** for HTTP network stack and API binding, with **MockWebServer** for use in Espresso tests.
 - **kotlinx.serialization** for deserializing JSON into domain objects.
 - **Room** for persistence.
 - **RxJava** for reactive streams of data from API to ViewModel.
 - [Coil](https://coil-kt.github.io/coil/) for Image Loading and caching
 - [mockito-kotlin](https://github.com/nhaarman/mockito-kotlin) for readability in unit tests
