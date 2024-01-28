pipeline {
  agent any
  stages {
    stage('Checkout Git') {
      steps {
        git(url: 'https://github.com/adrian-lui/mr-jenk.git', branch: 'main')
      }
    }

    stage('ls -la') {
      steps {
        sh 'ls -la'
      }
    }

    stage('Docker check') {
      steps {
        sh 'docker version'
      }
    }

    stage('ssh to web app host') {
      steps {
        sh 'ssh -i ssh -i ~/.ssh/buy-01-app_key.pem azureuser@20.82.141.107'
        sh 'ls -la'
        sh 'exit'
      }
    }

    // stage('Docker prune') {
    //   steps {
    //     sh 'docker system prune -a --volumes -f'
    //   }
    // }

    // stage('Docker compose') {
    //   steps {
    //     sh 'docker compose up -d --no-color --wait'
    //     sh 'docker compose ps'
    //   }
    // }
  }
}