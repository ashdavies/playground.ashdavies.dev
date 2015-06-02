## Android Data Binding Library with RecyclerView
An example application for the Android Data Binding library, implementing retrofit to fetch a list of repositories and update a RecyclerView using the binding methods to display each repository as a view component.

Enter a GitHub username in the ActionBar SearchView component to trigger the observable request, the application uses Retrofit to query the open GitHub api for repositories, results are returned in the RecyclerView with data bindigs.

![device-2015-06-02-145653](https://cloud.githubusercontent.com/assets/1892070/7936230/c3141dc0-0937-11e5-9463-35d8cb06092a.png)

### Usage
Requires Android Studio 1.3.0-beta1 or higher.

**Classpath dependencies**
```java
  classpath 'com.android.tools.build:gradle:1.2.3'
  classpath 'com.android.databinding:dataBinder:1.0-rc0'
```

**Plugins**
```java
apply plugin: ‘com.android.application'
apply plugin: 'com.android.databinding'
```

**Layout**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable name="user" type="com.example.User"/>
    </data>
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.firstName}" />
            
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.lastName}" />
   </LinearLayout>
</layout>
```

**Activity**
```java
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
    User user = new User("Test", "User");
    binding.setUser(user);
```

### Links
 - Play Store Listing (https://play.google.com/store/apps/details?id=com.chaos.databinding)
 - Data Bindings (https://developer.android.com/tools/data-binding/guide.html)
 - RecyclerView (https://developer.android.com/training/material/lists-cards.html#RecyclerView)
 - App Compat (http://android-developers.blogspot.de/2014/10/appcompat-v21-material-design-for-pre.html)
 - AppCompatActivity (https://plus.google.com/+AndroidDevelopers/posts/LNyDnnBYJ8r)
