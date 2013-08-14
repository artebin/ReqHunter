package net.trevize.reqhunter.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

public class WorkbenchTableHeaderRenderer extends JLabel implements
		TableCellRenderer {

	private int sorted_column = 0;

	public WorkbenchTableHeaderRenderer() {
		Font police = getFont().deriveFont(Font.BOLD, 16);
		setFont(police);
	}

	// This method is called each time a column header
	// using this renderer needs to be rendered.
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		// 'value' is column header value of column 'vColIndex'
		// rowIndex is always -1
		// isSelected is always false
		// hasFocus is always false

		// Configure the component with the specified value
		setText(value.toString());

		setOpaque(true);

		// Set tool tip if desired
		setToolTipText((String) value);

		// if (vColIndex == sorted_column) {
		// if (isSelected) {
		// setIcon(new ImageIcon("tango_go_down.jpg"));
		// } else {
		// setIcon(new ImageIcon("tango_go_up.jpg"));
		// }
		// }

		setBorder(new MatteBorder(1, 1, 5, 1, Color.BLACK));

		// Since the renderer is a component, return itself
		return this;
	}

	// The following methods override the defaults for performance reasons
	public void validate() {
	}

	public void revalidate() {
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
	}

	public void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
	}
}
