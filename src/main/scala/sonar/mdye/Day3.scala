package sonar.mdye

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day3 extends App {
  /*** methods here are reused between parts, otherwise they're in the parts' specific file ***/

  // previous processing yielded a binary representation of a number as Ints in a vector. We unwind that here
  def binaryToDouble(bin: Iterable[Int]): Double = bin.toVector.reverse.zipWithIndex.foldLeft(0d)((acc, n) => acc + Math.pow(2, n._2) * n._1)

  def fromFile(pathFromFile: String): Try[Vector[Vector[Int]]] = Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
    file.getLines().map { li: String =>
      li.toVector.map { ch: Char =>
        Integer.parseInt(ch.toString)
      }
    }.toVector
  })

  /*** run both parts in main() ***/

  // part 1
  sys.env.get("sonar-day3-input") match {
    // N.B. this is cleaned up in part 2
    case Some(pp) =>
      Day3p1.diagRollup(Day3.fromFile(pp)) match {
        case Success(i: Int) => println(s"Answer for day 3, part 1 is definitely: $i")
        case Failure(th: Throwable) => System.err.println(s"Failure calculating total; ", th)
      }
    case None => throw new IllegalStateException("Bogus env, processing halted")
  }


  // part 2
  for {
    pp <- Try(sys.env("sonar-day3-input"))
    in <- Day3.fromFile(pp)
    product <- Day3p2.rollup(in)
  } yield {
    println(s"Answer for day3, part 2 is definitely: $product")
  }
}
