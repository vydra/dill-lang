package dill;

import java.util.HashMap;
import java.util.Map;
import org.joda.money.Money;

public abstract class AbstractFeatureTest {

	private final Map<String, Object> symbolTable = new HashMap<String, Object>();

	public void addSymbol(String name, Object value) {
		symbolTable.put(name, value);
	}

	protected Object getString(String name) {
		return symbolTable.get(name);
	}

	protected Money getAmount(String name) {
		return (Money)symbolTable.get(name);
	}

}
