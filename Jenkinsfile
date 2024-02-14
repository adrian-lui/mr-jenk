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

    stage('SonarQube analysis') {
      environment {
        scannerHome = tool 'safe-zone-scanner';
      }
      steps {
        withSonarQubeEnv('safe-zone-server') { // If you have configured more than one global server connection, you can specify its name
          sh "${scannerHome}/bin/sonar-scanner"
        }
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
      post {
        always {
            junit '**/test-results/test/TEST-*.xml'
        }
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
        sh "sudo echo REVISION=${env.BUILD_NUMBER} | cat > ../.env"
        sh "docker version"
        sh 'docker login --username=$DOCKER_HUB_USR --password=$DOCKER_HUB_PSW'
        sh "docker compose --env-file ../.env build --push"
      }
    }

    stage('Deploy to web app host') {
      steps {
        sh "sudo scp -i ${SSH_KEY} deployment.yml jenkins@20.82.141.107:~/docker-compose.yml"
        sh "sudo scp -i ${SSH_KEY} ../.env jenkins@20.82.141.107:~/.env"
        sh "sudo ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose down"
        sh "sudo ssh -i ${SSH_KEY} -t jenkins@20.82.141.107 docker compose --env-file .env up -d"
        sh "exit"
      }
    }
  }

  post {
    always {
        sh "sudo rm -rf ./frontend/.angular" // clear bug cache
        sh "docker compose down"
    }
    success {
      sh "sudo echo Last successful build is now ${env.BUILD_NUMBER}"
      sh "sudo echo REVISION=${env.BUILD_NUMBER} | cat > ../.last_success_env"
      mail bcc: '', body: "<b>mr-jenk build success</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Success -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    failure {
      sh "sudo echo Build fails. Rolling back to last stable build."
      sh 'sudo curl -u $ROLLBACK_USR:$ROLLBACK_PSW http://74.234.33.226:8080/job/mr-jenk-rollback/build?token=stable-version'
      mail bcc: '', body: "<b>mr-jenk build failure</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Fail -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
    unstable {
      sh "sudo echo Build unstable. If deployment fails, please run rollback job"
      mail bcc: '', body: "<b>mr-jenk build unstable</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Go to URL of build to check details: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: 'luinairda@gmail.com', mimeType: 'text/html', replyTo: '', subject: "[mr-jenk] Unstable -> ${env.JOB_NAME}", to: "luinairda@gmail.com";
    }
  }
}