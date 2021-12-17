package sonar.mdye

import scala.annotation.tailrec
import scala.collection.immutable.HashSet
import scala.util.{Failure, Success, Try}

object Day4p2 {

  def playGame(moves: Seq[Int], boards: Seq[Board]): Try[(Int, Board)] = {
    var winners = HashSet[Board]()

    moves.foreach { move: Int =>
      boards.foreach { b: Board =>
        b.mark(move)

        if (b.isWinner(move)) {
          winners = winners + b
          if (winners.size == boards.size) return Success(move, b)
        }
      }
    }

    Failure(new IllegalStateException("No winning board given moves"))
  }
}

