package sonar

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day02Part1_GP {
  def handleMovement(x_y_accumulator: (Int, Int), next: String) ={
    next.split(" ").toSeq match {
        case "up" +: movement +: _ => (x_y_accumulator._1, x_y_accumulator._2 - movement.toInt)
        case "down" +: movement +: _ => (x_y_accumulator._1, x_y_accumulator._2 + movement.toInt)
        case "forward" +: movement +: _ =>
          (x_y_accumulator._1 + movement.toInt, x_y_accumulator._2)
      }
  }
  def handleMovementCommands(commands: Seq[String]): Int = {
    val coords = commands.foldLeft((0, 0))(handleMovement)
    coords._1 * coords._2
  }
}

object Day02Part2_GP {
  def handleMovement(x_y_aim: (Int, Int, Int), next: String) ={
    val (x, y, aim) = x_y_aim
    next.split(" ").toSeq match {
        case "up" +: movement +: _ => (x, y, aim - movement.toInt)
        case "down" +: movement +: _ => (x, y, aim + movement.toInt)
        case "forward" +: movement +: _ =>
          (x + movement.toInt, y+(aim*movement.toInt), aim)
      }
  }
  def handleMovementCommands(commands: Seq[String]): Int = {
    val coords = commands.foldLeft((0, 0, 0))(handleMovement)
    coords._1 * coords._2
  }
}

object Day2 {

  def fromFile(pathFromFile: String): Try[Vector[String]] = {

    Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
      file.getLines().toVector
    })
  }

  // return value is going to be (horiz_pos_adjustment, depth_pos_adjustment, aim_pos_adjustment, aim_pos_multiplier) where either can be 0 and the second can be negative
  def toVal(dir: String, qt: String): Try[(Int, Int, Int, Int)] = {

    Try(Integer.parseInt(qt)) match {
      case Success(qt_i) =>
        if (dir == "down") Success(0, 0, qt_i, 0)
        else if (dir == "up") Success(0, 0, Math.negateExact(qt_i), 0)
        else if (dir == "forward") Success(qt_i, 0, 0, qt_i)
        else Failure(new IllegalStateException(s"Unknown direction, $dir"))
      case Failure(th) => Failure(th) // TODO: a better way to do this?
    }
  }

  def posTranslate(directions: Vector[String]): Vector[Try[(Int, Int, Int, Int)]] = {
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
          // we don't care about accumulating the fourth value in the tuple we get from posTranslate (it's a multiplier for depth, not a value useful on its own)
          val accumulated = success.map(_.get).foldLeft((0, 0, 0)) { (acc, vv) =>
            val hpa_acc = acc._1
            val dpa_acc = acc._2
            val apa_acc = acc._3

            val hpa = vv._1
            val dpa = vv._2
            val apa = vv._3
            val apm = vv._4

            // unless the direction tuple processed has a non-zero value for apm (the multiplier), dpa_acc is unaffected by this
            val dpa_adjustment = apa_acc * apm

            (hpa_acc + hpa, dpa_acc + dpa + dpa_adjustment, apa_acc + apa)
          }

          // we ignore the third accumulated value
          Success(accumulated._1 * accumulated._2)
        }
      case Failure(th) => Failure(th) // TODO: a better way to do this?
    }
  }
}

object Day2mdye extends App {
  sys.env.get("sonar-day2-input") match {
    case Some(pp) =>
      Day2.directionTotal(Day2.fromFile(pp)) match {
        case Success(v: Int) => println(s"Answer for day 2, part 2 is definitely: $v")
        case Failure(th: Throwable) => System.err.println(s"Failure calculating total; ", th)
      }
    case None =>
      System.err.println("Bogus env, processing halted")
      sys.exit(1)
  }

}

object Day2gp extends App {
  Runner.solve(
    args,
    Day02Part1_GP.handleMovementCommands,
    Day02Part2_GP.handleMovementCommands,
  )
}
