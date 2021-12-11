package sonar.mdye

import sonar.mdye.Day3.binaryToDouble

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

object Day3p1 {

  def diagAccumulate(reads: Vector[Vector[Int]]): (Double, Double) = {

    val buff = ArrayBuffer[Int]()

    // assume all lines are same length
    for (p <- reads(0).indices) {
      val tot = reads.foldLeft(0) { (acc, v) =>
        if (v(p) == 0) acc - 1
        else acc + 1
      }

      buff += (if (tot < 0) 0 else 1)
    }

    val epsilon = buff.map(1 ^ _)

    (binaryToDouble(buff), binaryToDouble(epsilon))
  }

  def diagRollup(ff: Try[Vector[Vector[Int]]]): Try[Int] = {
    // note that error handling is done better in Day3p2's rollup method...

    ff match {
      case Success(it: Vector[Vector[Int]]) =>
        val (gamma, epsilon) = diagAccumulate(it)
        Success((gamma * epsilon).toInt)
      case Failure(th) => Failure(th)
    }
  }
}