package net.trevize.reqhunter.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.trevize.reqhunter.RequirementDatabase;
import net.trevize.reqhunter.ui.dialogs.AddRequirementDatabaseDialog;
import net.trevize.tinker.CellStyle;
import net.trevize.tinker.XGridBag;

public class WorkbenchManagerPanel extends JPanel implements ActionListener {

	public static final String ACTION_COMMAND_ADD_DATABASE = "ACTION_COMMAND_ADD_DATABASE";
	public static final String ACTION_COMMAND_REMOVE_DATABASE = "ACTION_COMMAND_REMOVE_DATABASE";
	public static final String ACTION_COMMAND_PROPERTIES_DATABASE = "ACTION_COMMAND_PROPERTIES_DATABASE";
	public static final String ACTION_COMMAND_OPEN_DATABASE = "ACTION_COMMAND_OPEN_DATABASE";

	private ReqHunterGUI reqhunter_gui;
	private Workbench workbench;
	private JButton b_add_db;
	private JButton b_rm_db;
	private JButton b_properties_db;
	private JButton b_open_db;
	private JTable table_databases;
	private JScrollPane jsp;

	public WorkbenchManagerPanel(ReqHunterGUI reqhunter_gui, Workbench workbench) {
		this.reqhunter_gui = reqhunter_gui;
		this.workbench = workbench;
		init();
	}

	private void init() {
		setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(this);
		int xgb_next_row = 0;

		CellStyle style0 = new CellStyle(1., 0., GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

		CellStyle style1 = new CellStyle(1., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

		CellStyle style2 = new CellStyle(1., .5, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

		CellStyle style3 = new CellStyle(1., 1., GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

		/**********************************************************************/

		JPanel p_toolbar = new JPanel();
		p_toolbar.setLayout(new BoxLayout(p_toolbar, BoxLayout.X_AXIS));

		ImageIcon i1 = new ImageIcon("./gfx/org/freedesktop/tango/list-add.png");
		b_add_db = new JButton(i1);
		b_add_db.setToolTipText("Add a new database");
		b_add_db.setActionCommand(ACTION_COMMAND_ADD_DATABASE);
		b_add_db.addActionListener(this);
		p_toolbar.add(b_add_db);

		p_toolbar.add(Box.createHorizontalStrut(5));

		ImageIcon i2 = new ImageIcon(
				"./gfx/org/freedesktop/tango/list-remove.png");
		b_rm_db = new JButton(i2);
		b_rm_db.setToolTipText("Remove the selected database");
		p_toolbar.add(b_rm_db);

		p_toolbar.add(Box.createHorizontalStrut(5));

		ImageIcon i3 = new ImageIcon(
				"./gfx/org/freedesktop/tango/document-properties.png");
		b_properties_db = new JButton(i3);
		b_properties_db.setToolTipText("Display database properties");
		p_toolbar.add(b_properties_db);

		p_toolbar.add(Box.createHorizontalStrut(5));

		ImageIcon i4 = new ImageIcon(
				"./gfx/org/freedesktop/tango/document-open.png");
		b_open_db = new JButton(i4);
		b_open_db.setToolTipText("Open database");
		b_open_db.setActionCommand(ACTION_COMMAND_OPEN_DATABASE);
		b_open_db.addActionListener(this);
		p_toolbar.add(b_open_db);

		p_toolbar.add(Box.createHorizontalGlue());

		xgb.add(p_toolbar, style0, xgb_next_row++, 0);

		/**********************************************************************/

		xgb.add(Box.createVerticalStrut(5), style1, xgb_next_row++, 0);

		/**********************************************************************/

		table_databases = new JTable(workbench);
		table_databases.getColumnModel().getColumn(0)
				.setCellRenderer(new WorkbenchTableCellRenderer());

		for (int vci = 0; vci < table_databases.getColumnCount(); ++vci) {
			TableColumn c = table_databases.getColumnModel().getColumn(vci);
			WorkbenchTableHeaderRenderer rhr = new WorkbenchTableHeaderRenderer();
			rhr.setHorizontalAlignment(SwingConstants.CENTER);
			c.setHeaderRenderer(rhr);
		}

		jsp = new JScrollPane(table_databases);
		jsp.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		xgb.add(jsp, style2, xgb_next_row++, 0);
	}

	/***************************************************************************
	 * implements ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action_command = arg0.getActionCommand();

		if (action_command == ACTION_COMMAND_ADD_DATABASE) {
			AddRequirementDatabaseDialog d = new AddRequirementDatabaseDialog(
					reqhunter_gui.getMain_frame().getRootPane());
			d.setVisible(true);
			if (d.getReq_db() != null) {
				workbench.addRequirementDatabase(d.getReq_db());
				table_databases.updateUI();
			}
		}

		else

		if (action_command == ACTION_COMMAND_OPEN_DATABASE) {
			int selected_row = table_databases.getSelectedRow();

			//we manage the case: no selected database
			if (selected_row == -1) {
				return;
			}

			//get the name of the selected dababase
			RequirementDatabase req_db = (RequirementDatabase) ((DefaultTableModel) table_databases
					.getModel()).getValueAt(selected_row, 0);
			String req_db_name = req_db.getDatabase_name();
			System.out.println("open database: " + req_db_name);

			reqhunter_gui.addRequirementDatabaseUI(req_db);
		}
	}

}
