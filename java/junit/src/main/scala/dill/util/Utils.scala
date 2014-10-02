package dill.util

object Utils {
  
  def featureNameFromClassName(className : String) = {
    val lastDotIndex = className.lastIndexOf('.')
    val featureTestIndex = className.indexOf("FeatureTest")
    className.substring(lastDotIndex+1, featureTestIndex)
  }

}