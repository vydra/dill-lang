package dill;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFeatureTest {

	private final Map<String, Object> symbolTable = new HashMap<String, Object>();

	public void addSymbol(String name, Object value) {
		symbolTable.put(name, value);
	}

	protected String getString(String name) {
		return (String)symbolTable.get(name);
	}
	
	protected Integer getInt(String name) {
		return (Integer)symbolTable.get(name);
	}

	protected BigDecimal getBigDecimal(String name) {
		return (BigDecimal)symbolTable.get(name);
	}

}
