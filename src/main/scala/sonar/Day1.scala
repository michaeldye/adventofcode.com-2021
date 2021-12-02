package sonar

import scala.io.Source
import scala.util.Using

object Day1 extends App {

  def fromFile(pathFromFile: String): Vector[Int] = {

    Using.resource(Source.fromFile(pathFromFile)) { file =>
      file.getLines().map(Integer.parseInt).toVector
    }
  }

  def countIncreasing(vals: Vector[Int]): Int = {
    // count the # of increases

    vals.foldLeft((0, Integer.MAX_VALUE)) { (acc: (Int,Int), n: Int) =>

      if (n > acc._2) (acc._1+1, n)
      else (acc._1, n)
    }._1

  }

  sys.env.get("sonar-day1-input") match {
    case Some(pp) =>
      print(s"Your answer is definitely: ${countIncreasing(fromFile(pp))}")
    case None =>
      throw new IllegalStateException("Bogus env")
  }

}
