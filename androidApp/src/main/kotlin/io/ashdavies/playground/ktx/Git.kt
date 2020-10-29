@file:Suppress("FunctionName")

package io.ashdavies.playground.ktx

import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.PullResult
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.util.concurrent.Callable

private const val GIT_EXT =
    ".git"

internal fun Git.checkout(block: CheckoutCommand.() -> Unit): Ref =
    callable(::checkout, block)

internal fun Git.pull(block: PullCommand.() -> Unit): PullResult =
    callable(::pull, block)

internal fun cloneRepository(block: CloneCommand.() -> Unit): Git =
    callable(Git::cloneRepository, block)

private fun <V, T : Callable<V>> callable(
    command: () -> T,
    block: T.() -> Unit
): V = command()
    .also(block)
    .call()

internal fun Git(directory: File): Git =
    FileRepositoryBuilder()
        .setGitDir(File(directory, GIT_EXT))
        .readEnvironment()
        .findGitDir()
        .build()
        .let(::Git)

@Suppress("SameParameterValue")
internal fun Git(directory: File, uri: String): Git =
    cloneRepository {
        setDirectory(directory)
        setURI(uri)
    }
