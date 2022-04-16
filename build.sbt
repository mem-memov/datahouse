name         := "datahouse"
organization := "memmemov"
version      := "0.0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.scalafx"   %% "scalafx"   % "17.0.1-R26",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test" //http://www.scalatest.org/download
)
libraryDependencies ++= javaFXModules

// Add JavaFX dependencies
lazy val javaFXModules = {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _                            => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web").map( m=>
    "org.openjfx" % s"javafx-$m" % "17.0.1" classifier osName
  )
}

// Fork a new JVM for 'run' and 'test:run' to avoid JavaFX double initialization problems
fork := true

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
Compile / run / mainClass := Some("memmemov.datahouse.ScalaFXHelloWorld")

// JSON library
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

// refined types
libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"                 % "0.9.28",
  "eu.timepit" %% "refined-cats"            % "0.9.28", // optional
)

// config library
libraryDependencies += "is.cir" %% "ciris" % "2.3.2"
libraryDependencies += "is.cir" %% "ciris-refined" % "2.3.2"

// IO library
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.9"

// HTTP library
val Http4sVersion = "1.0.0-M31"
libraryDependencies += "org.http4s" %% "http4s-ember-server" % Http4sVersion
libraryDependencies += "org.http4s" %% "http4s-ember-client" % Http4sVersion
libraryDependencies += "org.http4s" %% "http4s-dsl" % Http4sVersion

// Stream library
libraryDependencies += "co.fs2" %% "fs2-core" % "3.2.5"