 pipeline {
    agent any
    parameters {
    string(defaultValue: 'dev', description: 'Name of the environment to deploy', name: 'EnvironmentName', trim: true)
  }

      environment {
        PATH="/usr/local/bin:$PATH"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'c4b2addd-5cd4-4892-bbb4-7b34a202ebd6', url: 'https://github.com/jasim003/DevOps_Task_ACP.git'
            }
        }
        stage('Helm Sanity') {
            steps {
               sh '''
               #!/bin/bash
               ${HELM} lint web-app-svc/
               '''
            }
        }
        stage('Python Deployment') {
            steps {
               sh '''
               #!/bin/bash
               pwd
               ls -lthr
               if [[ $(helm upgrade python web-app-svc -n "${EnvironmentName}") == *Error* ]]; then
                    echo "Release package is not avaliable!"
                else
                    helm install python web-app-svc -n "${EnvironmentName}"
                fi
               '''
            }
        }
            
        stage('Redis Deployment') {
            steps {
               sh '''
               #!/bin/bash
                if [[ $(helm upgrade redis web-app-svc -f redis/value.yaml  -n "${EnvironmentName}") == *Error* ]]; then
                    echo "Release package is not avaliable!"
                else
                    helm install redis web-app-svc -f redis/value.yaml -n "${EnvironmentName}"
                fi
               '''
            }
        }
        stage('Pod Status') {
            steps {
               sh '''
               #!/bin/bash
               kubectl get po -n "${EnvironmentName}" | grep -i python
               sleep 2
               kubectl get po -n "${EnvironmentName}" | grep -i redis
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