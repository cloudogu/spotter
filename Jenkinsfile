/*
 * Copyright (c) 2020 - present Cloudogu GmbH
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 */

pipeline {

  agent {
    docker {
      image 'scmmanager/java-build:17.0.9_9'
      label 'docker'
    }
  }

  environment {
    HOME = "${env.WORKSPACE}"
    SONAR_USER_HOME = "${env.WORKSPACE}/.sonar"
  }

  stages {

    stage('Set Version') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
      }
      steps {
        // fetch all remotes from origin
        sh "git tag -d ${releaseVersion} || true"
        sh 'git config "remote.origin.fetch" "+refs/heads/*:refs/remotes/origin/*"'
        authGit 'SCM-Manager', 'fetch --all --tags'
        sh "git checkout ${env.BRANCH_NAME}"
        sh "git reset --hard origin/${env.BRANCH_NAME}"

        // read version from brach, set it and commit it
        mvn "versions:set -DnewVersion=${releaseVersion} -DgenerateBackupPoms=false"
        sh 'git add pom.xml */pom.xml'
        commit "release version ${releaseVersion}"

        // checkout, reset and merge
        sh "git checkout main"
        sh "git reset --hard origin/main"
        sh "git merge --ff-only ${env.BRANCH_NAME}"

        // set tag
        tag releaseVersion
      }
    }

    stage('Check') {
      steps {
        mvn 'clean org.jacoco:jacoco-maven-plugin:0.8.12:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.12:report -Dmaven.test.failure.ignore=true'
        junit '*/target/surefire-reports/*.xml'
      }
    }

    stage('Build') {
      steps {
        mvn 'install -DskipTests'
        archiveArtifacts artifacts: 'spotter-core/target/*.jar'
      }
    }

    stage('Deployment') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
        expression { return isBuildSuccess() }
      }
      steps {
        withPublishEnvironment { args ->
          mvn "clean deploy -DskipTests ${args}"
        }
      }
    }

    stage('Update Repository') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
        expression { return isBuildSuccess() }
      }
      steps {
        // merge main in to develop
        sh 'git checkout develop'
        sh 'git merge main'

        // set version to next development iteration
        mvn 'versions:set -DnextSnapshot=true -DgenerateBackupPoms=false'
        sh 'git add pom.xml */pom.xml'
        commit 'prepare for next development iteration'

        // push changes back to remote repository
        authGit 'SCM-Manager', 'push origin main --tags'
        authGit 'SCM-Manager', 'push origin develop --tags'
        authGit 'SCM-Manager', "push origin :${env.BRANCH_NAME}"
      }
    }

    stage('Update GitHub') {
      when {
        branch pattern: 'main', comparator: 'GLOB'
        expression { return isBuildSuccess() }
      }
      steps {
        // push to GitHub
        authGit 'cesmarvin', 'push https://github.com/cloudogu/spotter main -f'
      }
    }
  }

}

void mvn(String command) {
  // setting user home system property, should fix user prefs (?/.java/.prefs ...)
  String args = "-Duser.home=${env.WORKSPACE}"

  // we have to skip the license check, because the damn plugin checks the local maven repo (.m2)
  args += " -Dlicense.skip=true"

  sh "./mvnw --batch-mode -V ${args} ${command}"
}

void withPublishEnvironment(Closure<Void> closure) {
  withCredentials([
    usernamePassword(credentialsId: 'mavenCentral-acccessToken', usernameVariable: 'MAVEN_CENTRAL_USERNAME', passwordVariable: 'MAVEN_CENTRAL_PASSWORD'),
    file(credentialsId: 'mavenCentral-secretKey-asc-file', variable: 'ascFile'),
    string(credentialsId: 'mavenCentral-secretKey-Passphrase', variable: 'passphrase')
  ]) {
    String settingsXmlPath = "${env.WORKSPACE}/.jenkins-settings.xml"
    writeFile file: settingsXmlPath, text: """
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>\${env.MAVEN_CENTRAL_USERNAME}</username>
      <password>\${env.MAVEN_CENTRAL_PASSWORD}</password>
    </server>
  </servers>
</settings>
    """

    String args = " --settings ${settingsXmlPath} -DperformRelease"

    withEnv(["PGP_SECRETKEY=keyfile:${env.ascFile}",
             "PGP_PASSPHRASE=literal:${env.passphrase}"]) {
      closure.call(args)
    }
  }
}

void commit(String message) {
  sh "git -c user.name='CES Marvin' -c user.email='cesmarvin@cloudogu.com' commit -m '${message}'"
}

void tag(String version) {
  String message = "Release version ${version}"
  sh "git -c user.name='CES Marvin' -c user.email='cesmarvin@cloudogu.com' tag -m '${message}' ${version}"
}

String getReleaseVersion() {
  return env.BRANCH_NAME.substring("release/".length())
}

void isBuildSuccess() {
  return currentBuild.result == null || currentBuild.result == 'SUCCESS'
}

void authGit(String credentials, String command) {
  withCredentials([
    usernamePassword(credentialsId: credentials, usernameVariable: 'AUTH_USR', passwordVariable: 'AUTH_PSW')
  ]) {
    sh "git -c credential.helper=\"!f() { echo username='\$AUTH_USR'; echo password='\$AUTH_PSW'; }; f\" ${command}"
  }
}
