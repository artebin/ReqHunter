package net.trevize.reqhunter.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.table.DefaultTableCellRenderer;

import net.trevize.reqhunter.RequirementDatabase;


public class WorkbenchTableCellRenderer extends DefaultTableCellRenderer {

	public static String encodeRGB(Color color) {
		if (color == null) {
			return "#000000";
		}

		return "#"
				+ Integer.toHexString(color.getRGB()).substring(2)
						.toUpperCase();
	}

	private UIDefaults defaults;
	private Color color_bg, color_fg;
	private String html_color_bg, html_color_fg;

	public WorkbenchTableCellRenderer() {
		defaults = javax.swing.UIManager.getDefaults();
		color_bg = defaults.getColor("List.selectionBackground");
		color_fg = defaults.getColor("List.selectionForeground");
		html_color_bg = encodeRGB(defaults.getColor("List.selectionBackground"));
		html_color_fg = encodeRGB(defaults.getColor("List.selectionForeground"));
	}

	/***************************************************************************
	 * implementation of TableCellRenderer
	 **************************************************************************/

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		RequirementDatabase req_db = (RequirementDatabase) value;

		String text = "<h1>" + req_db.getDatabase_name()
				+ "</h1>Requirement database called "
				+ req_db.getDatabase_name() + "<br>here is the stats...";

		if (!isSelected) {
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
			setText("<html><body>" + text + "</body></html>");
		} else {
			setBackground(color_bg);
			setForeground(color_fg);
			setText("<html><body style='color:" + html_color_fg
					+ "; background-color:" + html_color_bg + ";'>" + text
					+ "</body></html>");
		}

		table.setRowHeight(row, (int) getPreferredSize().getHeight());
		return this;
	}

}
