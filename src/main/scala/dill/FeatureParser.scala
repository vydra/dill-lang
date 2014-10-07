package dill

import scala.util.parsing.combinator.JavaTokenParsers
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList

class FeatureParser extends JavaTokenParsers {

  def dillParser = featureNameParser ~ rep(scenarioParser) ^^ {
    case featureName ~ scenarios =>
      FeatureNode(featureName, scenarios)
  }
  
  def featureNameParser = "Feature:" ~> charSequenceParser

  def scenarioParser = "Scenario:" ~> charSequenceParser ~ opt(rep(nameValueParser)) ~ opt(dataTableParser) ^^ {
    case scanarioName ~ symbols ~ dataTable =>
      ScenarioNode(scanarioName, symbols,  dataTable)
  }

  def upToLeftBraceParser = """[^{]+\{""".r
  def leftEqParser = """[^=]+""".r
  def rightEqParser = """[^}]+""".r

  def nameValueParser = upToLeftBraceParser ~> leftEqParser ~ literal("=") ~ (decimalNumber | moneyParser | rightEqParser) ~ literal("}") ^^ {
    case left ~ eq ~ right ~ closingBrace =>
      val name = left //.split("=").head
      val value = right match {
        case MoneyNode(_) => right.asInstanceOf[MoneyNode].asJavaBigDecimal
        case _ => right
      }
      NameValueNode(name, value)
  }

  def moneyParser = literal("$") ~> """\d+[.]\d+""".r ^^ {
    MoneyNode(_)
  }

  def dataTableCellParser = literal("|") ~> """[^|]""".r ^^ {
    new DataCellNode(_)
  }

  def dataTableRowParser = rep(dataTableCellParser) <~ literal("|") ^^ {
    case cells =>
      val row = DataTableRowNode()
      cells.foreach { cell =>
        row.addValue(cell.value)
      }
      row
  }

  def dataTableParser = rep(dataTableRowParser) ^^ {
    case rows =>
      val dataTableNode = DataTableNode()
      rows.foreach { row => dataTableNode.addRow(row) }
      dataTableNode
  }

  def charSequenceParser = """.+""".r

  def parse(in: String) = {
    parseAll(dillParser, in)
  }

}

trait ASTNode

case class FeatureNode(name: String, scenarios : List[ScenarioNode]) extends ASTNode {
  val scenariosMap = scenarios.map(s=>(s.name,s)).toMap
  
  def findScenario(name: String) = {
    scenariosMap.get(name)
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(name: String, symbols: Option[List[NameValueNode]], dataTable : Option[DataTableNode]) extends ASTNode {

  val symbolTable = symbols match {
    case Some(symbols) => symbols.map( s=> (s.name, s.value ) ).toMap
    case None => Map.empty[String,Any]
  }

  def get(symbolName: String) = {
    symbolTable(symbolName)
  }

}

case class NameValueNode(val name: String, val value: Any) extends ASTNode

case class DataCellNode(val value: String) extends ASTNode

case class DataTableRowNode() extends ASTNode {
  val cellValues = MutableList[String]()
  def addValue(value: String) {
    cellValues += value
  }
}

case class DataTableNode() extends ASTNode {
  val rows = MutableList[DataTableRowNode]()
  def addRow(row: DataTableRowNode) {
    rows += row
  }
}

case class MoneyNode(val amount: String) extends ASTNode {
  def asJavaBigDecimal() = {
    BigDecimal(amount).underlying
  }
}
