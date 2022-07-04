package ch.linkyard

import cats.implicits._
import cats.effect.IOApp
import cats.effect.IO

object Main extends IOApp.Simple {

  // This is your new "main"!
  def run: IO[Unit] = {
    (1 to 100).toList
      .traverse(_ => HelloWorld.run(1).race(HelloWorld.run(20)).attempt.flatMap(IO.println))
      .void
  }
}
