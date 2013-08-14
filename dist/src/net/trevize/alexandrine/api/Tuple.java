package net.trevize.alexandrine.api;

import java.util.List;

/**
 * Some remarks:
 *   - the field labels are the labels of the applicable fields for the a Requirement type.
 *   - the Requirement table can contains some columns that are not applicable fields but representation fields.
 *     For instance the reference field is a representation field: this reference can be a concatenation of several fields.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Requirement.java - Jun 15, 2011
 */

public interface Tuple {

	public String getId();

	public void setId(String id);

	public List<String> getFieldLabels();

	public List<String> getFieldValues();

	public String getFieldValue(String field_label);

	public void setFieldValue(String field_label, String field_value);

	public String toString();

	public int getIndexOfLabel(String field_label);

}
