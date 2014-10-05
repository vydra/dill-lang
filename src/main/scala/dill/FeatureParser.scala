package dill

import scala.util.parsing.combinator.JavaTokenParsers
import scala.collection.mutable.HashMap

class FeatureParser extends JavaTokenParsers {

  def charSequenceParser = """.+""".r

  def featureParser = literal("Feature:") ~>  charSequenceParser ^^ {
    FeatureNode(_)
  }

  def scenarioParser = literal("Scenario:") ~> charSequenceParser  ~ rep(nameValueParser) ^^ {
    case scanarioName ~ nameValues =>
    	val scenario = ScenarioNode(scanarioName)
    	nameValues.foreach { nv =>
		      scenario.addSymbol(nv.name, nv.value)
    	}
    scenario
  }

  def upToLeftBraceParser = """[^{]+\{""".r
  def leftEqParser = """[^=]+""".r
  def rightEqParser = """[^}]+""".r

  def nameValueParser = upToLeftBraceParser ~> leftEqParser ~ literal("=") ~ (decimalNumber | moneyParser | rightEqParser) ~ literal("}") ^^ {
    case left ~ eq ~ right ~ closingBrace  =>
      val name = left //.split("=").head
      val value = right match {
        case MoneyNode(_) => right.asInstanceOf[MoneyNode].asJavaBigDecimal
        case _ => right
      }
      val res = NameValueNode(name,value)
    res
  }

  def moneyParser = literal("$") ~> """\d+[.]\d+""".r ^^ {
    MoneyNode(_)
  }

  def dillParser = featureParser ~ rep(scenarioParser) ^^ {
    case feature ~ scenarios =>
      scenarios.foreach { scenario =>
    	  feature.add(scenario)
      }
     feature
  }

  def parse(in : String) = {
    parseAll(dillParser, in)
  }

}

abstract class ASTNode

case class NameValueNode(val name : String, val value : Any) extends ASTNode {

}

case class FeatureNode(val name : String) extends ASTNode {
  val scenariosMap = HashMap[String, ScenarioNode]()

  def findScenario( name : String) = {
    scenariosMap.get(name)
  }

  def add(s : ScenarioNode) = {
    scenariosMap.put(s.name, s)
    this
  }

  override def toString = name + "\nscenarios: " + scenariosMap.values
}

case class ScenarioNode(val name : String ) extends ASTNode {

  val symbolTable = HashMap[String, Any]()

  def addSymbol(key : String, value : Any) = {
    symbolTable.put(key, value)
  }

  def get(symbolName : String) = {
    symbolTable(symbolName)
  }

}

case class MoneyNode(val amount : String) extends ASTNode {
  def asJavaBigDecimal() = {
    BigDecimal(amount).underlying
  }
}
