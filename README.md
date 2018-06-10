## Droidcon Berlin 2018: Leveraging Android Databinding with Kotlin
Android databinding is considered as both a powerful toolchain, empowering your views with access to view data without the necessity to build cumbersome presenters, and conversely as an overly complex, convoluted mess of binding statements opening the door to unnecessary, irresponsible domain logic in your view layouts.
Whilst the latter of these statements can be true, databinding offers a very powerful code generation syntax, allowing you to utilise the power of the compiler to ensure that your binding statements are runtime safe. Combining this with the concise syntax afforded by Kotlin allows us to dramatically cut down on boilerplate and build complex user interfaces with relative ease.
In this talk, you can learn how to utilise extension bindings and property delegates with your layouts, and manage your screen state transformations with true, and safe two-way binding, allowing you to build a fully reactive observable view layout.

## Project
An example application for the Android Data Binding library, implementing retrofit to fetch a list of repositories and update a RecyclerView using the binding methods to display each repository as a view component.
Enter a GitHub username in the ActionBar SearchView component to trigger the observable request, the application uses Retrofit to query the open GitHub api for repositories, results are returned in the RecyclerView with data bindigs.

![device-2015-06-02-145653](https://cloud.githubusercontent.com/assets/1892070/7936230/c3141dc0-0937-11e5-9463-35d8cb06092a.png)

## Links
 - [Play Store Listing] (https://play.google.com/store/apps/details?id=com.chaos.databinding)
 - [AndroidX](https://developer.android.com/topic/libraries/support-library/androidx-rn)
 - [Coroutines](https://kotlinlang.org/docs/reference/coroutines.html)
 - [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
 - [Kotlin](https://kotlinlang.org/docs/reference/android-overview.html)
 - [Retrofit](https://github.com/square/retrofit)
 - [Moshi Codegen](https://github.com/square/moshi#codegen)
