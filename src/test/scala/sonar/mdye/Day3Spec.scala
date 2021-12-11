package sonar.mdye

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

import scala.util.Success

class Day3Spec extends AnyFunSpec with Matchers {
  val canned: Vector[Vector[Int]] = Vector[Vector[Int]](
    Vector[Int](0,0,1,0,0),
    Vector[Int](1,1,1,1,0),
    Vector[Int](1,0,1,1,0),
    Vector[Int](1,0,1,1,1),
    Vector[Int](1,0,1,0,1),
    Vector[Int](0,1,1,1,1),
    Vector[Int](0,0,1,1,1),
    Vector[Int](1,1,1,0,0),
    Vector[Int](1,0,0,0,0),
    Vector[Int](1,1,0,0,1),
    Vector[Int](0,0,0,1,0),
    Vector[Int](0,1,0,1,0),
  )
  describe("Day3") {
    describe("binaryToDouble") {
      it("should convert a binary number in a vector to a double") {
        Day3.binaryToDouble(Vector[Int](1, 0, 1, 0, 1)) should equal(21)
      }
    }
  }

  describe(description="Day3p1") {
    describe("diagRollupP1") {
      it("should convert sample int lines to diag rollup value") {
        Day3p1.diagRollup(Success(canned)) should equal(Success(198))
      }
    }
  }

  describe(description="Day3p2") {
    describe("rollup") {
      it("should convert sample int lines to diag rollup value") {
        Day3p2.rollup(canned) should equal (Success(230))
      }
    }
  }
}
