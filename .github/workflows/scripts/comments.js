exports.findAll = async function (context, github, predicate = (it) => true) {
  const comments = await github.rest.issues.listComments({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
  });

  return comments.data.filter((comment) => {
    return comment.user.login.endsWith("[bot]") &&
      comment.user.type === "Bot" &&
      predicate(comment);
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

exports.deleteAll = async function (context, github, predicate = (it) => true) {
  console.log("Invoking exports.deleteAll()")
  const comments = await exports.findAll(context, github, predicate)
  console.log(comments)

  //for (const item in await exports.findAll(context, github, predicate)) {
  for (const item in comments) {
    console.log(`Deleting comment ${item.id}`);
    await exports.delete(context, github, item.id);
  }
};
