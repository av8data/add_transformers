#!/bin/bash

sbtver=1.4.1
sbtjar=sbt-launch.jar
sbtsha128=a9ca97fbfe0b9efa8bc9aee35e6b93c91043aa85

sbtrepo="https://repo1.maven.org/maven2/org/scala-sbt/sbt-launch"

if [ ! -f $sbtjar ]; then
  echo "downloading $PWD/$sbtjar" 1>&2
  if ! curl --location --silent --fail --remote-name $sbtrepo/$sbtver/$sbtjar; then
    exit 1
  fi
fi

checksum=`openssl dgst -sha1 $sbtjar | awk '{ print $2 }'`
if [ "$checksum" != $sbtsha128 ]; then
  echo "bad $PWD/$sbtjar.  delete $PWD/$sbtjar and run $0 again."
  exit 1
fi

[ -f ~/.sbtconfig ] && . ~/.sbtconfig

java -ea                          \
  $SBT_OPTS                       \
  $JAVA_OPTS                      \
  -jar $sbtjar "$@"
