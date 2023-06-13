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

    def deployToAWS(String imageName, String hostName) {
        def appInit = "bash ./initScript.sh $imageName"
        script.sshagent(['jenkins_ssh_to_aws']) {
            script.sh "scp ./docker-compose.yaml $hostName:/home/ec2-user"
            script.sh "scp ./initScript.sh $hostName:/home/ec2-user"
            script.sh "ssh -o StrictHostKeyChecking=no $hostName ${appInit}"
        }
    }

    def dockerPushImage(String imageName) {
        script.sh "docker push $imageName"
    }

}