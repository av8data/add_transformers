#!/bin/sh
echo "$(tput setaf 3)* installing scalafmt library... $(tput sgr 0)"
brew install --HEAD olafurpg/scalafmt/scalafmt
touch ../.git/hooks/pre-commit #create the file if not exist
rm ../.git/hooks/pre-commit #delete the file
ln -s pre-commit-hook.sh ../.git/hooks/pre-push #create a file link
echo "$(tput setaf 3)* link to ../.git/hooks/pre-commit successfully created.$(tput sgr 0)"
cp pre-commit-hook.sh ../.git/hooks/pre-commit-hook.sh
echo "$(tput setaf 3)* pre-commit-hook successfully installed.$(tput sgr 0)"
