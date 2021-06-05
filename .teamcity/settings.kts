import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.1"

project {

    buildType(Build)
}

object Build : BuildType({
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
                node ./build/index.js --publish=./releases/%build.number% --source=./src/post.json
            """.trimIndent()
        }
        nodeJS {
            enabled = false
            shellScript = "npm run test"
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
