pipeline {
  agent any
  tools {
    gradle '8.5'
    nodejs '20.8.1'
  }
  environment {
    DOCKER_HUB = credentials('docker-hub')
    SSH_KEY = credentials('ssh-key')
    ROLLBACK = credentials('rollback-user')
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

    stage('Docker build and push images') {
      steps {
        sh "echo REVISION=${env.BUILD_NUMBER} | cat > ../.env"
        sh "docker version"
        sh 'docker login --username=$DOCKER_HUB_USR --password=$DOCKER_HUB_PSW'
        sh "docker compose --env-file ../.env build --push"
        sh "rm -rf ./frontend/.angular" // clear bug cache
      }
    }

    stage('Deploy to web app host') {
      steps {
        sh "scp -i ${SSH_KEY} deployment.yml jenkins@20.82.141.107:~/docker-compose.yml"
        sh "scp -i ${SSH_KEY} ../.env jenkins@20.82.141.107:~/.env"
        sh "ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose down"
        sh "ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose --env-file .env up -d"
        sh "exit"
      }
    }
  }

  post {
    success {
      sh "echo Last successful build is now ${env.BUILD_NUMBER}"
      sh "echo REVISION=${env.BUILD_NUMBER} | cat > ../.last_success_env"
      mail bcc: '', body: "<b>mr-jenk build success</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Success -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    failure {
      sh "echo Build fails. Rolling back to last stable build."
      sh 'curl -u $ROLLBACK_USR:$ROLLBACK_PSW http://74.234.33.226:8080/job/mr-jenk-rollback/build?token=stable-version'
      mail bcc: '', body: "<b>mr-jenk build failure</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Fail -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    unstable {
      sh "echo Build unstable. If deployment fails, please run rollback job"
      mail bcc: '', body: "<b>mr-jenk build unstable</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Unstable -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
  }
}