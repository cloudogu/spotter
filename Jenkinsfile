/*
 * MIT License
 *
 * Copyright (c) 2021, Cloudogu GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

pipeline {

  agent {
    docker {
      image 'openjdk:11'
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
        // read version from brach, set it and commit it
        mvn "versions:set -DnewVersion=${releaseVersion} -DgenerateBackupPoms=false"
        sh 'git add pom.xml */pom.xml'
        commit "release version ${releaseVersion}"

        // fetch all remotes from origin
        sh 'git config "remote.origin.fetch" "+refs/heads/*:refs/remotes/origin/*"'
        sh 'git fetch --all'

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
        mvn 'clean test'
        junit '*/target/surefire-reports/*.xml'
      }
    }

    stage('Build') {
      steps {
        mvn 'install -DskipTests'
        archiveArtifacts artifacts: 'spotter-core/target/*.jar'
      }
    }

    stage('SonarQube') {
      steps {
        sh 'git config --replace-all "remote.origin.fetch" "+refs/heads/*:refs/remotes/origin/*"'
        sh 'git fetch origin develop'
        script {
          withSonarQubeEnv('sonarcloud.io-cloudogu') {
            String parameters = ' -Dsonar.organization=cloudogu'
            if (env.CHANGE_ID) {
              parameters += ' -Dsonar.pullrequest.provider=GitHub'
              parameters += ' -Dsonar.pullrequest.github.repository=cloudogu/spotter'
              parameters += " -Dsonar.pullrequest.key=${env.CHANGE_ID}"
              parameters += " -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH}"
              parameters += " -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
            } else {
              parameters += " -Dsonar.branch.name=${env.BRANCH_NAME}"
              if (env.BRANCH_NAME != "develop") {
                parameters += " -Dsonar.branch.target=develop"
              }
            }
            mvn " sonar:sonar ${parameters}"
          }
        }
      }
    }

    stage('Deployment') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
        expression { return isBuildSuccess() }
      }
      steps {
        withPublishEnvironment { args ->
          // TODO create settings.xml and pass settings
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
        authGit 'cesmarvin-github', 'push origin main --tags'
        authGit 'cesmarvin-github', 'push origin develop --tags'
        authGit 'cesmarvin-github', "push origin :${env.BRANCH_NAME}"
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
      <id>ossrh</id>
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
