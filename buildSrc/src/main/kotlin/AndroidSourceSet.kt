import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.NamedDomainObjectContainer

val NamedDomainObjectContainer<AndroidSourceSet>.main: AndroidSourceSet
  get() = getByName("main")

val NamedDomainObjectContainer<AndroidSourceSet>.test: AndroidSourceSet
  get() = getByName("test")

var AndroidSourceSet.srcDirs: Set<Any>
  get() = java.srcDirs
  set(value) {
    java.setSrcDirs(value)
  }
