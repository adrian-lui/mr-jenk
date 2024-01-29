pipeline {
  agent any
  stages {
    // stage('Checkout Git') {
    //   steps {
    //     git(url: 'https://github.com/adrian-lui/mr-jenk.git', branch: 'main')
    //   }
    // }

    // stage('ls -la') {
    //   steps {
    //     sh 'ls -la'
    //   }
    // }

    // stage('Docker check') {
    //   steps {
    //     sh 'docker version'
    //   }
    // }

    // stage('Docker prune') {
    //   steps {
    //     sh 'docker system prune -a --volumes -f'
    //   }
    // }

    stage('ssh to web app host') {
      steps {
        sh 'ssh -tt -i /var/lib/jenkins/.ssh/jenkins.pem jenkins@20.82.141.107'
        sh 'ls -la'
        sh 'exit'
      }
    }
  }
}