exports.findAll = async function (context, github, predicate = (it) => true) {
    const pulls = await github.rest.pulls.list({
      owner: context.repo.owner,
      repo: context.repo.repo,
      state: "open",
    });
  
    return pulls.data.filter((pull) => {
      return pull.user.login.endsWith("[bot]") &&
        pull.user.type === "Bot" &&
        predicate(pull);
    });
  };