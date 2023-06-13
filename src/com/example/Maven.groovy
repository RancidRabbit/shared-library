#!/usr/bin/env groovy

package com.example

class Maven implements Serializable {

    def script

    Maven(script) {
        this.script = script
    }

    def mavenBuildApp() {
        script.sh 'mvn -DskipTests install'
    }

    def mavenTestApp(){
        script.sh 'mvn test'
    }
}