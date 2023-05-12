exports.findAll = async function (context, github, pull, predicate = (it) => true) {
    const values = await github.rest.pulls.listCommits({
        owner: context.repo.owner,
        repo: context.repo.repo,
        pull_number: pull,
    });

    return values.data.filter((it) => {
        return it.author.login.endsWith("[bot]") &&
            it.author.type === "Bot" &&
            predicate(it);
    });
};
