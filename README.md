# Implementing the Paging Library
## [nyc.droidcon.com](https://www.nyc.droidcon.com/speaker/Ash-Davies)

The Android Paging Library makes it easy to integrate complex paging behaviour, gradually loading small chunks of data at a time to help reduce usage of network bandwidth and system resources.

The library allows you to implement this behaviour using compositional components in a decoupled architecture making your code more reliable, scalable, and testable. Furthermore, you’ll be able to use familiar components such as LiveData or RxJava to interface with your existing architecture.

In this talk you’ll learn:
 - how to integrate the PagedList component into your architecture
 - how to implement a DataSource to load snapshots when necessary
 - how to use BoundaryCallback to signal the end of available data
 - how to integrate LiveData or RxJava to fit your project

## Project
An example application for the Android Data Binding library, making use of Retrofit, Coroutines, Moshi, and Android Jetpack components, to fetch a list of GitHub repositories and update a RecyclerView using the binding methods to display each repository as a view component.

Enter a GitHub username in the ActionBar SearchView component to trigger the observable request, the application uses Retrofit to query the open GitHub api for repositories, results are returned in the RecyclerView with data bindings.

![device-2018-06-10-200109](https://github.com/ashdavies/data-binding/raw/master/art/device-2018-06-10-200109.png)
![device-2018-06-10-191700](https://github.com/ashdavies/data-binding/raw/master/art/device-2018-06-10-191700.png)
