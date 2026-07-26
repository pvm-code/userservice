pipeline{
	
	agent any
	
	
	environment{
		
		AWS_ACCOUNT_ID = '982920153818'
		AWS_REGION =  'ap-south-1'
		ECR_REPO = 'userservice'
		IMAGE_TAG = "${env.BUILD_NUMBER}"
		ECR_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}"
		
	}
	
	stages{
		stage('Checkout'){
			steps{
				chekout scm
			}
		}
		
		
		stage('Build JAR'){
			steps {
				sh './mvnw clean package -DskipsTest'
			}
		}
		
		stage('Push to ECR'){
			steps {
				withCredentials([[
					
					$class: 'AmazonWebServicesCredentialsBinding',
					credentialsId: 'aws-ecr-creds'
				]]){
					
					sh """
					
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
						docker push ${ECR_URI}:${IMAGE_TAG}
						docker push ${ECR_URI}:latest
					
					"""
					
					
					
				}
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}