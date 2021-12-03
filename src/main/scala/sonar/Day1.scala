package sonar

import scala.io.Source
import scala.util.Using

object Day1 {

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

  def windowedInput(vals: Vector[Int]): Vector[Int] = {
    // solve day 1, part 2: reduce vals into groups of 3 w/ overlaps of 2 (see https://adventofcode.com/2021/day/1#part2)
    vals.sliding(3, 1).map(_.sum).toVector

  }

  def getMeasurementWindows(vals: Vector[Int]): Vector[(Int, Int, Int)] = {
    (vals, vals.drop(1), vals.drop(2)).zipped.toVector
  }
}

object Day1p1 extends App {
  sys.env.get("sonar-day1-input") match {
    case Some(pp) =>
      println(s"Answer for day 1, part 1 is definitely: ${Day1.countIncreasing(Day1.fromFile(pp))}")
      println(s"Answer for day 1, part 2 (mdye) is definitely: ${Day1.countIncreasing(Day1.windowedInput(Day1.fromFile(pp)))}")
    case None =>
      throw new IllegalStateException("Bogus env")
  }
}

object Day1p2 extends App {
  sys.env.get("sonar-day1-input") match {
    case Some(pp) =>
      val measurements = Day1.getMeasurementWindows(Day1.fromFile(pp)).map {
        case (x, y, z) => x + y + z
      }
      println(s"Answer for day 1, part 2 (redjohn) is definitely: ${Day1.countIncreasing(measurements)}")
    case None =>
      throw new IllegalStateException("Bogus env")
  }
}

