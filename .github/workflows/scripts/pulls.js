exports.findAll = async function (context, github, predicate = (it) => true) {
    const values = await github.rest.pulls.list({
        owner: context.repo.owner,
        repo: context.repo.repo,
        state: "open",
    });

    return values.data.filter((it) => {
        return it.user.login.endsWith("[bot]") &&
            it.user.type === "Bot" &&
            predicate(it);
    });
};
