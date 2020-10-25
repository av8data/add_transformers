#!/bin/sh

# checks if locally staged changes are
# formatted properly. Ignores non-staged
# changes.
# Intended as git pre-commit hook

_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
DIR=$( echo $_DIR | sed 's/\/.git\/hooks$//' )

#COLOR CODES:
#tput setaf 3 = yellow -> Info
#tput setaf 1 = red -> warning/not allowed commit
#tput setaf 2 = green -> all good!/allowed commit

echo ""
echo "$(tput setaf 3)Running pre-commit hook ... (you can omit this with --no-verify, but don't)$(tput sgr 0)"
git diff --quiet
hadNoNonStagedChanges=$?

if ! [ $hadNoNonStagedChanges -eq 0 ]
then
   echo "$(tput setaf 3)* Stashing non-staged changes$(tput sgr 0)"
   git stash --keep-index -u > /dev/null
fi

echo "$(tput setaf 3)* Compiling staged changes... $(tput sgr 0)"
(cd $DIR/; sbt test:compile)
compiles=$?

echo "$(tput setaf 3)* Compiles?$(tput sgr 0)"

if [ $compiles -eq 0 ]
then
   echo "$(tput setaf 2)* Yes$(tput sgr 0)"
else
   echo "$(tput setaf 1)* No$(tput sgr 0)"
fi

echo "$(tput setaf 3)* Running Scalafix on staged changes... $(tput sgr 0)"
(cd $DIR/; sbt fixCheck)
git diff --quiet
scalafix=$?

echo "$(tput setaf 3)* Properly scalafix?$(tput sgr 0)"

if [ $scalafix -eq 0 ]
then
   echo "$(tput setaf 2)* Yes$(tput sgr 0)"
else
   echo "$(tput setaf 1)* No$(tput sgr 0)"
    echo "$(tput setaf 1)The following files need scalafix (in stage or commited):$(tput sgr 0)"
    git diff --name-only
    echo ""
    echo "$(tput setaf 1)Please run 'sbt fixCheck' to scalafix the code.$(tput sgr 0)"
    echo ""
fi

echo "$(tput setaf 3)* Formatting staged changes... $(tput sgr 0)"
(cd $DIR/; scalafmt --mode diff)
git diff --quiet
formatted=$?

echo "$(tput setaf 3)* Properly formatted?$(tput sgr 0)"

if [ $formatted -eq 0 ]
then
   echo "$(tput setaf 2)* Yes$(tput sgr 0)"
else
   echo "$(tput setaf 1)* No$(tput sgr 0)"
    echo "$(tput setaf 1)The following files need formatting (in stage or commited):$(tput sgr 0)"
    git diff --name-only
    echo ""
    echo "$(tput setaf 1)Please run 'scalafmt' to format the code.$(tput sgr 0)"
    echo ""
fi

echo "$(tput setaf 3)* Undoing formatting$(tput sgr 0)"
git stash --keep-index > /dev/null
git stash drop > /dev/null

if ! [ $hadNoNonStagedChanges -eq 0 ]
then
   echo "$(tput setaf 3)* Scheduling stash pop of previously stashed non-staged changes for 1 second after commit.$(tput sgr 0)"
   sleep 1 && git stash pop --index > /dev/null & # sleep and & otherwise commit fails when this leads to a merge conflict
fi

if [ $compiles -eq 0 ] && [ $formatted -eq 0 ] && [ $scalafix -eq 0 ]
then
   echo "$(tput setaf 2)... done. Proceeding with commit.$(tput sgr 0)"
   echo ""
   exit 0
elif [ $formatted -ne 0 ]
then
   echo "$(tput setaf 1)... done.$(tput sgr 0)"
   echo "$(tput setaf 1)CANCELLING commit due to NON-FORMATTED CODE.$(tput sgr 0)"
   echo ""
   exit 1
elif [ $scalafix -ne 0 ]
then
   echo "$(tput setaf 1)... done.$(tput sgr 0)"
   echo "$(tput setaf 1)CANCELLING commit due to NON-SCALAFIX CODE.$(tput sgr 0)"
   echo ""
   exit 1
else
   echo "$(tput setaf 1)... done.$(tput sgr 0)"
   echo "$(tput setaf 1)CANCELLING commit due to COMPILE ERROR.$(tput sgr 0)"
    echo ""
   exit 2
fi
