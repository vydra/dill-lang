package dill

import org.scalatest.junit.JUnitSuite
import org.junit.Test

class DataTableSuite extends JUnitSuite {
  /*
   * Perhaps use example
   * http://thomassundberg.wordpress.com/2014/06/30/cucumber-data-tables/
   * 
   * and use http://cukes.info/api/cucumber/jvm/javadoc/cucumber/api/DataTable.html
   * 
   */
  @Test def data_table_row() {
    val tableTxt = """|1|"""
    val p = new FeatureParser()
    val dataTableRowNode = p.parseAll(p.dataTableRowParser, tableTxt).get
    assert(dataTableRowNode.cellValues(0) === "1")
  }

  @Test def data_table_one_row_two_cells() {
    val tableTxt = """|1|2|"""
    val p = new FeatureParser()
    val dataTableRowNode = p.parseAll(p.dataTableRowParser, tableTxt).get
    assert(dataTableRowNode.cellValues.size === 2)
    assert(dataTableRowNode.cellValues(0) === "1")
    assert(dataTableRowNode.cellValues(1) === "2")
  }

  @Test def data_table_two_rows_one_cell_each() {
    val tableTxt = """
      |1|
      |2|"""
    val p = new FeatureParser()
    val dataTableNode = p.parseAll(p.dataTableParser, tableTxt).get
    assert(dataTableNode.rows.size === 2)
    assert(dataTableNode.rows(0).cellValues(0) === "1")
    assert(dataTableNode.rows(1).cellValues(0) === "2")
  }

  @Test def scanrio_with_datatable1() {
    val txt = """Scenario: data table scenario
      |1|"""
    val p = new FeatureParser()
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "1")
  }

  @Test def scenario_with_datatableOneRowTwoCells() {
    val txt = """Scenario: data table scenario
      |1|2|"""
    val p = new FeatureParser()
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "1")
    assert(scenarioNode.dataTable.get.rows(0).cellValues(1) === "2")
  }

  @Test def scenario_with_datatable_two_rows_one_cell_each() {
    val txt = """Scenario: data table scenario
      |1|
      |2|
      """
    val p = new FeatureParser()
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "1")
    assert(scenarioNode.dataTable.get.rows(1).cellValues(0) === "2")
  }

}