package sonar.redjohn

import scala.collection.mutable

import sonar.Runner

object Day3 extends App {
  case class PowerRates(gamma: Int, epsilon: Int) {
    def powerConsumption: Int = gamma * epsilon
  }
  case class LifeSupportRates(oxygen: Int, co2: Int)

  def calculateGammaAndEpsilon(report: Seq[String]): PowerRates = {
    val entryLength = report.head.size
    val counts: mutable.Map[Int, Map[Char, Int]] = mutable.Map.newBuilder
      .addAll(0.until(entryLength).map(col => (col, Map('0' -> 0, '1' -> 0))))
      .result
    report.foldLeft(counts) { (counts, entry) =>
      for ((char, idx) <- entry.zipWithIndex) {
        val countsForColumn = counts(idx)
        counts.addOne(idx, countsForColumn.updated(char, countsForColumn(char) + 1))
      }
      counts
    }
    val digits = 0.until(entryLength).map { col =>
      val countsForColumn = counts(col)
      val charsForColumn = countsForColumn.toSeq.sortBy(_._2).map(_._1)
      (charsForColumn(0), charsForColumn(1))
    }
    val (epsilon, gamma) = digits.unzip
    PowerRates(Integer.parseInt(gamma.mkString, 2), Integer.parseInt(epsilon.mkString, 2))
  }

  def processReportForPowerConsumption(report: Seq[String]): Int = {
    calculateGammaAndEpsilon(report).powerConsumption
  }

  def calculateLifeSupportRates(report: Seq[String]): LifeSupportRates = {
    ???
  }

  def processReportForLifeSupport(report: Seq[String]): Int = {
    0
  }

  Runner.solveOne(args, processReportForPowerConsumption)
}
