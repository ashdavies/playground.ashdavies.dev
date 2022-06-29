package io.ashdavies.http

public interface Environment {

    public val properties: Map<Any, Any>
    public val env: Map<String, String>

    public companion object Default : Environment by System
}
