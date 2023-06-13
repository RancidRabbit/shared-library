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

    def updatePom(){
        script.withCredentials([script.usernamePassword(credentialsId: 'gh_token_for_jenkins', passwordVariable: 'PASS', usernameVariable: 'USER')]){

            script.sh 'git config --global user.email "jenkins@gmail.com"'
            script.sh 'git config --global user.name "jenkins"'

            script.sh "git remote set-url origin https://${script.USER}:${script.PASS}@github.com/RancidRabbit/awsSpringTEst.git"
            script.sh 'git add pom.xml'
            script.sh 'git commit -m "updating version in pom.xml"'

            script.sh 'git push origin HEAD:main'
        }
    }
}