package sonar.mdye

import sonar.mdye.Day3.binaryToDouble

import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Try, Using}

object Day3p2 {
  def winners(vv: Vector[Vector[Int]]): Seq[Vector[Int]] = {
    // per-digit place (as ix) which value is most common
    vv(0).indices.map { ix =>
      val tot = vv.foldLeft(0) { (acc, v) =>
        if (v(ix) == 0) acc - 1
        else acc + 1
      }

      val mc = (if (tot < 0) 0 else 1)
      // returns a vector instead of a tuple b/c I don't know how to access a tuple by numbered index (as we do below)
      Vector[Int](mc, (1 ^ mc))
    }
  }

  @tailrec
  def remUntilSingle(dix: Int, vv: Vector[Vector[Int]], wix: Int): Vector[Int] = {
    // dix is the digit index, wix is the winner index (0 to look up the most common digit, 1 to look up the least common)

    val win = winners(vv)

    if (dix > win.size)
      throw new IllegalStateException("No more digits")
    else if (vv.size == 1)
      return vv(0) // found dat digit
    else {
      // recur
      remUntilSingle(dix + 1, vv.filter(_ (dix) == win(dix)(wix)), wix)
    }
  }

  def accumulate(reads: Vector[Vector[Int]]): (Double, Double) = (binaryToDouble(remUntilSingle(0, reads, 0)), binaryToDouble(remUntilSingle(0, reads, 1)))

  def rollup(ff: Vector[Vector[Int]]): Try[Int] = Try {
    val (oxy, co2) = accumulate(ff)
    (oxy * co2).toInt
  }
}

