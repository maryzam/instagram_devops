package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'BuildInstagramPost'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("BuildInstagramPost")) {
    check(artifactRules == "+:releases/%build.number% => insagram-post-%build.number%.zip") {
        "Unexpected option value: artifactRules = $artifactRules"
    }
    artifactRules = "+:releases/%build.number% => insagram-post-%build.number%"

    triggers {
        remove {
            vcs {
            }
        }
        add {
            finishBuildTrigger {
                buildType = "InstagramDevops_TestPostConfiguration"
            }
        }
    }

    dependencies {
        add(RelativeId("TestPostConfiguration")) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }

    }
}
