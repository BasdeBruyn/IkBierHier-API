from os import environ
import requests


def get_merge_request_iid(project_url, private_token, commit_sha):
    merge_requests = requests.get(
        f"{project_url}/merge_requests/?private_token={private_token}&"
    )
    merge_request_iid = -1
    for mr in merge_requests.json():
        if mr['sha'] == commit_sha:
            merge_request_iid = mr['iid']

    return merge_request_iid


def add_report_comment(project_url, private_token, merge_request_iid, body):
    comments = requests.get(
        f"{project_url}/merge_requests/{merge_request_iid}/notes?private_token={private_token}"
    )
    print("Merge request found for commit")

    report_comments = [comment for comment in comments.json() if '<!-- report links -->' in comment['body']]
    for comment in report_comments:
        requests.delete(
            f"{project_url}/merge_requests/{merge_request_iid}/notes/{comment['id']}?private_token={private_token}"
        )
        print(f"Comment with id: {comment['id']} removed")

    requests.post(
        f"{project_url}/merge_requests/{merge_request_iid}/notes?private_token={private_token}",
        data={'body': body}
    )

    print("Comment added")


repo_url = f"http://{environ['CI_SERVER_HOST']}/api/v4/projects/{environ['CI_PROJECT_ID']}"
ci_token = environ['CI_PRIVATE_TOKEN']
commit_hash = environ['CI_COMMIT_SHA']

reports_url = f"http://{environ['CI_SERVER_HOST']}:{environ['REPORT_PORT']}/" \
              f"{environ['CI_PROJECT_NAME']}/" \
              f"{environ['CI_COMMIT_BRANCH']}/" \
              f"reports/project-reports.html"
comment_body = f"<!-- report links -->" \
               f"<strong>Reports for this branch can be found " \
               f"<a href='{reports_url}' target='_blank'>here</a></strong>"

mr_iid = get_merge_request_iid(repo_url, ci_token, commit_hash)

if mr_iid > 0:
    add_report_comment(repo_url, ci_token, mr_iid, comment_body)
else:
    print("No merge request found for commit")
