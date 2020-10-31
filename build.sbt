import sbt.Keys._
import sbt._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val scala212 = "2.12.11"
lazy val scala213 = "2.13.1"
lazy val supportedScalaVersions = List(scala212, scala213)

ThisBuild / organization := "com.av8data"
ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := supportedScalaVersions

inThisBuild(
  List(
    homepage := Some(url("https://av8data.com")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "mattav8data",
        "Matthew Dickinson ",
        "matt@av8data.com",
        url("https://av8data.com")
      )
    )
  ))

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Compile := true,
  publishArtifact in Test := false,
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

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
//  if (isSnapshot.value)
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

(sys.env.get("SONATYPE_USERNAME"), sys.env.get("SONATYPE_PASSWORD")) match {
  case (Some(username), Some(password)) =>
    println(s"Using credentials: $username/$password")
    credentials += Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org",
      username,
      password)
  case _ =>
    println("USERNAME and/or PASSWORD is missing, using local credentials")
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
}

credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "B98040CCE81E1A52F3358BED4391E8775B145A81", // key identifier
  "ignored"
)

pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray())

releaseVersionBump := sbtrelease.Version.Bump.Next
//releasePublishArtifactsAction := PgpKeys.publishSigned.value

//releasePublishArtifactsAction in ThisBuild := PgpKeys.publishSigned.value

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
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value
)

lazy val jxbLibs = "javax.xml.bind" % "jaxb-api" % "2.3.1"
lazy val scalaParser =
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.3.0"

lazy val tafdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.tafdata",
    moduleName := "add_transformers-tafdata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true
  )
  .settings(publishSettings)

lazy val metardata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.metardata",
    moduleName := "add_transformers-metardata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true
  )
  .settings(publishSettings)

lazy val pirepdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.pirepdata",
    moduleName := "add_transformers-pirepdata",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true
  )
  .settings(publishSettings)

lazy val aircraftreports = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.aircraftreports",
    moduleName := "add_transformers-aircraftreports",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true
  )
  .settings(publishSettings)

lazy val airsigmet = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.airsigmet",
    moduleName := "add_transformers-airsigmet",
    scalaxbIgnoreUnknown in (Compile, scalaxb) := true
  )
  .settings(publishSettings)
