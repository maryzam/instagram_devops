package customBuildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*


object PublishInstagramPost : BuildType({
    name = "Publish Instagram Post"

    buildNumberPattern = "%dep.InstagramDevops_BuildInstagramPost.build.number%"

    params {
        param("env.IG_PROXY", "")
        param("env.IG_USERNAME", "maryzamdev")
        password("env.IG_PASSWORD", "credentialsJSON:7690d5f9-07bd-47c9-bc96-deaf236f4666", label = "env.IG_PASSWORD", display = ParameterDisplay.HIDDEN)
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Install npm packages"
            scriptContent = "npm install"
        }
        script {
            name = "Publish Instagram post"
            scriptContent = """
                node --version
                node ./.deploy/index.js --publish=./releases/insagram-post-%build.number%
            """.trimIndent()
        }
    }

    dependencies {
        dependency(BuildInstagramPost) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                cleanDestination = true
                artifactRules = "+:**/* => ./releases"
            }
        }
    }
}))