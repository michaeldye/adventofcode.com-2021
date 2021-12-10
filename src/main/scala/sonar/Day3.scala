package sonar

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day3 {

  def fromFile(pathFromFile: String): Try[Vector[Vector[Int]]] = {

    Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
      file.getLines().map { li: String =>
        li.toVector.map { ch: Char =>
          Integer.parseInt(ch.toString)
        }
      }.toVector
    })
  }

  def binaryToDouble(bin: Iterable[Int]): Double = {
    // previous processing yielded a binary representation of a number as Ints in a vector. We unwind that here

    bin.toVector.reverse.zipWithIndex.foldLeft(0d) { (acc: Double, n: (Int, Int)) =>
      val nn: Int = n._1
      val ix: Int = n._2

      acc + Math.pow(2, ix) * nn
    }
  }

  def diagAccumulate(reads: Vector[Vector[Int]]): (Double, Double) = {

    val buff = ArrayBuffer[Int]()

    // assume all lines are same length
    for (p <- reads(0).indices) {
      val tot = reads.foldLeft(0) { (acc, v) =>
        if (v(p) == 0) acc - 1
        else acc + 1
      }

      buff += (if (tot < 0) 0 else 1)
    }

    val epsilon = buff.map(1 ^ _)

    (binaryToDouble(buff), binaryToDouble(epsilon))
  }

  def diagRollup(ff: Try[Vector[Vector[Int]]]): Try[Int] = {
    // TODO: should pattern match here and let posTranslate assume good values, or pass the error handling through?
    ff match {
      case Success(it: Vector[Vector[Int]]) =>
        val (gamma, epsilon) = diagAccumulate(it)
        Success((gamma * epsilon).toInt)
      case Failure(th) => Failure(th) // TODO: a better way to do this?
    }
  }
}

object Day3p1 extends App {
  sys.env.get("sonar-day3-input") match {
    case Some(pp) =>
      Day3.diagRollup(Day3.fromFile(pp)) match {
        case Success(i: Int) => println(s"Answer for day 3, part 1 is definitely: $i")
        case Failure(th: Throwable) => System.err.println(s"Failure calculating total; ", th)
      }
    case None =>
      System.err.println("Bogus env, processing halted")
      sys.exit(1)
  }

}
