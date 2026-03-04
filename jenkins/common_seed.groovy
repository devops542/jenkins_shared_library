def baseUrl = "https://github.com/devops542/"
def repoName = "hello-world-docker"
def gitRepoUrl = baseUrl + repoName + ".git"
def jobName = repoName

pipelineJob(jobName) {

    properties {
        pipelineTriggers {
            triggers {
                pollSCM {
                    scmpoll_spec("*/1 * * * *")
                    ignorePostCommitHooks(true)
                }
            }
        }
    }

    logRotator {
        numToKeep(5)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(gitRepoUrl)
                        credentials('github_credentials')
                    }
                    branches('main')
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            scriptPath("Jenkinsfile_shared_docker")
        }
    }
}
