package dill

import org.junit.runners.BlockJUnit4ClassRunner
import dill.util.Utils
import java.io.File
import scala.reflect.io.Path
import scala.io._
import org.junit.internal.runners.statements.InvokeMethod
import org.junit.runners.model.FrameworkMethod

class DillJUnitRunner(val clazz: java.lang.Class[_]) extends BlockJUnit4ClassRunner(clazz) {
  /*
   * based on current test, load and parse the feature file or throw exception
   * if test-method name matches, bind data or throw exception
   *
   */

  override def methodInvoker(method: FrameworkMethod, test: Object) = {
    val currTest = test.asInstanceOf[AbstractFeatureTest]
    val featureName = Utils.featureNameFromClassName(currTest.getClass().getName())

    val featureTxt = Source.fromFile("src/test/resources/" + featureName + ".feature").getLines().mkString("\n")
    val p = new FeatureParser()
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario(method.getName()) match {
      case Some(s) =>
        s.symbolTable.keysIterator.foreach( name =>
          s.symbolTable.get(name) match {
            case Some(value) => currTest.addSymbol(name, value)
          }
        )
      case None => throw new Exception("Scenario not found")
    }

    super.methodInvoker(method, currTest)
  }

}
