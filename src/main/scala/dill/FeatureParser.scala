package dill

import scala.util.parsing.combinator.JavaTokenParsers
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList

class FeatureParser extends JavaTokenParsers {


  def dillParser = featureNameParser ~ rep(scenarioParser) ^^ {
    case featureName ~ scenarios =>
      FeatureNode(featureName, scenarios)
  }
  
  def featureNameParser = literal("Feature:") ~> charSequenceParser

  def scenarioParser = literal("Scenario:") ~> charSequenceParser ~ opt(rep(nameValueParser)) ~ opt(dataTableParser) ^^ {
    case scanarioName ~ nameValues ~ dataTable =>
      val scenario = ScenarioNode(scanarioName)
      nameValues match {
        case Some(nameValues) =>
          nameValues.foreach { nv =>
            scenario.addSymbol(nv.name, nv.value)
          }
        case None =>
      }
      dataTable match {
        case Some(dataTable) => scenario.addDataTable(dataTable)
        case None =>
      }

      scenario
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
      val res = NameValueNode(name, value)
      res
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

case class NameValueNode(val name: String, val value: Any) extends ASTNode

case class FeatureNode(val name: String, scenarios : List[ScenarioNode]) extends ASTNode {
  val scenariosMap = scenarios.map(s=>(s.name,s)).toMap
  
  def findScenario(name: String) = {
    scenariosMap.get(name)
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(val name: String) extends ASTNode {

  val symbolTable = HashMap[String, Any]()
  var dataTable : Option[DataTableNode] = None

  def addSymbol(key: String, value: Any) = {
    symbolTable.put(key, value)
  }

  def get(symbolName: String) = {
    symbolTable(symbolName)
  }

  def addDataTable(pDataTable: DataTableNode) = {
    dataTable = Some(pDataTable)
  }

}

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
