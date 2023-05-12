exports.invoke = async function (context, github) {
    const commits = require('./commits.js');
    const pulls = require('./pulls.js');

    const open = await pulls.findAll(context, github, (it) => {
        return it.head.ref.startsWith("renovate")
    })
    
    for (const pull of open) {
        console.log("=== Open Pull Request")
        console.log(open)

        const head = await github.rest.git.getCommit({
            commit_sha: pull.head.sha,
            owner: context.repo.owner,
            repo: context.repo.repo,
        })

        console.log("=== Pull Request Head Commit")
        console.log(head)

        const isUnsigned = !head.data.verification.verified && head.data.verification.reason == "unsigned"
        const isBot = pull.user.login.endsWith("[bot]") && pull.user.type == "Bot"

        if (isBot && isUnsigned) {
            const payload = {
                parents: head.data.parents.map(it => it.sha),
                message: head.data.message,
                owner: context.repo.owner,
                author: head.data.author,
                tree: head.data.tree.sha,
                repo: context.repo.repo,
            };

            console.log("=== Commit Payload ===")
            console.log(payload)

            //const response = await github.rest.git.createCommit(payload)
            //console.log(response)
            //break
        }
    }
}