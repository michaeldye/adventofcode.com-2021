package sonar.redjohn

import scala.annotation.tailrec
import scala.collection.mutable

import sonar.Runner

object Day3 extends App {
  case class PowerRates(gamma: Int, epsilon: Int) {
    val powerConsumption: Int = gamma * epsilon
  }
  case class LifeSupportRates(o2: Int, co2: Int) {
    val rating: Int = o2 * co2
  }

  sealed trait Bit
  case object Zero extends Bit
  case object One extends Bit

  sealed trait Commonality {
    def compare(x: Int, y: Int): Boolean
  }
  case object Most extends Commonality {
    override def compare(x: Int, y: Int): Boolean = x > y
  }
  case object Least extends Commonality {
    override def compare(x: Int, y: Int): Boolean = x < y
  }

  sealed trait LifeSupportCriteria {
    def commonality: Commonality
    def tiebreaker: Bit
  }
  case object OxygenCriteria extends LifeSupportCriteria {
    override val commonality = Most
    override val tiebreaker = One
  }
  case object CarbonDioxideCriteria extends LifeSupportCriteria {
    override val commonality = Least
    override val tiebreaker = Zero
  }

  private def countColumnCharacters(report: Seq[String]): mutable.Map[Int, Map[Char, Int]] = {
    val entryLength = report.head.size
    val counts: mutable.Map[Int, Map[Char, Int]] = mutable.Map.newBuilder
      .addAll(0.until(entryLength).map(col => (col, Map('0' -> 0, '1' -> 0))))
      .result

    for (entry <- report) {
      for ((char, idx) <- entry.zipWithIndex) {
        val countsForColumn = counts(idx)
        counts.addOne(idx, countsForColumn.updated(char, countsForColumn(char) + 1))
      }
    }

    counts
  }

  def calculateGammaAndEpsilon(report: Seq[String]): PowerRates = {
    val entryLength = report.head.size
    val counts = countColumnCharacters(report)
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

  @tailrec
  def filterOnBits(report: Seq[String], criteria: LifeSupportCriteria, column: Int = 0): String = {
    val (zeroes, ones) = report.partition(x => x(column) == '0')
    val next = if (zeroes.size == ones.size) {
      criteria.tiebreaker match {
        case Zero => zeroes
        case One => ones
      }
    } else {
      if (criteria.commonality.compare(zeroes.size, ones.size)) zeroes
      else ones
    }

    if (next.size == 1) next.head
    else filterOnBits(next, criteria, column + 1)
  }

  def calculateLifeSupportRates(report: Seq[String]): LifeSupportRates = {
    LifeSupportRates(
      Integer.parseInt(filterOnBits(report, OxygenCriteria), 2),
      Integer.parseInt(filterOnBits(report, CarbonDioxideCriteria), 2),
    )
  }

  def processReportForLifeSupport(report: Seq[String]): Int = {
    calculateLifeSupportRates(report).rating
  }

  Runner.solve(args, processReportForPowerConsumption, processReportForLifeSupport)
}
