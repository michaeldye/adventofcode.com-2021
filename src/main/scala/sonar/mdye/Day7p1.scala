package sonar.mdye

import scala.util.{Success, Try}

object Day7p1 {

  def calcCosts(init: Vector[Int], low: Int, high: Int): Try[Map[Int, Int]] = {
    /* generate map of ( pos -> Map ( possibleMin -> cost ))
     */

    // make tuples of (pos, [range])
    val ranged = init.map((_, low until high + 1))

    // calculate costs
    val costs: Vector[Map[Int, Int]] = ranged.map {
      case (pos: Int, rr: Range) =>
        rr.map { r: Int =>
          (r, Math.abs(pos - r))
        }.toMap
    }

    // val costsOnly = costs.map(_.keys.toVector)

    val aggregated = (low until high + 1).map { rx =>
        (rx, costs.foldLeft(0) { (acc, p) =>
          acc + p(rx)
        })
      }.toMap

    Success(aggregated)
  }
}
