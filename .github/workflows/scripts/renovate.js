exports.invoke = async function (context, github) {
    const commits = require('./.github/workflows/scripts/commits.js');
    const pulls = require('./.github/workflows/scripts/pulls.js');

    const open = await pulls.findAll(context, github, (it) => {
        return it.head.ref.startsWith("renovate")
    })

    for (const pull of open) {
        const head = await github.rest.git.getCommit({
            commit_sha: pull.head.sha,
            owner: context.repo.owner,
            repo: context.repo.repo,
        })

        console.log("=== Pull Request Head Commit")
        console.log(head)

        const isUnsigned = !head.verification.verified && head.verification.reason == "unsigned"
        const isBot = pull.author.login.endsWith("[bot]") && pull.author.type == "Bot"

        if (isBot && isUnsigned) {
            const payload = {
                parents: head.parents.map(it => it.sha),
                owner: context.repo.owner,
                repo: context.repo.repo,
                message: head.message,
                author: head.author,
                tree: head.tree.sha,
            };

            console.log("=== Commit Payload ===")
            console.log(payload)

            const response = await github.rest.git.createCommit(payload)
            console.log(response)
            break
        }
    }
}