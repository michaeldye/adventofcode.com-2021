package sonar.mdye

import scala.util.{Failure, Success, Try}

object Day4p1 {

  def playGame(moves: Seq[Int], boards: Seq[Board]): Try[(Int, Board)] = {
    // Try every move, in order, on all boards. Evaluate at end of each move
    // if there is a winning board and return the move that won and the
    // winning board

    moves.foreach { move: Int =>

      // mark the moves
      boards.foreach { b: Board =>
        b.mark(move)

        // return early if board is a winner
        if (b.isWinner(move)) return Success(move, b)
      }
    }

    Failure(new IllegalStateException("No winning board given moves"))
  }


}

