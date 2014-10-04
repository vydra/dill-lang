package dill;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFeatureTest {

	private final Map<String, Object> symbolTable = new HashMap<String, Object>();

	public void addSymbol(String name, Object value) {
		symbolTable.put(name, value);
	}

	protected Object getString(String name) {
		return symbolTable.get(name);
	}

	protected BigDecimal getBigDecimal(String name) {
		return (BigDecimal)symbolTable.get(name);
	}

}
