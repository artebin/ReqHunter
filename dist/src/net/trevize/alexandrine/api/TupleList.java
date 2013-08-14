package net.trevize.alexandrine.api;

import java.util.List;
import java.util.Map;

public interface TupleList {

	public List<Tuple> getRequirements();

	public void loadRequirements(String requirements_file_path);

	public void writeRequirements(String requirements_file_path);

	public Tuple insertNewRequirement();

	public String getFreeID();

	public Map<String, List<String>> getFieldValueEnumerations();

}
