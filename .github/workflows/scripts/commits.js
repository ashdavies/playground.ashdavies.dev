exports.findAll = async function (context, github, pull, predicate = (it) => true) {
    const values = await github.rest.pulls.listCommits({
        owner: context.repo.owner,
        repo: context.repo.repo,
        pull_number: pull,
    });

    return values.data.filter((it) => {
        return it.user.login.endsWith("[bot]") &&
            it.user.type === "Bot" &&
            predicate(it);
    });
};
