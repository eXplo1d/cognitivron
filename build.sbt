name := "cognitivron"

version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion = "2.6.16"
val LogBackVersion = "1.2.6"
val ScalaTestVersion = "3.2.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %%"akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % LogBackVersion,
  "org.scalactic" %% "scalactic" % ScalaTestVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
)