package dill.util

object Utils {

  def featureNameFromClassName(className: String) = {
    val lastDotIndex = className.lastIndexOf('.')
    val splits = className.split("FeatureTest|FeatureSuite")
    splits(0).substring(lastDotIndex + 1)
  }
}
