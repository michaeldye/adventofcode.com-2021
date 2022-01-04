package sonar.mdye

import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day6 extends App {

  def fromFile(pathFromFile: String): Try[Vector[Int]] = Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
    file.getLines().flatMap(_.split(",")).map(Integer.parseInt).toVector
  })

  val res: Try[(String, String)] = for {
    pp <- Try(sys.env("sonar-day6-input"))
    initAges <- Day6.fromFile(pp)
    population <- Day6p1.runSim(initAges, 80)
    generations <- Day6p2.initSim(initAges)
    part2Population <- Day6p2.runSim(generations, 256)
  } yield {
    (s"Lanternfish total after 80 days: ${population.size}",
    s"Lanternfish total after 256 days: $part2Population")
  }

  res match {
    case Success(rep: (String, String)) =>
      println(rep._1)
      println(rep._2)
      sys.exit(0)
    case Failure(ex: Exception) =>
      System.err.println(s"Failure, $ex")
      sys.exit(2)
    case _ =>
      println(s"Problem $res")
      sys.exit(3)
  }
}