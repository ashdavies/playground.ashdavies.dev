package io.ashdavies.playground

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

public interface PlaygroundRoot<T : Any> {
    public val childStack: Value<ChildStack<*, T>>
}

public interface NavigationRoot<N : NavigationRoot.Navigation, T : NavigationRoot.Child<N>> : PlaygroundRoot<T> {
    public interface Child<N> {
        public val navigation: N
    }

    public interface Navigation
}
