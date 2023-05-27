#!/usr/bin/env groovy

def call() {
    withCredentials([usernamePassword(credentialsId: 'nexus-user', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t 209.38.249.127:8083/simple-java:1.1 .'
        sh "echo $PASS | docker login -u $USER --password-stdin 209.38.249.127:8083"
        sh 'docker push 209.38.249.127:8083/simple-java:1.1'
    }
}