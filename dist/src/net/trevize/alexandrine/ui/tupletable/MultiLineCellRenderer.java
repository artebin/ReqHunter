package net.trevize.alexandrine.ui.tupletable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class MultiLineCellRenderer extends JLabel implements TableCellRenderer {

	public MultiLineCellRenderer() {
		//		setLineWrap(true);
		//		setWrapStyleWord(true);
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			//			setForeground(table.getSelectionForeground());
			//			setBackground(table.getSelectionBackground());

			setForeground(Color.WHITE);
			setBackground(Color.BLACK);
			//setBackground(new Color(X11Colors.red4));
		} else {
			setForeground(table.getForeground());
			if (row % 2 == 0) {
				setBackground(Color.WHITE);
			} else {
				setBackground(new Color(0xdcdcdc));
			}
		}

		//		if (isSelected) {
		//			setFont(table.getFont().deriveFont(Font.BOLD));
		//		} else {
		//			setFont(table.getFont());
		//		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				setForeground(UIManager.getColor("Table.focusCellForeground"));
				setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			setBorder(new EmptyBorder(1, 1, 1, 1));
		}

		setText((value == null) ? "" : value.toString());

		return this;
	}

}
