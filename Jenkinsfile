pipeline {
  agent any
  tools {
    gradle '8.5'
    nodejs '20.8.1'
  }
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
        npm install
        sudo ng test --no-watch --browsers='ChromeHeadlessNoSandbox'
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
          docker compose build --push
        '''
      }
    }
          // docker system prune -a --volumes -f

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