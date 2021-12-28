package sonar.mdye

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.math.BigInt
import scala.util.Try

object Day6p2 extends App {

  def initSim(ages: Vector[Int]): Try[Map[Int, BigInt]] = {
    Try(ages.foldLeft(HashMap[Int, BigInt]()) { (acc, age) =>
      acc + (age -> (acc.getOrElse(age, BigInt(0)) + 1))
    })
  }

  def runSim(generations: Map[Int, BigInt], simDays: Int): Try[BigInt] = {

    @tailrec
    def runSim0(generations: Map[Int, BigInt], days: Int): Map[Int, BigInt] = {
      if (days == 0) generations
      else {
        var upGen = HashMap[Int, BigInt]()

        val fishesAt0 = generations.getOrElse(0, BigInt(0))
        upGen = upGen + (6 -> fishesAt0)
        upGen = upGen + (8 -> fishesAt0)

        for (age <- 1 until 9) {
          upGen = upGen + ((age-1) -> (generations.getOrElse(age, BigInt(0)) + upGen.getOrElse(age-1, BigInt(0))))
        }

        runSim0(upGen, days - 1)
      }
    }

    Try(runSim0(generations, simDays).foldLeft(BigInt(0)) { case (acc, (_, v)) =>
      acc + v
    })
  }
}