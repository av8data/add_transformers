import sbt.Keys._
import sbt._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"

lazy val supportedScalaVersions = List(scala212, scala213)

inThisBuild(
  List(
    crossScalaVersions := supportedScalaVersions,
    scalaVersion := scala213,
    organization := "com.av8data",
    homepage := Some(url("https://av8data.com")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "mattav8data",
        "Matthew Dickinson ",
        "matt@av8data.com",
        url("https://av8data.com")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        browseUrl = url("https://github.com/av8data/add_transformers"),
        connection = "https://github.com/av8data/add_transformers.git"
      )
    ),
    publishTo := Some(
      "releases" at "https://oss.sonatype.org/" + "service/local/staging/deploy/maven2"),
  )
)

inThisBuild(
  List(
    scalafixScalaBinaryVersion := "2.13"
  ))

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

(sys.env.get("SONATYPE_USERNAME"), sys.env.get("SONATYPE_PASSWORD")) match {
  case (Some(username), Some(password)) =>
    println(s"Using credentials: $username/$password")
    credentials += Credentials(
      realm = "Sonatype Nexus Repository Manager",
      host = "oss.sonatype.org",
      userName = username,
      passwd = password)
  case _ =>
    println("USERNAME and/or PASSWORD is missing, using local credentials")
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
}

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Compile := true,
  publishArtifact in Test := false,
  autoAPIMappings := true
)

credentials += Credentials(
  realm = "GnuPG Key ID",
  host = "gpg",
  userName = "B98040CCE81E1A52F3358BED4391E8775B145A81", // key identifier
  passwd = "ignored"
)

pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray())

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
  releaseStepCommand("sonatypeRelease"),
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
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  scalaxbIgnoreUnknown in (Compile, scalaxb) := true
)

lazy val jxbLibs = "javax.xml.bind" % "jaxb-api" % "2.3.1"
lazy val scalaParser =
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "2.0.1"

lazy val root = (project in file("."))
  .settings(publishLocal := {}, publish := {}).aggregate(
    aircraftreports,
    airsigmet,
    metardata,
    pirepdata,
    tafdata)

lazy val aircraftreports = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.aircraftreports",
    moduleName := "add_transformers-aircraftreports",
    description := "AIREP data representation in scalaxb"
  )
  .settings(publishSettings)

lazy val airsigmet = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.airsigmet",
    moduleName := "add_transformers-airsigmet",
    description := "AIRSIGMENT data representation in scalaxb"
  )
  .settings(publishSettings)

lazy val metardata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.metardata",
    moduleName := "add_transformers-metardata",
    description := "METAR data representation in scalaxb"
  )
  .settings(publishSettings)

lazy val pirepdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.pirepdata",
    moduleName := "add_transformers-pirepdata",
    description := "PIREP data representation in scalaxb"
  )
  .settings(publishSettings)

lazy val tafdata = project
  .enablePlugins(ScalaxbPlugin, GitVersioning, GitBranchPrompt)
  .settings(sharedSettings)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.av8data.add_transformers.tafdata",
    moduleName := "add_transformers-tafdata",
    description := "TAF data representation in scalaxb"
  )
  .settings(publishSettings)

