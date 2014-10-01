package dill

import scala.util.parsing.combinator.JavaTokenParsers
import scala.collection.mutable.HashMap

class FeatureParser extends JavaTokenParsers {
  
  
  def featureParser = literal("Feature:") ~>  """.+""".r ^^ {
    FeatureNode(_)
  }
  
  def scenarioParser = literal("Scenario:") ~> """.+""".r  ~ rep(nameValueParser) ^^ {
    case scanarioName ~ nameValues =>
    	val scenario = ScenarioNode(scanarioName)
    	nameValues.foreach { nv => 
		      scenario.addSymbol(nv.name, nv.value)
    	}
    scenario 	
  }
  
  def nameValueParser = """.*\{.+=""".r ~ (decimalNumber | """[\.\d\w$]+""".r) ~ literal("}") ^^ {
    case left ~ right ~ closingBrace  =>
      val name = left.split("\\{").last.split("=").head
      val value = right
      val res = NameValueNode(name,value)
    res	
  }
  
  def dill = featureParser ~ rep(scenarioParser) ^^ {
    case feature ~ scenarios => 
      scenarios.foreach { scenario =>
    	  feature.add(scenario)
      } 
     feature   
  }
  
  def parse(in : String) = {
    parseAll(dill, in)
  }
  
}

abstract class ASTNode

case class NameValueNode(val name : String, val value : Any) extends ASTNode {
  
}

case class FeatureNode(val name : String) extends ASTNode {
  var scenarios : List[ScenarioNode]  = List()
  def add(s : ScenarioNode) = {
    scenarios = scenarios :+ s
    this
  }
  override def toString = name + "\nscenarios: " + scenarios
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