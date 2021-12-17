package sonar.mdye


import sonar.mdye.Day5p1.P1SegmentFactory
import sonar.mdye.Day5p2.P2SegmentFactory

import scala.collection.immutable.HashMap
import scala.util.{Failure, Success, Try}

object Day5 extends App {
  class SegmentFactory {
    def toI(s: String): Try[Int] = Try(Integer.parseInt(s))

    protected def points(x1: String, y1: String, x2: String, y2: String): Try[(Point, Point)] = {
      for {
        x1i <- toI(x1)
        y1i <- toI(y1)
        x2i <- toI(x2)
        y2i <- toI(y2)
      } yield {
        (Point(x1i, y1i), Point(x2i, y2i))
      }
    }
  }

  abstract class Segment(begin: Point, end: Point) {
    def genRanges(): Iterable[Point]
    val points: Iterable[Point] = genRanges()
    override def toString: String = s"begin: $begin, end: $end"
  }


  def parseSegments(lines: Vector[String], SegFactoryFn: (String, String, String, String) => Try[Option[Segment]]): Try[Vector[Segment]] = {
    val seg = "(\\d+),(\\d+) -> (\\d+),(\\d+)".r

    Try(lines.map {
      case seg(x1, y1, x2, y2) => SegFactoryFn(x1, y1, x2, y2)
      case _ => Failure(throw new IllegalStateException(s"Unable to parse line with regex $seg"))
    }.flatMap(_.get))
  }

  def countOverlaps(segments: Vector[Segment]): Try[Int] = {
    val reduced = reduceSegments(segments)
    val filtered = reduced.filter(_._2 >= 2)
    Try(filtered.size)
  }

  def reduceSegments(segments: Vector[Segment]): HashMap[Point, Int] = {
    segments.flatMap(_.points).foldLeft(HashMap[Point, Int]()) { (acc, point) =>
      val pct = acc.getOrElse(point, 0)
      acc + (point -> (pct + 1))
    }
  }

  val res: Try[(String, String)] = for {
    pp <- Try(sys.env("sonar-day5-input"))
    lines <- Day4.fromFile(pp)
    // horiz and vertical only
    p1Segments <- Day5.parseSegments(lines, P1SegmentFactory.fromPoints)
    p1Overlaps <- Day5.countOverlaps(p1Segments)
    // diagonal and horiz and vertical
    p2Segments <- Day5.parseSegments(lines, P2SegmentFactory.fromPoints)
    p2Overlaps <- Day5.countOverlaps(p2Segments)
  } yield {
    (s"Answer to Day 5, part 1 is definitely: $p1Overlaps",
    s"Answer to Day 5, part 2 is definitely: $p2Overlaps")
  }

  res match {
    case Success((part1: String, part2: String)) =>
      println(part1)
      println(part2)
      sys.exit(0)
    case Failure(ex: Exception) =>
      System.err.println(s"Failure, dawg: $ex")
      sys.exit(2)
    case _ =>
      println(s"Wrp $res")
      sys.exit(3)
  }
}

case class Point(x: Int, y: Int)