package dill

import scala.util.parsing.combinator.JavaTokenParsers
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList

class FeatureParser extends JavaTokenParsers {

  // override val whiteSpace = """\t+""".r

  def charSequenceParser = """.+""".r

  def featureParser = literal("Feature:") ~> charSequenceParser ^^ {
    FeatureNode(_)
  }

  def scenarioParser = literal("Scenario:") ~> charSequenceParser ~ opt(rep(nameValueParser)) ~ opt(rep(dataTableRowParser)) ^^ {
    case scanarioName ~ nameValues ~ dataTableRows =>
      val scenario = ScenarioNode(scanarioName)
      nameValues match {
        case Some(nameValues) =>
          nameValues.foreach { nv =>
            scenario.addSymbol(nv.name, nv.value)
          }
        case None =>
      }
      dataTableRows match {
        case Some(dataTableRows) => dataTableRows.foreach(
          row => scenario.addDataTableRow(row))
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
      val row = new DataTableRowNode()
      cells.foreach { cell =>
        row.addValue(cell.value)
      }
      row
  }

//  def dataTableParser = rep(dataTableRowParser) ^^ {
//    case row =>
//      println("row: " + row)
//  }

  def dillParser = featureParser ~ rep(scenarioParser) ^^ {
    case feature ~ scenarios =>
      scenarios.foreach { scenario =>
        feature.add(scenario)
      }
      feature
  }

  def parse(in: String) = {
    parseAll(dillParser, in)
  }

}

abstract class ASTNode

case class NameValueNode(val name: String, val value: Any) extends ASTNode {

}

case class FeatureNode(val name: String) extends ASTNode {
  val scenariosMap = HashMap[String, ScenarioNode]()

  def findScenario(name: String) = {
    scenariosMap.get(name)
  }

  def add(s: ScenarioNode) = {
    scenariosMap.put(s.name, s)
    this
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(val name: String) extends ASTNode {

  val symbolTable = HashMap[String, Any]()
  val dataTable = MutableList[DataTableRowNode]()

  def addSymbol(key: String, value: Any) = {
    symbolTable.put(key, value)
  }

  def get(symbolName: String) = {
    symbolTable(symbolName)
  }

  def addDataTableRow(row: DataTableRowNode) = {
    dataTable += row
  }

}

case class DataCellNode(val value: String) extends ASTNode

case class DataTableRowNode() extends ASTNode {
  val cellValues = MutableList[String]()
  def addValue(value: String) {
    cellValues += value
  }
}

case class MoneyNode(val amount: String) extends ASTNode {
  def asJavaBigDecimal() = {
    BigDecimal(amount).underlying
  }
}
