import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

project {
    buildType(BuildInstagramPost)
}

object BuildInstagramPost : BuildType({
    name = "Build Instagram Post"

    artifactRules = "+:releases/%build.number% => insagram-post-%build.number%.zip"
    buildNumberPattern = "v1.0.%build.counter%"

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
        vcs {
        }
    }

    features {
        vcsLabeling {
            vcsRootId = "${DslContext.settingsRoot.id}"
        }
    }
})
