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
        script.withCredentials([script.usernamePassword(credentialsId: 'access_to_dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "echo $script.PASS | docker login -u $script.USER --password-stdin"
        }
    }

    def deployToAWS(String imageName, String hostName) {
        def appInit = "bash ./initScript.sh $imageName"
        script.sshagent(['ssh_to_aws']) {
            // how one should configure OS to scp files without specifying .pem file :0
            script.sh "scp -o StrictHostKeyChecking=no docker-compose.yaml $hostName:/home/ec2-user"
            script.sh "scp -o StrictHostKeyChecking=no initScript.sh $hostName:/home/ec2-user"
            script.sh "ssh -o StrictHostKeyChecking=no $hostName ${appInit}"
        }
    }

    def dockerPushImage(String imageName) {
        script.sh "docker push $imageName"
    }

}