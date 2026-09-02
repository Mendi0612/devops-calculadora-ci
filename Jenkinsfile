// Laboratorio DevOps - UNINPAHU - Semana 3
// Pipeline: Checkout -> Build -> Test -> Package -> Build Image -> Deploy -> Health Check

pipeline {

    agent any

    tools {
        maven 'Maven-3.9'
    }

    options {
        skipDefaultCheckout(true)
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositorio desde GitHub...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilando el proyecto...'
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Ejecutando pruebas unitarias con JUnit...'
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Empaquetando el archivo JAR...'
                sh 'mvn -B package -DskipTests'

                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Build Image') {
            steps {
                echo 'Construyendo imagen Docker...'

                sh '''
                    docker build -t calculadora-ci:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Deploy') {
            steps {
                echo 'Desplegando aplicación...'

                sh '''
                    docker rm -f calculadora-app || true

                    docker run -d \
                    --name calculadora-app \
                    -p 8081:8080 \
                    calculadora-ci:${BUILD_NUMBER}
                '''
            }
        }

        stage('Health Check') {
            steps {
                echo 'Verificando estado de la aplicación...'

                sh '''
                    sleep 5
                    curl -f http://host.docker.internal:8081/salud
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline finalizado correctamente.'
        }

        failure {
            echo 'El pipeline falló. Revisa la consola.'
        }
    }
}