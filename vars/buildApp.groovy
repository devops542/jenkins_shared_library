def call(Map pipelineParams){

    def projectName = pipelineParams.repoName

    pipeline {
        agent any

        environment {
            registry = "rakesh199403/${projectName}"
            registryCredential = 'dockerhub_credentials'
            dockerImage = ''
        }

        stages {

            stage('Get SCM') {
                steps {
                    git(
                        branch: 'main',
                        credentialsId: 'github_credentials',
                        url: "https://github.com/devops542/${projectName}.git"
                    )
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        dockerImage = docker.build("${registry}:${BUILD_NUMBER}")
                    }
                }
            }

            stage('Push Docker Image') {
                steps {
                    script {
                        docker.withRegistry('', registryCredential) {
                            dockerImage.push()
                        }
                    }
                }
            }

            stage('Remove Local Docker Image') {
                steps {
                    sh "docker rmi ${registry}:${BUILD_NUMBER}"
                }
            }

        }
    }
}
