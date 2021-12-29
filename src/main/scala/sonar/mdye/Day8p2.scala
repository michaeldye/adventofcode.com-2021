package sonar.mdye

import scala.collection.immutable.HashMap
import scala.util.{Failure, Success, Try}


object Day8p2 {

  // very un-scala-y, we throw if we get any processing error
  def integerPerOutputLine(entries: Vector[Entry]): Vector[Int] = {

    entries.map { li =>
      new CodeLine(li).decodedOutput match {
        case Success(ints) => Integer.parseInt(ints.mkString("")) // probably the dumbest thing they could've done with this data
        case Failure(f) => throw new IllegalStateException(s"Error decoding line $li. Cause: $f") // throw if we encounter any error
      }
    }
  }
}

class CodeLine(val entry: Entry) {
  val decoder: Try[Decoder] = new WireAssignmentDiviner(entry).toDecoder

  val decodedOutput: Try[Vector[Int]] = tryDecode(entry.outputs)
  val decodedInput: Try[Vector[Int]] = tryDecode(entry.inputs)

  def tryDecode(data: Vector[String]): Try[Vector[Int]] = {
    decoder match {
      case Success(d) => Success(data.map(d.decode))
      case Failure(f) => Failure(f)
    }
  }
}

trait Decoder {
  def decode(s: String): Int
}

object Wiring {
  def codeToSegments(s: String): Vector[Char] = s.toLowerCase.toVector.sorted

  def unionSegments(s1: String, s2: String): String = codeToSegments(s1).concat(codeToSegments(s2)).distinct.mkString

  def diffSegments(v1: Vector[Char], v2: Vector[Char]): Vector[Char] = v1.diff(v2)
}

class WireAssignmentDiviner(private val entry: Entry) {

  // mapping is a code to Vector of possible ints for that code
  private var mapping = entry.inputs.foldLeft(Vector[(String, Vector[Int])]()) { (acc, code) =>

    def byLen(length: Int): Vector[Int] = {
      length match {
        case 2 => Vector(1)
        case 3 => Vector(7)
        case 4 => Vector(4)
        case 5 => Vector(2, 3, 5)
        case 6 => Vector(0, 6, 9)
        case 7 => Vector(8)
        case _ => throw new IllegalStateException(s"Unable to map code length: $length")
      }
    }

    acc :+ (code, byLen(code.length))
  }.toMap

  def deduceMapping(): Unit = {
    // derive 5
    val pSegsInFive = segments(5)(x => x._2 == 1)
    val segInFive = segment(6)(x => pSegsInFive.contains(x._1) && x._2 == 3)
    val fiveCode = codeByLength(5, x => x.contains(segInFive))

    updateMapping(fiveCode, 5)

    // derive 9
    val oneCode = code(1)
    val segsInNine = Wiring.codeToSegments(Wiring.unionSegments(fiveCode, oneCode))
    val nineCode = code(9, { x => Wiring.codeToSegments(x._1) == segsInNine })

    updateMapping(nineCode, 9)

    // derive 6
    val segsInEight = segments(7)(x => x._2 == 1)
    val segInSix = Wiring.diffSegments(segsInEight, segsInNine)(0)
    val sixCode = Wiring.unionSegments(s"$segInSix", fiveCode)

    updateMapping(sixCode, 6)

    // derive 3
    val threeCode = code(3, { x => Wiring.diffSegments(segsInNine, Wiring.codeToSegments(x._1)).size == 1 })

    updateMapping(threeCode, 3)
  }

  def toDecoder: Try[Decoder] = {
    // assigns member "mapping"
    deduceMapping()

    val asg = mapping.map { case (k, v) =>
      if (v.size != 1)
        return Failure(new IllegalStateException(s"Could not determine complete mapping for $k"))
      else k -> v(0)
    }

    if ((0 until 10).diff(asg.values.toVector).nonEmpty) Failure(new IllegalStateException(s"Attempt to build decoder with incomplete mapping, $mapping"))
    else Success(new OutputDecoder(asg))
  }

  def updateMapping(code: String, digit: Int): Unit = {
    // updates internal decoder state with complete mapping of code string -> int

    // remove digit from possible matches (value in map)
    mapping = mapping.map { x => x._1 -> x._2.filterNot(_ == digit) }

    // remove code from mapping altogether if we're going to add the mapping
    mapping = mapping.filterNot(x => Wiring.codeToSegments(x._1) == Wiring.codeToSegments(code))

    mapping += (code -> Vector(digit))
  }

  def segments(inputLength: Int)(filterPred: ((Char, Int)) => Boolean): Vector[Char] = {
    val restr = entry.inputs.filter {
      _.length == inputLength
    }

    val chars = restr.flatMap(Wiring.codeToSegments)

    // build a frequency map for segments
    chars.foldLeft(HashMap[Char, Int]()) { (acc, v) =>
      val nv = acc.getOrElse(v, 0) + 1
      acc + (v -> nv)
    }.filter(filterPred).keys.toVector
  }

  def segment(inputLength: Int)(filterPred: ((Char, Int)) => Boolean): Char = singleOrThrow(segments(inputLength)(filterPred))

  def code(value: Int): String = code(value, _ => true)

  def code(value: Int, filterPred: ((String, Vector[Int])) => Boolean): String = singleOrThrow(mapping.filter(x => x._2.contains(value)).filter(filterPred).keys.toVector)

  def codeByLength(length: Int, filterPred: String => Boolean): String = singleOrThrow(entry.inputs.filter(_.length == length).filter(filterPred))

  def singleOrThrow[T <: Any](v: Vector[T]): T = {
    if (v.size == 1)v(0)
    else throw new IllegalStateException("Expected single value in $v")
  }

  class OutputDecoder(private val asg: Map[String, Int]) extends Decoder {
    // store keys in mapping (the string code) as a sorted vector of chars
    private val mapping = asg.map(x => Wiring.codeToSegments(x._1) -> x._2)

    def decode(code: String): Int = {
      mapping.get(Wiring.codeToSegments(code)) match {
        case Some(x) => x
        case None => throw new IllegalStateException(s"code $code cannot be found in mapping, $mapping")
      }
    }
  }
}