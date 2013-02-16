#!/usr/bin/env python
import sys

try:
	import requests
except:
	print "run sudo easy_install requests"
	exit(1)

USERNAME = "admin"
PASSWORD = "admin"

def issueUpdatedAt(jiraUrl, issueKey):
	# ignore other projects than TST
	if (not issueKey.startswith("TST")):
		return
	issueUrl = "%s/rest/api/2/issue/%s" % (jiraUrl, issueKey)
	issue = requests.get(issueUrl, auth=(USERNAME, PASSWORD)).json()
	# dump issue details to a file
	with open("/tmp/test.txt", "w+") as f:
		f.write(str(issue))

if __name__ == "__main__":
	issueUpdatedAt(sys.argv[1], sys.argv[2])