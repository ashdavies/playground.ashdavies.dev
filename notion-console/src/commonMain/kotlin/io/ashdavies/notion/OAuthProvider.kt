package io.ashdavies.notion

public sealed interface OAuthProvider {
    public object Google : OAuthProvider
    public object Notion : OAuthProvider
}
