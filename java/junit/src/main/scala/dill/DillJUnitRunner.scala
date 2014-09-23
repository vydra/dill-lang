package dill

import org.junit.runners.BlockJUnit4ClassRunner

class DillJUnitRunner(val clazz : java.lang.Class[_]) extends BlockJUnit4ClassRunner(clazz) {
  
   override def createTest() = {
    val currTest = super.createTest().asInstanceOf[AbstractFeatureTest] 
    currTest.addSymbol("balance", new java.math.BigDecimal(100.00))
    currTest
   }
  
}