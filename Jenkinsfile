 pipeline {
    agent any

      environment {
        PATH="/usr/local/bin:$PATH"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'c4b2addd-5cd4-4892-bbb4-7b34a202ebd6', url: 'https://github.com/jasim003/DevOps_Task_ACP.git'
            }
        }
        stage('Docker Builld') {
            steps {
               sh '''
               #!/bin/bash
               docker build -t python:v1 . 
               '''
            }
        }
        stage('Docker image tag') {
            steps {
               sh '''
               #!/bin/bash
               docker tag python:v1 jasimdocker003/python:v1
               '''
            }
        }
            
        stage('Docker push') {
            steps {
               sh '''
               #!/bin/bash
               docker push jasimdocker003/python:v1
               '''
            }
        }
        stage('Helm Sanity') {
            steps {
               sh '''
               #!/bin/bash
               helm lint web-app-svc/
               '''
            }
        }
        stage('Python Deployment') {
            steps {
               sh '''
               #!/bin/bash
               pwd
               ls -lthr
               if [[ $(helm install python web-app-svc -f python/value.yaml -n "${EnvironmentName}") == *Error* ]]; then
                  echo "Release package is not avaliable!"
                else
                    helm upgrade python web-app-svc -f python/value.yaml -n "${EnvironmentName}"
                fi
               '''
            }
        }
        stage('Redis Deployment') {
            steps {
               sh '''
               #!/bin/bash
                if [[ $(helm install redis web-app-svc -f redis/value.yaml -n "${EnvironmentName}") == *Error* ]]; then
                    echo "Release package is not avaliable!"
                else
                    helm upgrade redis web-app-svc -f redis/value.yaml -n "${EnvironmentName}"
                fi
               '''
            }
        }
        stage('Pod Status') {
            steps {
               sh '''
               #!/bin/bash
               kubectl wait --for=condition=ready pod -l app=python -n "${EnvironmentName}" --timeout=30s
               
               kubectl wait --for=condition=ready pod -l app=redis -n "${EnvironmentName}" --timeout=30s
               '''
            }
        }
    }
post {
    always {
        cleanWs( cleanWhenNotBuilt: false, disableDeferredWipeout: true, notFailBuild: true)
    }
}
}