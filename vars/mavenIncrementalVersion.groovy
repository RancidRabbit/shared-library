#!/usr/bin/env groovy
import com.example.Maven

def call(){
    return new Maven(this).mavenIncrementalVersion()
}