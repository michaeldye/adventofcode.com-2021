package sonar.mdye

import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day6p1 extends App {

  def runSim(initAges: Vector[Int], simDays: Int): Try[Iterable[Fish]] = {

    @tailrec
    def runSim0(school: Iterable[Fish], days: Int): Iterable[Fish] = {
      println(s"School size at sim day $days: ${school.size}")
      if (days == simDays) school
      else runSim0(school.flatMap(_.tick()) ++ school, days+1)
    }

    Try(runSim0(initAges.map(new Fish(_)), 0))
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