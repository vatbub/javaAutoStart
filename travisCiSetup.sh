#!/bin/bash

if [ $TRAVIS_OS_NAME = 'windows' ]; then
  choco install jdk8
  choco install maven
  export JAVA_HOME="/c/Program Files/Java/jdk1.8.0_211"
  export PATH=$PATH:/c/ProgramData/chocolatey/lib/maven/apache-maven-3.6.3/bin:$JAVA_HOME/bin
else
  wget https://s3.eu-central-1.amazonaws.com/vatbubjdk/jdk-8u191-linux-x64.tar.gz -nv
  tar -xf jdk-8u191-linux-x64.tar.gz
  export JAVA_HOME=/home/travis/build/vatbub/javaAutoStart/jdk1.8.0_191/
fi
