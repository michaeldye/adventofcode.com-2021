package sonar.mdye

import sonar.mdye.Day5.{Segment, SegmentFactory}

import scala.util.{Failure, Success, Try}

object Day5p1 {

  class P1Segment(val begin: Point, val end: Point) extends Segment(begin, end) {

    override def genRanges(): Iterable[Point] = {
      def rr(a: Int, b: Int): Range = if (a > b) (b until a + 1) else (a until b + 1)

      def fill(aa: Range, bb: Range, a: Int, b: Int): Seq[(Int, Int)] = {
        if (aa.size == 1) List.fill(bb.size)(a) zip bb
        else aa zip List.fill(aa.size)(b)
      }

      val xr = rr(begin.x, end.x)
      val yr = rr(begin.y, end.y)

      fill(xr, yr, begin.x, begin.y).map(tup => Point(tup._1, tup._2))
    }
  }

  object P1SegmentFactory extends SegmentFactory {
    def fromPoints(x1: String, y1: String, x2: String, y2: String): Try[Option[Segment]] = {
      points(x1, y1, x2, y2) match {
        case Success((begin: Point, end: Point)) =>
          if (begin.x == end.x || begin.y == end.y) Success(Some(new P1Segment(begin, end)))
          else Success(None)
        case _ => Failure(new IllegalStateException(s"Some bullshit happened"))
      }
    }
  }

}