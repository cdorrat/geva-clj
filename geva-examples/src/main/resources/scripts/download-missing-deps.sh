#!/bin/sh
# GEVA examples depend on a few pacakges that aren't available from a maven repository,
# This script will downlaod the appropriate jars for the following packages and install them in the local repository.
#  - JSci - http://downloads.sourceforge.net/project/jsci/JSci/v1.2%20for%20Java%201.3%20or%20higher/JSci.zip
#  - JScheme - http://downloads.sourceforge.net/project/jscheme/jscheme/7.2/jscheme-7.2.jar


cd `mktemp -d tmp.XXXXXXXXXX`

# install JSci
wget http://downloads.sourceforge.net/project/jsci/JSci/v1.2%20for%20Java%201.3%20or%20higher/JSci.zip
unzip JSci.zip
mvn install:install-file -DgroupId=jsci -DartifactId=jsci-core -Dversion=1.2 -Dfile=JSci/classes/jsci-core.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=jsci -DartifactId=jsci-xtra -Dversion=1.2 -Dfile=JSci/classes/jsci-xtra.jar  -Dpackaging=jar -DgeneratePom=true

#Install JScheme
wget http://downloads.sourceforge.net/project/jscheme/jscheme/7.2/jscheme-7.2.jar
mvn install:install-file -DgroupId=jscheme -DartifactId=jscheme -Dversion=7.2 -Dfile=jscheme-7.2.jar  -Dpackaging=jar -DgeneratePom=true

