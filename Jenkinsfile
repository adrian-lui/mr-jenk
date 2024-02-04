pipeline {
  agent any
  tools {
    gradle '8.5'
    nodejs '20.8.1'
  }
  environment {
    DOCKER_HUB = credentials('docker-hub')
    SSH_KEY = credentials('ssh-key')
  }
  stages {
    stage('Checkout Git') {
      steps {
        git(url: 'https://github.com/adrian-lui/mr-jenk.git', branch: 'main')
      }
    }

    // stage('backend gradle test') {
    //   steps {
    //     sh '''
    //     gradle --version
    //     cd backend
    //     gradle test
    //     cd ..
    //     '''
    //   }
    // }

    // stage('frontend smoke test') {
    //   steps {
    //     sh '''
    //     cd frontend
    //     npm install
    //     sudo ng test --no-watch --browsers='ChromeHeadlessNoSandbox'
    //     cd ..
    //     '''
    //   }
    // }

    stage('Docker check') {
      steps {
        sh 'docker version'
      }
    }

    stage('Set build tag') {
      steps {
        sh "sudo echo REVISION=${env.BUILD_NUMBER} | cat > ../.env"
      }
    }

    stage('Docker build and push images') {
      steps {
        sh 'docker login --username=$DOCKER_HUB_USR --password=$DOCKER_HUB_PSW'
        sh "docker compose --env-file ../.env build --push"
        sh "sudo rm -rf ./frontend/.angular" // clear bug cache
      }
    }

    stage('ssh to web app host') {
      steps {
        sh "sudo scp -i ${SSH_KEY} deployment.yml jenkins@20.82.141.107:~/docker-compose.yml"
        sh "sudo scp -i ${SSH_KEY} ../.env jenkins@20.82.141.107:~/.env"
        sh "sudo ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose down"
        sh "sudo ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose --env-file .envv up -d"
        sh "exit"
      }
    }

    stage('save last successful build') {
      steps {
        sh "sudo echo Last successful build is now ${env.BUILD_NUMBER}"
        sh "sudo echo REVISION=${env.BUILD_NUMBER} | cat > ../.last_success_env"
      }
    }
  }

  post {
    success {
      mail bcc: '', body: "<b>mr-jenk build success</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Success -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    failure {
      sh "sudo echo Build unsuccessful. If deployment fails, please run rollback job"
      mail bcc: '', body: "<b>mr-jenk build failure</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Fail -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    unstable {
      sh "sudo echo Build unstable. If deployment fails, please run rollback job"
      mail bcc: '', body: "<b>mr-jenk build unstable</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Unstable -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
  }
}