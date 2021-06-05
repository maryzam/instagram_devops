import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

import buildTypes.*

version = "2021.1"

project {
    
    buildType(TestPostConfiguration)
    buildType(BuildInstagramPost)
    buildType(PublishInstagramPost)

    buildTypesOrderIds = arrayListOf(TestPostConfiguration, BuildInstagramPost, PublishInstagramPost)
}
