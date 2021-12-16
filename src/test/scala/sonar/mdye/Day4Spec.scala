package sonar.mdye

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class Day4Spec extends AnyFunSpec with Matchers {
  val movesRaw: String = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"

  val boardRaw: Vector[String] =
    """
      |
      |22 13 17 11  0
      | 8  2 23  4 24
      |21  9 14 16  7
      | 6 10  3 18  5
      | 1 12 20 15 19
      |
      | 3 15  0  2 22
      | 9 18 13 17  5
      |19  8  7 25 23
      |20 11 10 24  4
      |14 21 16 12  6
      |
      |""".stripMargin.split("\n").toVector

  val allRaw: Vector[String] = movesRaw +: boardRaw

  describe("Day4") {

    describe("toBoards") {
      it("should produce vectors of boards from lines of strings") {
        val boards = Day4.toBoards(boardRaw)
        boards.size should equal(2)

        for (b <- boards) {
          b.spaces.size should equal(25)
          b.spaces.count(_.visited) should equal(0)
        }
      }
    }

    describe("movesAndBoards") {
      it("should produce moves and boards from line input") {
        Day4.movesAndBoards(allRaw) match {
          case Success((moves: Seq[Int], boards: Seq[Board])) =>
            moves.size should equal(27)
            boards.size should equal(2)
          case _ => throw new IllegalStateException("Didn't match")
        }
      }

    }
  }

}
