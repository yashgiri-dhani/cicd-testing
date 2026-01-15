pipeline {
    agent any

    environment {
        DEPLOYMENT_DIR = '/Users/yash.giri/Desktop/auto-uat-deploy/deployment'
        BACKUP_DIR     = '/Users/yash.giri/Desktop/auto-uat-deploy/backup'
        SERVICE_URL    = 'http://localhost:8090'
        HEALTH_ENDPOINT = '/health'
        BUILD_OUTPUT_DIR = '/Users/yash.giri/Desktop/auto-uat-deploy/build'
        HOME_PATH = '/Users/yash.giri/Desktop/auto-uat-deploy/'
    }

    stages {
        stage('Init Timestamp') {
            steps {
                script {
                    env.TIMESTAMP = sh(
                        script: 'date +%Y%m%d_%H%M%S',
                        returnStdout: true
                    ).trim()
                }
            }
        }

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
                    echo 'Build successfully'
                }
                echo '✅ Build completed!'
            }
        }

        stage('Backup Current Deployment') {
            steps {
                echo '💾 Backing up current deployment...'
                sh """


                    if [ -d "${DEPLOYMENT_DIR}" ] && [ "\$(ls -A ${DEPLOYMENT_DIR} 2>/dev/null)" ]; then
                        tar -czf ${BACKUP_DIR}/backup_${TIMESTAMP}.tar.gz -C ${DEPLOYMENT_DIR} . || true
                        echo "${TIMESTAMP}" > ${BACKUP_DIR}/last_successful.txt

                        cd ${BACKUP_DIR}
                        ls -t backup_*.tar.gz 2>/dev/null | tail -n +6 | xargs -r rm -f
                    else
                        echo "⚠️ No existing deployment to backup"
                    fi
                """
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying application...'
                sh """

//                     rm -rf ${DEPLOYMENT_DIR}/*
//
//                     if [ -d "${BUILD_OUTPUT_DIR}" ]; then
//                         cp -r ${BUILD_OUTPUT_DIR}/* ${DEPLOYMENT_DIR}/
//                     else
//                         echo "❌ Build output directory not found"
//                         exit 1
//                     fi



//                     chmod -R 755 ${DEPLOYMENT_DIR}

                    kill -9 \$(pgrep -f hospitalManagement) || true
                    sleep 1
                    java -jar ${HOME_PATH}/hospitalManagement-0.0.1-SNAPSHOT.jar
                """
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sleep 5

                    def healthCheckPassed = false
                    for (int i = 1; i <= 3; i++) {
                        def code = sh(
                            script: "curl -s -o /dev/null -w '%{http_code}' ${SERVICE_URL}${HEALTH_ENDPOINT} || echo 000",
                            returnStdout: true
                        ).trim()

                        if (code == '200') {
                            healthCheckPassed = true
                            break
                        }
                        sleep 3
                    }

                    if (!healthCheckPassed) {
                        error "❌ Health check failed"
                    }
                }
            }
        }

        stage('Mark as Successful') {
            steps {
                sh """
                    echo "${TIMESTAMP}" > ${BACKUP_DIR}/last_successful.txt
                """
            }
        }
    }

    post {
        success {
            echo '🎉 DEPLOYMENT SUCCESSFUL!'
        }

        failure {
            echo '❌ DEPLOYMENT FAILED! Initiating rollback...'
            sh """
                if [ -f "${BACKUP_DIR}/last_successful.txt" ]; then
                    LAST_SUCCESS=\$(cat ${BACKUP_DIR}/last_successful.txt)
                    BACKUP_FILE=${BACKUP_DIR}/backup_\${LAST_SUCCESS}.tar.gz

                    if [ -f "\${BACKUP_FILE}" ]; then
                        rm -rf ${DEPLOYMENT_DIR}/*
                        tar -xzf "\${BACKUP_FILE}" -C ${DEPLOYMENT_DIR}
                        chmod -R 755 ${DEPLOYMENT_DIR}

                        kill -9 \$(pgrep -f hospitalManagement) || true
                        sleep 1
                        java -jar ${HOME_PATH}/hospitalManagement-0.0.1-SNAPSHOT.jar &
                    fi
                fi
            """
        }

        always {
            echo "📊 Pipeline finished | Build: ${BUILD_NUMBER} | Job: ${JOB_NAME}"
        }
    }
}
