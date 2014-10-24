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

  def scenarioParser = "Scenario:" ~> charSequenceParser ~ symbolsParser ~ opt(dataTableParser) ^^ {
    case scanarioName ~ symbols ~ dataTable =>
      ScenarioNode(scanarioName, symbols, dataTable)
  }

  def symbolsParser = rep(nameValueParser)

  def upToLeftBraceParser = """(?!Scenario)[^{]+\{""".r
  def leftEqParser = """[^=]+""".r
  def rightEqParser = """[^}]+""".r

  //TODO - dvydra - try using polymorphism insted of asInstanceof!
  def nameValueParser = upToLeftBraceParser ~> leftEqParser ~ "=" ~ (decimalNumber | moneyParser | rightEqParser) ~ "}" ^^ {
    case left ~ eq ~ right ~ closingBrace =>
      val name = left
      val value = right match {
        case MoneyNode(_) => right.asInstanceOf[MoneyNode].asJavaBigDecimal
        case _ => right
      }
      NameValueNode(name, value)
  }

  def moneyParser = "$" ~> """\d+[.]\d+""".r ^^ {
    MoneyNode(_)
  }

  def dataTableCellParser = "|" ~> """[^|]+""".r ^^ {
    case value =>
      DataCellNode(value.trim)
  }

  def dataTableRowParser = rep(dataTableCellParser) <~ "|" ^^ {
    case cells =>
      DataTableRowNode(cells.map(s => (s.value)))
  }

  def dataTableParser = """(?!Scenario)[^:]+""".r ~ ":" ~ opt("""[^|]+""".r) ~ rep(dataTableRowParser) ^^ {
    case name ~ colon ~ junk ~ rows =>
      DataTableNode(name, rows)
  }

  def charSequenceParser = """.+""".r

}

trait ASTNode

case class FeatureNode(name: String, scenarios: List[ScenarioNode]) extends ASTNode {
  val scenariosMap = scenarios.map(s => (s.name.trim(), s)).toMap

  def findScenario(name: String) = {
    scenariosMap.get(name.trim())
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(name: String, symbols: List[NameValueNode], dataTable: Option[DataTableNode]) extends ASTNode {

  val symbolTable = symbols.map(s => (s.name, s.value)).toMap

  def getSymbol(symbolName: String) = {
    symbolTable(symbolName)
  }
  
  def symbolCount = symbolTable.size

}

case class NameValueNode(name: String, value: Any) extends ASTNode

case class DataCellNode(value: String) extends ASTNode

case class DataTableRowNode(cellValues: List[String]) extends ASTNode

case class DataTableNode(name: String, rows: List[DataTableRowNode]) extends ASTNode

case class MoneyNode(amount: String) extends ASTNode {
  def asJavaBigDecimal() = {
    BigDecimal(amount).underlying
  }
}
