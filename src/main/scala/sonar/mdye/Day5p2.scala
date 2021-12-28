package sonar.mdye

import sonar.mdye.Day5.{Segment, SegmentFactory}

import scala.util.{Failure, Success, Try}

object Day5p2 {

  class P2Segment(val begin: Point, val end: Point) extends Segment(begin, end) {

    override def genRanges(): Iterable[Point] = {

      def orientation(b: Int, e: Int): (Int, Int, Boolean) = {
        if (b < e) (b, e, false)
        else if (b == e) (b, e, false)
        else (e, b, true)
      }

      def gen(aLow: Int, aHigh: Int, rev: Boolean, bSize: Int): Seq[Int] = {
        if (aLow == aHigh) {
          List.fill(bSize+1)(aLow)
        } else {
          val tr = aLow until aHigh+1
          if (rev) tr.reverse
          else tr
        }
      }

      val (xLow, xHigh, xRev) = orientation(begin.x, end.x)
      val (yLow, yHigh, yRev) = orientation(begin.y, end.y)

      val xr = gen(xLow, xHigh, xRev, (yHigh-yLow))
      var yr = gen(yLow, yHigh, yRev, (xHigh-xLow))

      (xr zip yr).map(tup => Point(tup._1, tup._2))
    }
  }

  object P2SegmentFactory extends SegmentFactory {
    def fromPoints(x1: String, y1: String, x2: String, y2: String): Try[Option[Segment]] = {
      points(x1, y1, x2, y2) match {
        // accept anything
        case Success((begin: Point, end: Point)) => Success(Some(new P2Segment(begin, end)))
        case _ => Failure(new IllegalStateException(s"Unable to create segment from input: $x1, $y1, $x2, $y2"))
      }
    }
  }

}