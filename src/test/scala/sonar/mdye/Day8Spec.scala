package sonar.mdye

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class Day8Spec extends AnyFunSpec with Matchers {
  val linesRaw: String =
    """acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
      /be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
      /edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
      /fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
      /fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
      /aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
      /fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
      /dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
      /bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
      /egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
      /gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".stripMargin('/')

  def fromString(ss: String): Vector[String] = linesRaw.split("\n").toVector

  describe("Day8p1") {
    describe("toEntries") {
      val detCt = for {
        en <- Day8.toEntries(fromString(linesRaw))
        codes <- Day8p1.assembleCodes(en)
        ct <- Day8p1.determinedOutputCodeCount(codes)
      } yield ct

      it("should produce entries") {
        detCt should be(Success(26))
      }
    }
  }

  describe("Day8p2") {
    describe("deduceWiring") {
      val entries = Day8.toEntries(fromString(linesRaw)).get

      it("should produce expected input for example line") {
        val fin = new CodeLine(entries(0)).decodedInput.get
        fin(0) should be(8)
        fin(1) should be(5)
        fin(2) should be(2)
        fin(3) should be(3)
        fin(4) should be(7)
        fin(5) should be(9)
        fin(6) should be(6)
        fin(7) should be(4)
        fin(8) should be(0)
        fin(9) should be(1)
      }

      it("should produce expected output for example line") {
        val fout = new CodeLine(entries(0)).decodedOutput.get
        fout(0) should be(5)
        fout(1) should be(3)
        fout(2) should be(5)
        fout(3) should be(3)
      }

    }
  }
}
