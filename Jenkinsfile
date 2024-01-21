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

    stage('Build') {
      steps {
        sh '''/usr/local/bin/docker-compose down
'''
        sh '/usr/local/bin/docker-compose up --build'
      }
    }

  }
}