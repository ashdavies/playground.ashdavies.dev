package io.ashdavies.playground

public sealed interface OAuthProvider {
    public object Google : OAuthProvider
    public object Notion : OAuthProvider
}
