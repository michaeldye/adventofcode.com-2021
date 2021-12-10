package sonar.redjohn

import sonar.Runner

object Day2 extends App {
  def handleMovement(coordinates: (Int, Int), next: String) ={
    next.split(" ").toSeq match {
      case "up" +: movement +: _ => (coordinates._1, coordinates._2 - movement.toInt)
      case "down" +: movement +: _ => (coordinates._1, coordinates._2 + movement.toInt)
      case "forward" +: movement +: _ =>
        (coordinates._1 + movement.toInt, coordinates._2)
    }
  }

  def handleMovementCommands(commands: Seq[String]): Int = {
    val coords = commands.foldLeft((0, 0))(handleMovement)
    coords._1 * coords._2
  }

  def handleMovementAndAim(coordinates: (Int, Int, Int), next: String) ={
    val (x, y, aim) = coordinates
    next.split(" ").toSeq match {
      case "up" +: movement +: _ => (x, y, aim - movement.toInt)
      case "down" +: movement +: _ => (x, y, aim + movement.toInt)
      case "forward" +: movement +: _ =>
        (x + movement.toInt, y+(aim*movement.toInt), aim)
    }
  }

  def handleMovementAndAimCommands(commands: Seq[String]): Int = {
    val coords = commands.foldLeft((0, 0, 0))(handleMovementAndAim)
    coords._1 * coords._2
  }

  Runner.solve(
    args,
    handleMovementCommands,
    handleMovementAndAimCommands,
  )
}
