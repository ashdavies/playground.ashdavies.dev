@file:Suppress("FunctionName")

package io.ashdavies.playground.conferences

import io.ashdavies.playground.ktx.Git
import io.ashdavies.playground.ktx.checkout
import io.ashdavies.playground.ktx.emptyString
import io.ashdavies.playground.ktx.pull
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.GitHub
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.TRACK
import org.eclipse.jgit.api.Git
import java.io.File

private const val CONFERENCES_GIT =
    "https://github.com/AndroidStudyGroup/conferences.git"

private const val CONFERENCES_BRANCH =
    "gh-pages"

private const val CONFERENCES_PATH =
    "_conferences"

internal class GitConferencesService(
    private val directory: File,
) : GitHubService<String, Conference> {

    constructor(
        parent: File,
        child: String,
    ) : this(File(parent, child))

    private val git: Git =
        when {
            directory.exists() -> Git(directory)
            else -> Git(directory, CONFERENCES_GIT)
        }

    init {
        git.checkout {
            setName(CONFERENCES_BRANCH)
            setStartPoint("origin/$CONFERENCES_BRANCH")
            setUpstreamMode(TRACK)
        }

        git.pull { }
    }

    override suspend fun getAll(): List<GitHub.Item<Conference>> =
        File(directory, CONFERENCES_PATH)
            .run { list() as Array<String> }
            .map { get(it) }

    override suspend fun get(key: String): GitHub.Item<Conference> =
        File(directory, "$CONFERENCES_PATH/$key")
            .readText()
            .let { ContentItem(key, it) }

    private fun ContentItem(key: String, content: String): GitHub.Item<Conference> {
        val links = GitHub.Links(
            html = emptyString(),
            self = emptyString(),
            git = emptyString(),
        )

        val standard: GitHub.Item<Conference> = GitHub.StandardItem(
            downloadUrl = emptyString(),
            htmlUrl = emptyString(),
            gitUrl = emptyString(),
            path = directory.name,
            size = content.length,
            type = emptyString(),
            sha = emptyString(),
            url = emptyString(),
            links = links,
            name = key,
        )

        return GitHub.ContentItem(
            encoding = "text/yaml",
            delegate = standard,
            content = content,
        )
    }
}
