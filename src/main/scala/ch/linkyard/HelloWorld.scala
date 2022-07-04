package ch.linkyard

import cats.implicits._
import cats.effect.cps._
import cats.effect._
import fs2.Chunk
import scala.concurrent.duration._

object HelloWorld {

  def getData(factor: Int): fs2.Stream[IO, Int] = {
    fs2.Stream.unfoldChunkEval(0) { offset =>
      readFromExternalSystem(factor)(offset, 50).map(users =>
        Option.when(users.nonEmpty)((Chunk.indexedSeq(users), offset + users.size))
      )
    }
  }

  def readFromExternalSystem(factor: Int)(offset: Int, limit: Int): IO[IndexedSeq[Int]] = {
    val realCount = 78
    IO.interruptibleMany {
      println(s"reading from external: ${factor}")
      (1 to 5).foreach(_ =>
        try {
          Thread.sleep(factor*100)
        } catch {
          case e: InterruptedException =>
            println("interrupted")
            throw e
          case t: Throwable => ()
        }
      )
      println(s"- done: ${factor}")
      (offset to Math.min(realCount, offset +limit)).toIndexedSeq
    }
  }

  def process(value: Int): IO[Unit] = {
    if (value == 42) IO.raiseError(new RuntimeException("Failed because 42"))
    else IO.sleep(value.millis)
  }

  def run(factor: Int): IO[String] = async[IO] {
    val data = getData(factor)
      .evalMap(process)
      .compile.toList.await
    IO.delay(s"Hello Cats: ${data.length}").await
  }
}
