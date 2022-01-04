package sonar.mdye

import scala.io.Source
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try, Using}

object Day8 extends App {

  def fromFile(pathFromFile: String): Try[Vector[String]] = Using.resource(Source.fromFile(pathFromFile)) { file =>
    val dr = "([ \\w]+ \\| [ \\w]+)".r

    // won't bother extracting now, just ensure formatting of each line is good
    Try(file.getLines().toVector.map {
      case dr(mm: String) => mm
      case line => throw new IllegalArgumentException(s"Unable to parse line: $line")
    })
  }

  def toEntries(lines: Vector[String]): Try[Vector[Entry]] = {
    val (successes, errors) = lines.map(EntryFactory.instance).partition(_.isSuccess)

    if (errors.isEmpty) Success(successes.map(_.get))
    else Failure(new IllegalStateException(s"Failures processing entries: $errors"))
  }

  val res: Try[(String, String)] = for {
    pp <- Try(sys.env("day8-input"))
    lines <- fromFile(pp)
    entries <- toEntries(lines)
    codes <- Day8p1.assembleCodes(entries)
    ct <- Day8p1.determinedOutputCodeCount(codes)
    allLineCodes <- Try(Day8p2.integerPerOutputLine(entries))
  } yield {
    (s"Part1: determined code ct: ${ct}",
    s"Part2: determined output codes: ${allLineCodes.sum}")

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

case class Entry(inputs: Vector[String], outputs: Vector[String])

object EntryFactory {
  val dr: Regex = "([a-z]+) ?".r

  // TODO: may need to count lengths and determine if the record is legal
  def dataFromSubline(dd: String, ct: Int): Try[Vector[String]] = {
    val data = dr.findAllIn(dd).map(_.strip()).toVector

    if (data.size != ct) Failure(new IllegalStateException(s"Expected $ct data segments in $dd"))
    else Success(data)
  }

  def instance(line: String): Try[Entry] = {
    // ten inputs then a pipe, then 4 outputs
    line.split('|') match {
      case Array(ins, outs, _*) =>
        for {
          in <- dataFromSubline(ins, 10)
          out <- dataFromSubline(outs, 4)
        } yield Entry(in, out)
      case _ => Failure(new IllegalArgumentException(s"Unable to use $line"))
    }
  }
}