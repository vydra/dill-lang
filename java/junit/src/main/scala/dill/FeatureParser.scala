package dill

import scala.util.parsing.combinator.JavaTokenParsers

class FeatureParser extends JavaTokenParsers {
  
  
  def feature = literal("Feature:") ~>  """.+""".r ^^ {
    FeatureNode(_)
  }
  
  def scenario = literal("Scenario:") ~>  """.+""".r ^^ {
    ScenarioNode(_)
  }
  
  def dill = feature ~ rep(scenario) ^^ {
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

case class FeatureNode(val name : String) extends ASTNode {
  var scenarios : List[ScenarioNode]  = List()
  def add(s : ScenarioNode) = {
    scenarios = scenarios :+ s
    this
  }
  override def toString = name + "\nscenarios: " + scenarios
}

case class ScenarioNode(val name : String ) extends ASTNode {
  
}