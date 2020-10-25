import sbt.Keys._
import sbt._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val scala212 = "2.12.11"
lazy val scala213 = "2.13.1"
lazy val supportedScalaVersions = List(scala212, scala213)
val twitterReleaseVersion = "20.9.0"

ThisBuild / organization := "com.av8data"
ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := supportedScalaVersions


lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Compile := true,
  publishArtifact in Test := false,
  homepage := Some(url("https://av8data.com")),
  autoAPIMappings := true
)

showCurrentGitBranch
git.useGitDescribe := true
git.baseVersion := "0.0.0"
val VersionRegex = "v([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r
git.gitTagToVersionNumber := {
  case VersionRegex(v, "SNAPSHOT") => Some(s"$v-SNAPSHOT")
  case VersionRegex(v, "") => Some(v)
  case VersionRegex(v, s) => Some(v)
  case v => None
}

releaseVersionBump := sbtrelease.Version.Bump.Next

releaseVersion := { ver =>
  Version(ver)
    .map(_.bump(releaseVersionBump.value).string)
    .getOrElse(versionFormatError(ver))
}

releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  setReleaseVersion,
  runTest,
  tagRelease,
  publishArtifacts,
  pushChanges
)

addCommandAlias(
  name = "fixCheck",
  value = "; compile:scalafix --check ; test:scalafix --check"
)

lazy val sharedSettings = Seq(
  organization := "com.av8data",
  libraryDependencies ++= Seq(jxbLibs, scalaXml, scalaParser),
  crossScalaVersions := supportedScalaVersions,
  releaseCrossBuild := true
)

lazy val jxbLibs = "javax.xml.bind" % "jaxb-api" % "2.3.1"
lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

lazy val tafdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.tafdata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true,
  )
  .settings(publishSettings)

lazy val metardata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.metardata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true,
  )
  .settings(publishSettings)

lazy val pirepdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.pirepdata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true,
  )
  .settings(publishSettings)

lazy val aircraftreports = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.aircraftreports",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true,
  )
  .settings(publishSettings)

lazy val airsigmet = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.airsigmet",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true,
  )
  .settings(publishSettings)
