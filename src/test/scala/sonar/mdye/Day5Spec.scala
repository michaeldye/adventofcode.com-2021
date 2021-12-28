package sonar.mdye

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import sonar.mdye.Day5p1.P1SegmentFactory
import sonar.mdye.Day5p2.P2SegmentFactory

import scala.util.Success

class Day5Spec extends AnyFunSpec with Matchers {
  val navRaw: Vector[String] =
    """0,9 -> 5,9
      |8,0 -> 0,8
      |9,4 -> 3,4
      |2,2 -> 2,1
      |7,0 -> 7,4
      |6,4 -> 2,0
      |0,9 -> 2,9
      |3,4 -> 1,4
      |0,0 -> 8,8
      |5,5 -> 8,2
      |""".stripMargin.split("\n").toVector

  describe("Day5") {
    describe("parseSegments with P1SegmentFactory") {
      it("should produce vectors of segments") {

        val segs = Day5.parseSegments(navRaw, P1SegmentFactory.fromPoints) match {
          case Success(s) => s
          case _ => throw new AssertionError("failed to parse segments")
        }

        Day5.countOverlaps(segs) should be (Success(5))
      }
    }

    describe("parseSegments with P2SegmentFactory") {
      it("should produce vectors of segments") {

        val segs = Day5.parseSegments(navRaw, P2SegmentFactory.fromPoints) match {
          case Success(s) => s
          case _ => throw new AssertionError("failed to parse segments")
        }

        Day5.countOverlaps(segs) should be (Success(12))
      }
    }
  }
}
