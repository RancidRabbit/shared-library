#!/usr/bin/env groovy
import com.example.Docker

def call(String imageName, String hostName){
    return new Docker(this).deployToAWS(imageName, hostName)
}