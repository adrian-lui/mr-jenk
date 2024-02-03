pipeline {
  agent any
  stages {
    stage('Checkout Git') {
      steps {
        git(url: 'https://github.com/adrian-lui/mr-jenk.git', branch: 'main')
      }
    }

    stage('backend gradle test') {
      steps {
        sh '''
        gradle --version
        cd backend
        gradle test
        cd ..
        '''
      }
    }

    stage('frontend smoke test') {
      steps {
        sh '''
        cd frontend
        ng test --no-watch --browsers='ChromeHeadlessNoSandbox'
        cd ..
        '''
      }
    }

    stage('Docker check') {
      steps {
        sh 'docker version'
      }
    }

    stage('Docker build and push images') {
      steps {
        sh '''
          docker system prune -a --volumes -f
          docker compose build --push
        '''
      }
    }

    stage('ssh to web app host') {
      steps {
        sh '''
          ssh -tt -i /var/lib/jenkins/.ssh/jenkins.pem jenkins@20.82.141.107'
          docker compose down
          docker compose up
          exit
        '''
      }
    }
  }
}