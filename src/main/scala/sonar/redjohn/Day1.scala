package sonar.redjohn

import sonar.Runner

object Day1 extends App {
  def countIncreasing(vals: Seq[Int]): Int = {
    vals.foldLeft((0, Integer.MAX_VALUE)) { (acc: (Int,Int), n: Int) =>
      if (n > acc._2) (acc._1+1, n)
      else (acc._1, n)
    }._1
  }

  def countIncreasingWindows(vals: Seq[String]): Int = {
    val ints = vals.map(_.toInt)
    val windows = ints.lazyZip(ints.drop(1)).lazyZip(ints.drop(2)).map(_ + _ + _)
    countIncreasing(windows)
  }

  def countIncreasingMeasurements(vals: Seq[String]): Int = {
    countIncreasing(vals.map(_.toInt))
  }

  Runner.solve(args, countIncreasingMeasurements, countIncreasingWindows)
}

