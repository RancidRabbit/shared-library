#!/usr/bin/env groovy

package com.example

class Docker implements Serializable {

    def script
    // возможность иметь доступ к переменным среды в JenkinsFile
    Docker(script) {
        this.script = script
    }


    def dockerBuildImage(String imageName) {
        script.sh "docker build -t $imageName ."
    }

    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'nexus-user', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "echo $script.PASS | docker login -u $script.USER --password-stdin 209.38.249.127:8083"
        }
    }

    def dockerPushImage(String imageName) {
        script.sh "docker push $imageName"
    }

}