package sonar

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day2 {

  def fromFile(pathFromFile: String): Try[Vector[String]] = {

    Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
      file.getLines().toVector
    })
  }


  // return value is going to be (horiz_pos_adjustment, depth_pos_adjustment) where either can be 0 and the second can be negative
  def toVal(dir: String, qt: String): Try[(Int, Int)] = {

    Try(Integer.parseInt(qt)) match {
      case Success(qt_i) =>
        if (dir == "down") Success(0, qt_i)
        else if (dir == "up") Success(0, Math.negateExact(qt_i))
        else if (dir == "forward") Success(qt_i, 0)
        else Failure(new IllegalStateException(s"Unknown direction, $dir"))
      case Failure(th) => Failure(th) // TODO: a better way to do this?
    }
  }

  def posTranslate(directions: Vector[String]): Vector[Try[(Int, Int)]] = {
    val Pattern = "(\\w+) (\\d+)".r

    directions.map {
      case Pattern(dir, qt) => toVal(dir, qt)
      case ln => Failure(new IllegalArgumentException(s"Unable to parse line $ln"))
    }
  }

  def directionTotal(ff: Try[Vector[String]]): Try[Int] = {
    // TODO: should pattern match here and let posTranslate assume good values, or pass the error handling through?
    ff match {
      case Success(v: Vector[String]) =>
        val (success, failures) = posTranslate(v).partition(_.isSuccess)
        if (failures.nonEmpty) Failure(new Exception(s"Bogus input lines $failures"))
        else {
          val summed = success.map(_.get).reduce((a,b) => ( a._1 + b._1, a._2 + b._2 ))
          Success(summed._1 * summed._2)
        }
      case Failure(th) => Failure(th) // TODO: a better way to do this?
    }
  }
}

object Day2p1 extends App {
  sys.env.get("sonar-day2-input") match {
    case Some(pp) =>
      Day2.directionTotal(Day2.fromFile(pp)) match {
        case Success(v: Int) => println(s"Answer for day 2, part 1 is definitely: $v")
        case Failure(th: Throwable) => System.err.println(s"Failure calculating total; ", th)
      }
    case None =>
      System.err.println("Bogus env, processing halted")
      sys.exit(1)
  }

}
