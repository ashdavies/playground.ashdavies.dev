exports.findAll = async function (context, github, predicate = (it) => true) {
  const comments = await github.rest.issues.listComments({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
  });

  return comments.data.filter((comment) => {
    const result = comment.user.login.endsWith("[bot]") &&
      comment.user.type === "Bot" &&
      predicate(comment);
    
    console.log(`Filter comment result ${result}`)
    console.log(comment.user)
    console.log(comment)
    result
  });
};

exports.create = function (context, github, body) {
  return github.rest.issues.createComment({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
    body,
  });
};

exports.delete = function (context, github, id) {
  return github.rest.issues.deleteComment({
    owner: context.repo.owner,
    repo: context.repo.repo,
    comment_id: id,
  });
};

exports.deleteAll = function (context, github, predicate = (it) => true) {
  for (const item in exports.findAll(context, github, predicate)) {
    console.log(`Deleting comment ${item.id}`);
    exports.delete(context, github, item.id);
  }
};
