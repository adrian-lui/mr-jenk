pipeline {
  agent any
  stages {
    stage('Checkout Git') {
      steps {
        git(url: 'https://github.com/adrian-lui/mr-jenk', branch: 'main')
      }
    }

    stage('ls -la') {
      steps {
        sh 'ls -la'
      }
    }

  }
}