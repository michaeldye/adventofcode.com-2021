package sonar.mdye

import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Day4 extends App {

  // reusable
  def fromFile(pathFromFile: String): Try[Vector[String]] = Try(Using.resource(Source.fromFile(pathFromFile)) { file =>
    file.getLines().toVector
  })

  def movesAndBoards(lines: Vector[String]): Try[(Seq[Int], Seq[Board])] = {

    lines match {
      case first +: rest =>
        val moves = first.split(",").map(Integer.parseInt)
        val boards = toBoards(rest)

        Success((moves, boards))
      case _ => Failure(throw new IllegalStateException("Bogus lines input"))
    }
  }

  def toBoards(boardLines: Vector[String]): Vector[Board] = {

    @tailrec
    def coll(lines: Vector[String], boards: Vector[BoardBuilder]): Vector[BoardBuilder] = {
      if (lines.isEmpty) boards
      else coll(lines.drop(5), boards :+ lines.take(5).foldLeft(new BoardBuilder())(_.moar(_)))
    }

    coll(boardLines.filterNot(_.isEmpty), Vector[BoardBuilder]()).map(_.toBoard)
  }

  def determineScore(move: Int, winner: Board): Try[Int] = Try(move * winner.spaces.filterNot(_.visited).map(_.num).sum)


  // TODO: figure out what happens to failures from these for comprehensions

  for {
    pp <- Try(sys.env("sonar-day4-input"))
    lines <- Day4.fromFile(pp)
    (moves, boards) <- Day4.movesAndBoards(lines)
    (move, winningBoard) <- Day4p1.playGame(moves, boards)
    score <- Day4.determineScore(move, winningBoard)

    (d2move, d2winningBoard) <- Day4p2.playGame(moves, boards)
    d2score <- Day4.determineScore(d2move, d2winningBoard)
  } yield {
    println(s"Answer for day4, part 1 is definitely: $score")
    println(s"Answer for day4, part 2 is definitely: $d2score")
  }

}

class BoardBuilder() {

  private var buff = Array[Int]()

  def moar(line: String): BoardBuilder = {
    val ints: Array[Int] = line.split("\\s+").filter(_ != "").map { v => Integer.parseInt(v.trim) }
    buff = buff ++ ints
    this
  }

  def toBoard: Board = new Board(buff.toSeq)
}

class Board(private val seq: Seq[Int]) {

  val spaces: Seq[Space] = seq.map(new Space(_))

  def mark(v: Int): Unit = spaces.foreach { space: Space =>
    if (space.num == v) space.visit()
  }

  def isWinner(move: Int): Boolean = {

    // collect all of the indices for values matching the move (could be multiple)
    val matchIndices: Seq[Int] = spaces.zipWithIndex.filter(_._1.num == move).map(_._2)

    // takes two functions as args, one to calc the offset and the other to pass to List.tabulate to generate a range
    def seriesWin(offsetFn: Int => Int)(tabulateFn: Int => Int => Int): Boolean = {

      // check if any move index has a series win (meaning all spaces in the range are visited)
      matchIndices.exists { ix =>
        val series = List.tabulate(5)(tabulateFn(offsetFn(ix)))
        series.forall(ix => spaces(ix).visited)
      }
    }

    val rw = seriesWin(ix => ix / 5)(offset => ix => ix + 5 * offset)
    val cw = seriesWin(ix => ix % 5)(offset => ix => ix * 5 + offset)

    rw || cw
  }

  override def equals(obj: Any): Boolean = seq.equals(obj.asInstanceOf[Board].seq)

  override def hashCode(): Int = seq.hashCode()
}

class Space(val num: Int, var visited: Boolean = false) {

  def visit(): Unit = visited = true
}

