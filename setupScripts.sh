#!/bin/bash
set -x
set -e

mkdir -p target/jira/home/automat
cat > target/jira/home/automat/master.sh <<EOF
echo $* > "$PWD/output.txt"
EOF

cd target/jira/home/automat
for p in userSignUp.sh userCreated.sh userForgotPassword.sh userForgotUsername.sh cannotChangePassword.sh userLogin.sh userLogout.sh; do
	ln -s master.sh $p
done