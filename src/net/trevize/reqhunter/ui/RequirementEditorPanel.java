package net.trevize.reqhunter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.tinker.CellStyle;
import net.trevize.tinker.JTextAreaPP;
import net.trevize.tinker.X11Colors;
import net.trevize.tinker.XGridBag;

public class RequirementEditorPanel extends JScrollPane implements
		ActionListener {

	private static final String ACTION_COMMAND_APPLY_MODIFICATION = "ACTION_COMMAND_APPLY_MODIFICATION";

	private JTable jtable;
	private Tuple requirement;
	private JPanel panel;
	private HashMap<String, JTextAreaPP> components;

	public RequirementEditorPanel(JTable jtable, Tuple requirement) {
		/*
		 * this component needs a reference to the JTable to fire for modifications.
		 */
		this.jtable = jtable;
		this.requirement = requirement;
		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(new Color(X11Colors.LightSteelBlue));

		components = new HashMap<String, JTextAreaPP>();

		panel.setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(panel);
		int xgb_next_row = 0;

		CellStyle style0 = new CellStyle(1., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

		JButton b_apply_modification = new JButton("Apply modifications");
		b_apply_modification
				.setActionCommand(ACTION_COMMAND_APPLY_MODIFICATION);
		b_apply_modification.addActionListener(this);
		xgb.add(b_apply_modification, style0, xgb_next_row++, 0);

		xgb.add(Box.createVerticalStrut(10), style0, xgb_next_row++, 0);
		xgb.add(new JSeparator(), style0, xgb_next_row++, 0);
		xgb.add(Box.createVerticalStrut(10), style0, xgb_next_row++, 0);

		for (int i = 0; i < requirement.getFieldLabels().size(); ++i) {
			String field_label = requirement.getFieldLabels().get(i);

			JTextAreaPP jta = new JTextAreaPP();
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
			jta.setText(requirement.getFieldValue(field_label));
			jta.getUndoManager().discardAllEdits();

			JPanel p = new JPanel();
			p.setBackground(new Color(X11Colors.LightSteelBlue));
			p.setLayout(new BorderLayout());
			TitledBorder border = new TitledBorder(field_label);
			Font font = border.getTitleFont().deriveFont(
					Font.BOLD + Font.ITALIC, 16);
			border.setTitleFont(font);
			p.setBorder(border);
			p.add(jta, BorderLayout.CENTER);

			components.put(field_label, jta);
			xgb.add(p, style0, xgb_next_row++, 0);

			xgb.add(Box.createVerticalStrut(10), style0, xgb_next_row++, 0);
		}

		CellStyle style1 = new CellStyle(1., 1., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

		xgb.add(Box.createVerticalGlue(), style1, xgb_next_row++, 0);

		setViewportView(panel);
		getHorizontalScrollBar().setUnitIncrement(10);
		getVerticalScrollBar().setUnitIncrement(10);

		/*
		 * put the scrollbar value of the scrollpane that contained the JeditorPane
		 * to 0. 
		 * Because of the JEditorPane, this has to be done in a separate thread.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getVerticalScrollBar().setValue(0);
				getHorizontalScrollBar().setValue(0);
			}
		});
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action_command = arg0.getActionCommand();

		if (action_command == ACTION_COMMAND_APPLY_MODIFICATION) {
			for (int i = 0; i < requirement.getFieldLabels().size(); ++i) {
				String field_label = requirement.getFieldLabels().get(i);

				requirement.setFieldValue(field_label,
						components.get(field_label).getText());
			}

			((DefaultTableModel) jtable.getModel()).fireTableDataChanged();
			jtable.repaint();
		}
	}

}
