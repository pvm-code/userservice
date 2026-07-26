pipeline {

    agent any

    environment {
        AWS_ACCOUNT_ID = '982920153818'
        AWS_REGION = 'ap-south-1'
        ECR_REPO = 'userservice'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        ECR_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}"
        APP_SERVER_IP = '15.252.45.161'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${ECR_URI}:${IMAGE_TAG} ."
                sh "docker tag ${ECR_URI}:${IMAGE_TAG} ${ECR_URI}:latest"
            }
        }

        stage('Push to ECR') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-ecr-creds'
                ]]) {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                        docker push ${ECR_URI}:${IMAGE_TAG}
                        docker push ${ECR_URI}:latest
                    """
                }
            }
        }

        stage('Deploy to app-server') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-host', variable: 'DB_HOST'),
                    string(credentialsId: 'db-name', variable: 'DB_NAME'),
                    string(credentialsId: 'db-user', variable: 'DB_USER'),
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD')
                ]) {
                    sshagent(credentials: ['app-server-ssh-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ec2-user@${APP_SERVER_IP} '
                                aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com &&
                                docker pull ${ECR_URI}:latest &&
                                docker stop userservice || true &&
                                docker rm userservice || true &&
                                docker run -d --name userservice -p 8080:8080 \\
                                    -e DB_HOST=${DB_HOST} \\
                                    -e DB_NAME=${DB_NAME} \\
                                    -e DB_USER=${DB_USER} \\
                                    -e DB_PASSWORD=${DB_PASSWORD} \\
                                    ${ECR_URI}:latest
                            '
                        """
                    }
                }
            }
        }
    }
}