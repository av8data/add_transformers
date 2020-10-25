val twitterReleaseVersion = "20.9.0"

addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % twitterReleaseVersion)
addSbtPlugin("org.scalaxb" % "sbt-scalaxb" % "1.8.0")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.21")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.6")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
