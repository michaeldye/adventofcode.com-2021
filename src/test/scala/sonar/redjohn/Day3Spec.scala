package sonar.redjohn

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

class Day3Spec extends AnyFunSpec with Matchers {
  describe("redjohn's Day3") {
    // example from problem description
    // solution is gamma = 22 and epsilon = 9
    val report = Seq(
      "00100",
      "11110",
      "10110",
      "10111",
      "10101",
      "01111",
      "00111",
      "11100",
      "10000",
      "11001",
      "00010",
      "01010",
    )

    describe("calculateGammaAndEpsilon") {
      it("calculates the gamma and epsilon rate") {
        Day3.calculateGammaAndEpsilon(report).gamma should equal(22)
        Day3.calculateGammaAndEpsilon(report).epsilon should equal(9)
      }
    }

    describe("processReportForPowerConsumption") {
      it("combines the rates") {
        Day3.processReportForPowerConsumption(report) should equal(198)
      }
    }

    describe("calculateLifeSupportRates") {
      it("calculates the oxygen generator and CO2 scrubber rates") {
        Day3.calculateLifeSupportRates(report).oxygen should equal(23)
      }
    }
  }
}

