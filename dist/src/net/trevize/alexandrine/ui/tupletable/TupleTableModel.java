package net.trevize.alexandrine.ui.tupletable;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import net.trevize.alexandrine.api.TupleList;

public class TupleTableModel extends DefaultTableModel {

	private TupleList requirement_list;

	public TupleTableModel(TupleList requirement_list) {
		this.requirement_list = requirement_list;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return requirement_list.getRequirements().get(0).getFieldLabels()
				.size();
	}

	@Override
	public String getColumnName(int column) {
		return requirement_list.getRequirements().get(0).getFieldLabels()
				.get(column); //the fist column is the tree node
	}

	@Override
	public int getRowCount() {
		if (requirement_list == null) {
			return 0;
		} else {
			return requirement_list.getRequirements().size();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return requirement_list
				.getRequirements()
				.get(rowIndex)
				.getFieldValue(
						requirement_list.getRequirements().get(0)
								.getFieldLabels().get(columnIndex));
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}
