package dill

import scala.BigDecimal
import scala.annotation.migration
import scala.util.parsing.combinator.JavaTokenParsers

class FeatureParser extends JavaTokenParsers {

  def parse(in: String) = {
    parseAll(dillParser, in)
  }

  def dillParser = featureNameParser ~ rep(scenarioParser) ^^ {
    case featureName ~ scenarios =>
      FeatureNode(featureName, scenarios)
  }

  def featureNameParser = "Feature:" ~> charSequenceParser

  def scenarioParser = "Scenario:" ~> charSequenceParser ~ opt(symbolsParser) ~ opt(dataTableParser) ^^ {
    case scanarioName ~ symbols ~ dataTable =>
      ScenarioNode(scanarioName, symbols, dataTable)
  }

  def symbolsParser = rep(nameValueParser)

  def upToLeftBraceParser = """[^{]+\{""".r
  def leftEqParser = """[^=]+""".r
  def rightEqParser = """[^}]+""".r

  //TODO - dvydra - try using polymorphism insted of asInstanceof!
  def nameValueParser = upToLeftBraceParser ~> leftEqParser ~ "=" ~ (decimalNumber | moneyParser | rightEqParser) ~ "}" ^^ {
    case left ~ eq ~ right ~ closingBrace =>
      val name = left //.split("=").head
      val value = right match {
        case MoneyNode(_) => right.asInstanceOf[MoneyNode].asJavaBigDecimal
        case _ => right
      }
      NameValueNode(name, value)
  }

  def moneyParser = "$" ~> """\d+[.]\d+""".r ^^ {
    MoneyNode(_)
  }

  def dataTableCellParser = "|" ~> """[^|]""".r ^^ {
    DataCellNode(_)
  }

  def dataTableRowParser = rep(dataTableCellParser) <~ "|" ^^ {
    case cells =>
      DataTableRowNode(cells.map(s => (s.value)))
  }

  def dataTableParser = rep(dataTableRowParser) ^^ {
    case rows =>
      DataTableNode(rows)
  }

  def charSequenceParser = """.+""".r

}

trait ASTNode

case class FeatureNode(name: String, scenarios: List[ScenarioNode]) extends ASTNode {
  val scenariosMap = scenarios.map(s => (s.name, s)).toMap

  def findScenario(name: String) = {
    scenariosMap.get(name)
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(name: String, symbols: Option[List[NameValueNode]], dataTable: Option[DataTableNode]) extends ASTNode {

  val symbolTable = symbols match {
    case Some(symbols) => symbols.map(s => (s.name, s.value)).toMap
    case None => Map.empty[String, Any]
  }

  def get(symbolName: String) = {
    symbolTable(symbolName)
  }

}

case class NameValueNode(name: String, value: Any) extends ASTNode

case class DataCellNode(value: String) extends ASTNode

case class DataTableRowNode(cellValues: List[String]) extends ASTNode

case class DataTableNode(rows: List[DataTableRowNode]) extends ASTNode

case class MoneyNode(amount: String) extends ASTNode {
  def asJavaBigDecimal() = {
    BigDecimal(amount).underlying
  }
}
