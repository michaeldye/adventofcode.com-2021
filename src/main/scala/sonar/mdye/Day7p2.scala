package sonar.mdye

import scala.util.{Success, Try}

object Day7p2 {

  def calcCosts(init: Vector[Int], low: Int, high: Int): Try[Map[Int, Int]] = {
    val rr = low until high + 1

    // make tuples of (pos, [range])
    val ranged = init.map((_, rr))

    // calculate costs
    val costs: Vector[Map[Int, Int]] = ranged.map {
      case (pos: Int, rr: Range) =>
        rr.map { r: Int =>
          val distance = Math.abs(pos - r)
          // cost is now distance + sum of series to
          val penalty = (0 until distance).sum
          (r, distance + penalty)
        }.toMap
    }

    Success(rr.map { rx =>
        (rx, costs.foldLeft(0) { (acc, p) =>
          acc + p(rx)
        })
      }.toMap)
  }
}
