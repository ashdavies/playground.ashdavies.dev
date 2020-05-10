import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.NamedDomainObjectContainer

private const val Main = "main"
private const val Test = "test"

val NamedDomainObjectContainer<AndroidSourceSet>.main: AndroidSourceSet
    get() = getByName(Main)

val NamedDomainObjectContainer<AndroidSourceSet>.test: AndroidSourceSet
    get() = getByName(Test)

var AndroidSourceSet.srcDirs: Set<Any>
    get() = throw IllegalStateException()
    set(value) {
        java.setSrcDirs(value)
    }
