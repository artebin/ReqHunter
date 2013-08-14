package net.trevize.reqhunter.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.trevize.reqhunter.ReqHunter;
import net.trevize.reqhunter.RequirementDatabase;
import net.trevize.reqhunter.plugins.ReqHunterPlugin;
import net.trevize.tinker.CellStyle;
import net.trevize.tinker.HeaderPanel;
import net.trevize.tinker.XGridBag;

import org.java.plugin.PluginLifecycleException;

public class AddRequirementDatabaseDialog extends JDialog implements
		WindowListener, ActionListener {

	public static final String header_text_content = "<html><body style='background-color: #ffffff;margin: 5px 10px 5px 10px;'><h1>Add a requirement database</h1>The following data is needed to add a requirement database to the workbench:<br><ul><li>the name of the database,</li><li>the file path of the database,</li><li>the ReqHunter binding plugin to use (i.e. the type of requirement).</li></ul></body></html>";
	public static final String ACTION_COMMAND_ADD_DATABASE_TO_WORKBENCH = "ACTION_COMMAND_ADD_DATABASE_TO_WORKBENCH";

	private JComponent parent;
	private JTextField jtf_database_name;
	private JTextField jtf_requirements_file_path;
	private JComboBox jtf_requirement_class_implementation;
	private JCheckBox jcb_create_new;
	private JButton b_add_database;
	private RequirementDatabase req_db;

	public AddRequirementDatabaseDialog(JComponent parent) {
		this.parent = parent;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Add a requirement database");
		init();
		pack();
		setLocationRelativeTo(parent);
	}

	private void init() {
		getContentPane().setLayout(new BorderLayout());

		ImageIcon header_icon = new ImageIcon("./gfx/ReqHunter.png");
		while (header_icon.getImageLoadStatus() == java.awt.MediaTracker.LOADING) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		getContentPane().add(new HeaderPanel(header_icon, header_text_content),
				BorderLayout.NORTH);

		/**********************************************************************/

		JPanel p_properties = new JPanel();
		p_properties.setBorder(new EmptyBorder(5, 5, 5, 5));
		p_properties.setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(p_properties);
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

		xgb.add(Box.createVerticalStrut(10), style1, xgb_next_row++, 0);

		JPanel p0 = new JPanel();
		p0.setBorder(new TitledBorder("Database name"));
		p0.setLayout(new BorderLayout());
		jtf_database_name = new JTextField();
		p0.add(jtf_database_name, BorderLayout.CENTER);
		xgb.add(p0, style1, xgb_next_row++, 0);

		/**********************************************************************/

		xgb.add(Box.createVerticalStrut(10), style1, xgb_next_row++, 0);

		/**********************************************************************/

		JPanel p1 = new JPanel();
		p1.setBorder(new TitledBorder("Database file path"));
		p1.setLayout(new BorderLayout());
		jtf_requirements_file_path = new JTextField();
		p1.add(jtf_requirements_file_path, BorderLayout.CENTER);
		JButton b_browse = new JButton(" Browse... ");
		b_browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				if (fileChooser.getSelectedFile() != null) {
					try {
						jtf_requirements_file_path.setText(fileChooser
								.getSelectedFile().getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		p1.add(b_browse, BorderLayout.EAST);

		xgb.add(p1, style1, xgb_next_row++, 0);

		/**********************************************************************/

		xgb.add(Box.createVerticalStrut(10), style1, xgb_next_row++, 0);

		/**********************************************************************/

		JPanel p2 = new JPanel();
		p2.setBorder(new TitledBorder("ReqHunter binding plugin"));
		p2.setLayout(new BorderLayout());
		jtf_requirement_class_implementation = new JComboBox(new Vector(
				ReqHunter.getRequirement_binding_plugin_descriptors().keySet()));
		p2.add(jtf_requirement_class_implementation, BorderLayout.CENTER);
		xgb.add(p2, style1, xgb_next_row++, 0);

		/**********************************************************************/

		xgb.add(Box.createVerticalStrut(10), style1, xgb_next_row++, 0);

		/**********************************************************************/

		getContentPane().add(p_properties, BorderLayout.CENTER);

		/**********************************************************************/

		xgb.add(Box.createVerticalStrut(10), style1, xgb_next_row++, 0);

		/**********************************************************************/

		JPanel p3 = new JPanel();
		p3.setBorder(new EmptyBorder(5, 5, 5, 5));
		p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
		p3.add(Box.createHorizontalGlue());
		JButton b_cancel = new JButton(" Cancel ");
		b_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				return;
			}
		});
		p3.add(b_cancel);

		p3.add(Box.createHorizontalStrut(5));

		b_add_database = new JButton("Add database to workbench");
		b_add_database
				.setActionCommand(ACTION_COMMAND_ADD_DATABASE_TO_WORKBENCH);
		b_add_database.addActionListener(this);
		p3.add(b_add_database);

		getContentPane().add(p3, BorderLayout.SOUTH);
	}

	public RequirementDatabase getReq_db() {
		return req_db;
	}

	/***************************************************************************
	 * implementation of WindowListener
	 **************************************************************************/

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command == ACTION_COMMAND_ADD_DATABASE_TO_WORKBENCH) {
			String requirements_file_path = jtf_requirements_file_path
					.getText();

			File f_req_db;

			if (requirements_file_path.trim().isEmpty()) {
				f_req_db = null;
			} else {
				f_req_db = new File(requirements_file_path);
			}

			String f_path_req_db = null;
			try {
				f_path_req_db = f_req_db.getCanonicalPath();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			if (!f_req_db.exists()) {
				int result = JOptionPane
						.showConfirmDialog(
								this,
								"The specified file:\n"
										+ f_path_req_db
										+ "\ndo not exist.\n\n"
										+ "Create a new file for a new requirements database?",
								"The specified file do not exist",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				return;
			}

			String database_name = jtf_database_name.getText();
			String plugin_name = (String) jtf_requirement_class_implementation
					.getSelectedItem();
			String requirement_implementation_name = null;

			try {
				requirement_implementation_name = ((ReqHunterPlugin) ReqHunter
						.getPlugin_manager()
						.getPlugin(
								ReqHunter
										.getRequirement_binding_plugin_descriptors()
										.get(plugin_name).getId()))
						.getFactoryName();
			} catch (PluginLifecycleException e1) {
				e1.printStackTrace();
			}
			req_db = new RequirementDatabase(requirements_file_path,
					database_name, requirement_implementation_name);
			setVisible(false);
		}
	}

}
