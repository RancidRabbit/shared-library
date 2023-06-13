#!/usr/bin/env groovy

package com.example

class Maven implements Serializable {

    def script

    Maven(script) {
        this.script = script
    }

   def mavenIncrementVersion() {
       script.sh 'mvn build-helper:parse-version versions:set \
                 -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                 versions:commit'
       def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
       def version = matcher[1][1]
       def TAG = "$version-$script.BUILD_NUMBER"
   }

    def mavenBuildApp() {
        script.sh 'mvn -DskipTests install'
    }

    def mavenTestApp(){
        script.sh 'mvn test'
    }
}