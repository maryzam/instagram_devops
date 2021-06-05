package customBuildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

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
}))

