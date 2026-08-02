pipeline {
    agent any

    environment {
        IMAGE_NAME = 'userservice'
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Login to Amazon ECR') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-ecr-creds'
                ]]) {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} \
                        | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                    """
                }
            }
        }

        stage('Tag Image') {
            steps {
                sh """
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} \
                    ${ECR_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Push Image') {
            steps {
                sh """
                    docker push ${ECR_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Deploy to kubernetes') {
            steps {
                sshagent(credentials: ['k8s-server-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@${K8S_SERVER} '
                        
                         kubectl set image deployment/user-service \
                   		 
                   		 user-service=${ECR_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}

                    	 kubectl rollout status deployment/user-service
                        

                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'User Service deployed successfully.'
        }

        failure {
            echo 'Deployment failed.'
        }

        always {
            sh 'docker image prune -f'
        }
    }
}