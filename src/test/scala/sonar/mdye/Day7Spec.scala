package sonar.mdye

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class Day7Spec extends AnyFunSpec with Matchers {
  val costIn: Vector[Int] = Vector[Int](16,1,2,0,4,2,7,1,2,14)

  val p1Costs: Map[Int, Int] = Day7p1.calcCosts(costIn, 0, 16).get
  val p2Costs: Map[Int, Int] = Day7p2.calcCosts(costIn, 0, 16).get

  describe("Day7p1") {
    describe("leastCost should select position 2 with cost 37") {
      Day7.leastCost(p1Costs) match {
        case Success((pos: Int, cost: Int)) =>
          pos should be (2)
          cost should be (37)
        case _ => throw new AssertionError("wurp")
      }
    }
  }

  describe("Day7p2") {
    describe("leastCost should select position 5 with cost 168") {
      Day7.leastCost(p2Costs) match {
        case Success((pos: Int, cost: Int)) =>
          pos should be (5)
          cost should be (168)
        case _ => throw new AssertionError("wurp")
      }
    }
  }

}
