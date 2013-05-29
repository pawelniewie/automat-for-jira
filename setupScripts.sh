#!/bin/bash
set -x
set -e

mkdir -p target/jira/home/automat
cat > target/jira/home/automat/master.sh <<EOF
echo \$0 \$* >> "$PWD/output.txt"
EOF

cd target/jira/home/automat
chmod a+x master.sh

for p in userSignUp.sh userCreated.sh userForgotPassword.sh userForgotUsername.sh cannotChangePassword.sh userLogin.sh userLogout.sh; do
	ln -fs master.sh $p
done