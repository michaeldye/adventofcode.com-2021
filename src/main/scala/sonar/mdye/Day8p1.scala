package sonar.mdye

import scala.util.{Failure, Success, Try}

object Day8p1 {
  def determinedOutputCodeCount(codes: Vector[Code]): Try[Int] = {
    val outCodes = codes.flatMap(_.outputs)
    val determined = outCodes.filter(_._2.size == 1)
    Success(determined.size)
  }

  def assembleCodes(entries: Vector[Entry]): Try[Vector[Code]] = Try(entries.map(CodeFactory.codeFromEntry))
}

object CodeFactory {
  def codeFromEntry(entry: Entry): Code = new Code(entry)
}

class Code(val entry: Entry) {
  val outputs: Vector[(String, Vector[Int])] = accByLength(entry.outputs)

  def accByLength(coll: Vector[String]): Vector[(String, Vector[Int])] = coll.foldLeft(Vector[(String, Vector[Int])]()) { (acc, code) =>
    codeByLength(code.length) match {
      case Success(v) => acc :+ (code, v)
      case _ => throw new IllegalStateException(s"Unable to use code $code: bogus length")
    }
  }

  def codeByLength(length: Int): Try[Vector[Int]] = {
    length match {
      case 2 => Success(Vector(1))
      case 3 => Success(Vector(7))
      case 4 => Success(Vector(4))
      case 5 => Success(Vector(2, 3, 5))
      case 6 => Success(Vector(0, 6, 9))
      case 7 => Success(Vector(8))
      case _ => Failure(new IllegalStateException(s"Unable to map code length: $length"))
    }
  }

}