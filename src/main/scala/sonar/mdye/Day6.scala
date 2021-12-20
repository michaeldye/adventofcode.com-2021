package sonar.mdye

import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day6 extends App {

  def fromFile(pathFromFile: String): Try[Vector[Int]] = Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
    file.getLines().flatMap(_.split(",")).map(Integer.parseInt).toVector
  })

  def runSim(initAges: Vector[Int], simDays: Int): Try[Vector[Fish]] = {

    @tailrec
    def runSim0(school: Vector[Fish], days: Int): Vector[Fish] = {
      println(s"School size at sim day $days: ${school.size}")
      if (days == simDays) school
      else runSim0(school.flatMap(_.tick()) ++: school, days+1)
    }


    Try(runSim0(initAges.map(new Fish(_)).toVector, 0))
  }

  val res: Try[String] = for {
    pp <- Try(sys.env("sonar-day6-input"))
    initAges <- Day6.fromFile(pp)
    population <- Day6.runSim(initAges, 80)
  } yield {
    s"Lanternfish total after fixed days: ${population.size}"
  }

  res match {
    case Success(rep: String) =>
      println(rep)
      sys.exit(0)
    case Failure(ex: Exception) =>
      System.err.println(s"Failure, $ex")
      sys.exit(2)
    case _ =>
      println(s"Problem $res")
      sys.exit(3)
  }

}


class Fish(var timer: Int) {

  def tick(): Option[Fish] = {
    if (timer == 0) {
      timer = 6
      Some(new Fish(8))
    } else {
      timer -= 1
      None
    }
  }
}