package io.ashdavies.playground

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

public interface PlaygroundRoot<T : Any> {
    public val routerState: Value<RouterState<*, T>>
}

public interface NavigationRoot<N : NavigationRoot.Navigation, T : NavigationRoot.Child<N>> : PlaygroundRoot<T> {
    public interface Child<N> {
        public val navigation: N
    }

    public interface Navigation
}
