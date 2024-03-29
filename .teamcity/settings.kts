import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

version = "2021.1"

project {
    
    buildType(TestPostConfiguration)
    buildType(BuildInstagramPost)
    buildType(PublishInstagramPost)

    buildTypesOrderIds = arrayListOf(TestPostConfiguration, BuildInstagramPost, PublishInstagramPost)
}

object TestPostConfiguration : BuildType({
    name = "Test Post Configuration"
    buildNumberPattern = "v1.0.%build.counter%"

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
    }

    triggers {
        vcs {}
    }
})

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
})