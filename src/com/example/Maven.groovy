#!/usr/bin/env groovy

package com.example

class Maven implements Serializable {

    def script

    Maven(script) {
        this.script = script
    }

    def mavenIncrementalVersion(){
        script.sh 'mvn build-helper:parse-version versions:set \
                 -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                 versions:commit'
        def matcher = script.readFile('pom.xml') =~ '<version>(.+)</version>'
        def version = matcher[1][1]
        script.println(version)
        script.env.TAG = "$version-$script.BUILD_NUMBER"
        script.println("${script.TAG}")
    }

    def mavenBuildApp() {
        script.sh 'mvn -DskipTests install'
    }

    def mavenTestApp(){
        script.sh 'mvn test'
    }
}