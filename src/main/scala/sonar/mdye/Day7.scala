package sonar.mdye

import scala.util.{Failure, Success, Try}

object Day7 extends App {
  // may be different than real max int eventually (if we decide a way to guess at a cutoff for searching possible winners)
  def limits(initPos: Vector[Int]): Try[(Int, Int)] = Success(initPos.min, initPos.max)

  def leastCost(costs: Map[Int, Int]): Try[(Int, Int)] = Try(costs.toList.minBy(_._2))

  val res: Try[(String, String)] = for {
    pp <- Try(sys.env("day7-input"))
    initPos <- Day6.fromFile(pp) // reuse day6 input
    (low, high) <- Day7.limits(initPos)
    costsPart1 <- Day7p1.calcCosts(initPos, low, high)
    costsPart2 <- Day7p2.calcCosts(initPos, low, high)
    (positionPart1, totalPart1) <- Day7.leastCost(costsPart1)
    (positionPart2, totalPart2) <- Day7.leastCost(costsPart2)
  } yield {
    (s"Part1: optimal position for crabz is $positionPart1; total cost is: $totalPart1",
      s"Part2: optimal position for crabz is $positionPart2; total cost is: $totalPart2")
  }

  res match {
    case Success((p1: String, p2: String)) =>
      println(p1)
      println(p2)
      sys.exit(0)
    case Failure(ex: Exception) =>
      System.err.println(s"Failure, $ex")
      sys.exit(2)
    case _ =>
      println(s"Problem $res")
      sys.exit(3)
  }
}
