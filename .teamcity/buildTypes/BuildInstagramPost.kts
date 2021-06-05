package buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

object BuildInstagramPost : BuildType({
    name = "Build Instagram Post"

    buildNumberPattern = "v1.0.%build.counter%"
    artifactRules = "+:releases/%build.number% => insagram-post-%build.number%"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Install npm packages"
            scriptContent = "npm install"
        }

        script {
            name = "Run tests"
            scriptContent = "npm run test"
        }

        script {
            name = "Run Instagram post"
            scriptContent = """
                node --version
                node ./.build/index.js --publish=./releases/%build.number% --source=./src/post.json
            """.trimIndent()
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "InstagramDevops_TestPostConfiguration"
        }
    }

    dependencies {
        dependency(TestPostConfiguration) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
})