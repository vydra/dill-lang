package dill

import scala.util.parsing.combinator.JavaTokenParsers

class FeatureParser extends JavaTokenParsers {
  
  def featureParser : Parser[FeatureNode] = literal("Feature:") ~>  """.+""".r ^^ {
    
    val res = new FeatureNode(_)
    res
   
  }
  
  def parse(in : String) = parseAll(featureParser, in)
  
  
}

case class FeatureNode(val featureName : String) {
}