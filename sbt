#!/bin/bash

sbtver=1.4.4
sbtjar=sbt-launch.jar
sbtsha128=b09bbfad81206dc39ac2f780f35c80841aa3a407

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

java -ea \
  $SBT_OPTS                       \
  $JAVA_OPTS                      \
  -jar $sbtjar "$@"
