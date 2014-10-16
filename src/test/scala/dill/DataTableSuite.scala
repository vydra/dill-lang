package dill

import org.scalatest.junit.JUnitSuite
import org.junit.Test

class DataTableSuite extends JUnitSuite {
  
  val p = new FeatureParser()
  
  @Test def data_table_row_one_cell() {
    val tableTxt = """|1|"""
    val dataTableRowNode = p.parseAll(p.dataTableRowParser, tableTxt).get
    assert(dataTableRowNode.cellValues(0) === "1")
  }

  @Test def data_table_one_row_two_cells() {
    val tableTxt = """|col1|col2|"""
    val dataTableRowNode = p.parseAll(p.dataTableRowParser, tableTxt).get
    assert(dataTableRowNode.cellValues.size === 2)
    assert(dataTableRowNode.cellValues(0) === "col1")
    assert(dataTableRowNode.cellValues(1) === "col2")
  }

  @Test def data_table_two_rows_one_cell_each() {
    val tableTxt = """Num table:
      |1|
      |2|"""
    val dataTableNode = p.parseAll(p.dataTableParser, tableTxt).get
    assert(dataTableNode.rows.size === 2)
    assert(dataTableNode.name === "Num table")
    assert(dataTableNode.rows(0).cellValues(0) === "1")
    assert(dataTableNode.rows(1).cellValues(0) === "2")
  }

  @Test def scenario_with_datatable_one_column() {
    val txt = """Scenario: data table scenario
    Num Table:
      |num|
      |1|"""
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "num")
    assert(scenarioNode.dataTable.get.rows(1).cellValues(0) === "1")
  }
  
    @Test def scenario_with_datatable_one_col_2_rows() {
    val txt = """Scenario: data table scenario
  Data Table:
      |num|
      |1|
      |2|
      """
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "num")
    assert(scenarioNode.dataTable.get.rows(1).cellValues(0) === "1")
    assert(scenarioNode.dataTable.get.rows(2).cellValues(0) === "2")
  }


  @Test def scenario_with_datatable_two_columns() {
    val txt = """Scenario: data table scenario
    DataTable:
      |col1 | col2|
      |1    |    2|
    """
    val scenarioNode = p.parseAll(p.scenarioParser, txt).get
    assert(scenarioNode.dataTable.get.rows(0).cellValues(0) === "col1")
    assert(scenarioNode.dataTable.get.rows(0).cellValues(1) === "col2")
    assert(scenarioNode.dataTable.get.rows(1).cellValues(0) === "1")
    assert(scenarioNode.dataTable.get.rows(1).cellValues(1) === "2")
  }

}