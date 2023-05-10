exports.find = async function (context, github) {
  const comments = await github.rest.issues.listComments({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
  });

  return comments.data.find(comment =>
   comment.user.login.endsWith('[bot]') && comment.user.type === 'Bot'
  );
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
