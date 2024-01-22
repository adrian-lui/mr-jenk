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

    stage('Docker prune') {
      steps {
        sh 'docker system prune -a --volumes -f'
      }
    }

    stage('Docker compose') {
      steps {
        sh 'docker compose up -d --no-color --wait'
        sh 'docker compose ps'
      }
    }
  }
}