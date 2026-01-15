pipeline {
    agent any
    
    environment {
        // Configuration - CHANGE THESE VALUES
        DEPLOYMENT_DIR = '/Users/yash.giri/Desktop/auto-uat-deploy/deployment'                    // Where to deploy your application
        BACKUP_DIR = '/Users/yash.giri/Desktop/auto-uat-deploy/backup'             // Where to store backups
        SERVICE_URL = 'http://localhost:8090'               // Your service URL to ping
        HEALTH_ENDPOINT = '/health'                          // Health check endpoint (e.g., /health, /, /api/status)
        TIMESTAMP = sh(returnStdout: true, script: 'date +%Y%m%d_%H%M%S').trim()
        BUILD_OUTPUT_DIR = 'build'   // Your build output directory (e.g., build, dist, target)
        HOME_PATH = '/Users/yash.giri/Desktop/auto-uat-deploy/'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Checking out code from GitHub...'
                checkout scm
                echo '✅ Code checkout successful!'
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Building application...'
                script {
                    // Add your build commands here
                    // Examples:
                    // sh 'npm install && npm run build'
                    // sh 'mvn clean package'
                    // sh 'python setup.py build'
                    // sh 'go build -o app'
                    
                    sh 'echo "Add your build commands here"'
                         sh "/opt/maven/bin/mvn clean install -Dmaven.test.skip"
                                        echo 'Build successfully'
                }
                echo '✅ Build completed!'
            }
        }

        
        stage('Backup Current Deployment') {
            steps {
                echo '💾 Backing up current deployment...'
                script {
                    sh """
                        # Create backup directory if not exists
                        mkdir -p ${BACKUP_DIR}
                        
                        # Backup current deployment if exists
                        if [ -d "${DEPLOYMENT_DIR}" ] && [ "\$(ls -A ${DEPLOYMENT_DIR} 2>/dev/null)" ]; then
                            echo "Creating backup of current deployment..."
                            tar -czf ${BACKUP_DIR}/backup_${TIMESTAMP}.tar.gz -C ${DEPLOYMENT_DIR} . 2>/dev/null || true
                            
                            # Store timestamp of this backup as last known good
                            echo "${TIMESTAMP}" > ${BACKUP_DIR}/last_successful.txt
                            
                            # Keep only last 5 backups
                            cd ${BACKUP_DIR}
                            ls -t backup_*.tar.gz 2>/dev/null | tail -n +6 | xargs -r rm -f
                            
                            echo "✅ Backup created: backup_${TIMESTAMP}.tar.gz"
                        else
                            echo "⚠️ No existing deployment to backup (first deployment)"
                        fi
                    """
                }
                echo '✅ Backup completed!'
            }
        }
        
        stage('Deploy') {
            steps {
                echo '🚀 Deploying application...'
                script {
                    sh """
                        # Create deployment directory if not exists
                        mkdir -p ${DEPLOYMENT_DIR}
                        
                        # Clear old files (be careful with this!)
                        rm -rf ${DEPLOYMENT_DIR}/*
                        
                        # Copy new build to deployment directory
                        if [ -d "${BUILD_OUTPUT_DIR}" ]; then
                            cp -r ${BUILD_OUTPUT_DIR}/* ${DEPLOYMENT_DIR}/
                            echo "✅ Files copied from ${BUILD_OUTPUT_DIR} to ${DEPLOYMENT_DIR}"
                        else
                            echo "❌ Build output directory not found: ${BUILD_OUTPUT_DIR}"
                            exit 1
                        fi
                        
                        # Set proper permissions
                        chmod -R 755 ${DEPLOYMENT_DIR}
                        
                        # Restart your service if needed
                        # Examples:
                        # sudo systemctl restart myapp
                        # docker restart myapp-container
                        # pm2 restart myapp

                        kill -9 $(pgrep -f hospitalManagement)
                         sleep(time: 1, unit: 'SECONDS')
                        java -jar ${HOME_PATH}/hospitalManagement-0.0.1-SNAPSHOT.jar

                        
                        echo "✅ Deployment files copied successfully"
                    """
                }
                echo '✅ Deployment completed!'
            }
        }
        
        stage('Health Check') {
            steps {
                echo '🏥 Performing health check...'
                script {
                    // Wait for service to start
                    echo 'Waiting 5 seconds for service to start...'
                    sleep(time: 5, unit: 'SECONDS')
                    
                    // Ping service 3 times
                    def healthCheckPassed = false
                    def maxAttempts = 3
                    
                    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                        echo "Health check attempt ${attempt}/${maxAttempts}..."
                        
                        def pingResult = sh(
                            script: "curl -s -o /dev/null -w '%{http_code}' ${SERVICE_URL}${HEALTH_ENDPOINT} || echo '000'",
                            returnStdout: true
                        ).trim()
                        
                        echo "HTTP Status: ${pingResult}"
                        
                        if (pingResult == '200') {
                            echo "✅ Health check passed! Service is responding."
                            healthCheckPassed = true
                            break
                        } else {
                            echo "⚠️ Health check failed (HTTP ${pingResult})"
                            if (attempt < maxAttempts) {
                                echo "Waiting 3 seconds before retry..."
                                sleep(time: 3, unit: 'SECONDS')
                            }
                        }
                    }
                    
                    // If health check failed, trigger rollback
                    if (!healthCheckPassed) {
                        error("❌ Health check failed after ${maxAttempts} attempts! Will trigger rollback.")
                    }
                }
                echo '✅ Health check passed! New deployment is working!'
            }
        }
        
        stage('Mark as Successful') {
            steps {
                echo '✅ Marking deployment as successful...'
                script {
                    sh """
                        # Mark this deployment as successful
                        echo "${TIMESTAMP}" > ${BACKUP_DIR}/last_successful.txt
                        echo "✅ Deployment marked as latest successful version"
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo '🎉 ✅ DEPLOYMENT SUCCESSFUL!'
            echo "New version deployed and verified at: ${SERVICE_URL}"
            echo "Deployment timestamp: ${TIMESTAMP}"
        }
        
        failure {
            echo '❌ DEPLOYMENT FAILED! Initiating rollback...'
            script {
                sh """
                    echo "🔄 Rolling back to last successful deployment..."
                    
                    # Check if we have a previous successful backup
                    if [ -f "${BACKUP_DIR}/last_successful.txt" ]; then
                        LAST_SUCCESS=\$(cat ${BACKUP_DIR}/last_successful.txt)
                        BACKUP_FILE="${BACKUP_DIR}/backup_\${LAST_SUCCESS}.tar.gz"
                        
                        echo "Last successful deployment: \${LAST_SUCCESS}"
                        
                        if [ -f "\${BACKUP_FILE}" ]; then
                            echo "Found backup file: \${BACKUP_FILE}"
                            
                            # Clear current failed deployment
                            rm -rf ${DEPLOYMENT_DIR}/*
                            
                            # Restore from backup
                            tar -xzf "\${BACKUP_FILE}" -C ${DEPLOYMENT_DIR}
                            
                            # Set permissions
                            chmod -R 755 ${DEPLOYMENT_DIR}
                            
                            echo "✅ Rollback completed successfully!"
                            echo "Service restored to version: \${LAST_SUCCESS}"
                            
                            # Restart service again if needed
                            # sudo systemctl restart myapp
                            kill -9 $(pgrep -f hospitalManagement)
                            sleep(time: 1, unit: 'SECONDS')
                             java -jar ${HOME_PATH}/hospitalManagement-0.0.1-SNAPSHOT.jar
                            
                            # Verify rollback worked
                            sleep 3
                            ROLLBACK_CHECK=\$(curl -s -o /dev/null -w '%{http_code}' ${SERVICE_URL}${HEALTH_ENDPOINT} || echo '000')
                            echo "Rollback health check: HTTP \${ROLLBACK_CHECK}"
                            
                            if [ "\${ROLLBACK_CHECK}" = "200" ]; then
                                echo "✅ Rollback successful! Service is responding."
                            else
                                echo "⚠️ Warning: Rollback completed but service check returned \${ROLLBACK_CHECK}"
                            fi
                        else
                            echo "❌ Backup file not found: \${BACKUP_FILE}"
                            echo "Cannot rollback - no previous successful deployment available"
                        fi
                    else
                        echo "⚠️ No previous successful deployment found"
                        echo "This might be the first deployment attempt"
                    fi
                """
            }
        }
        
        always {
            echo '📊 Pipeline execution finished.'
            echo "Build Number: ${BUILD_NUMBER}"
            echo "Job Name: ${JOB_NAME}"
        }
    }
}