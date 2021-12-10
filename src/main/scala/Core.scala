package sonar

import scala.util.{Failure, Success, Try}
import java.io.{File, FileNotFoundException}
import scala.io.Source
import scala.util.Using

object AOCCliParser {

  case class CliArgConfig(challInput: File = new File("."))

  def parseCli(args: Array[String]): Try[CliArgConfig] = {
    import scopt.OParser

    val builder = OParser.builder[CliArgConfig]
    val parser = {
      import builder._
      OParser.sequence(
        opt[File]('i', "challInput")
          .required()
          .valueName("<challenge-input-file-path>")
          .action((x, c) => c.copy(challInput = x))
          .text("challInput is a required file property")
          .validate { file => if (file.exists) success else failure(s"$file does not exist") },
      )
    }

    val parsed = OParser.parse(parser, args, CliArgConfig())
    parsed match {
      case Some(conf) => Success(conf)
      case _ =>
        Failure(new RuntimeException("couldnt parse CLI args into known config"))
    }
  }
}

object FileParser {
  def readFile(filePath: File): Try[Seq[String]] = {
    Using(Source.fromFile(filePath)) { _.getLines.toSeq }
  }
}


object Runner {
  type Solution = Seq[String] => Any
  val NotImplemented: Solution = (_ => "Not Implemented")

  def solveOne(args: Array[String], p1: Solution) = solve(args, p1, NotImplemented)

  def solve(args: Array[String], p1: Solution, p2: Solution) = {
    val result = for {
      cli <- AOCCliParser.parseCli(args)
      file <- FileParser.readFile(cli.challInput)
    } yield {
      s"p1: ${p1(file)}\np2: ${p2(file)}"
    }
    println(result.get)
  }
}
