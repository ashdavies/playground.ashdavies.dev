package io.ashdavies.playground.profile.something

public interface Something {
    public companion object {
        public inline fun deferred(crossinline block: () -> Int): () -> Int = {
            double { block() * 2 }
        }

        public fun double(block: () -> Int): Int {
            return block() * 2
        }
    }
}
