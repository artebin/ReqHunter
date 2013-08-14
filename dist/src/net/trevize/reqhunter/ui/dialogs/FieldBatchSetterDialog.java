package net.trevize.reqhunter.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.trevize.alexandrine.api.Tuple;

public class FieldBatchSetterDialog extends JDialog implements WindowListener,
		ActionListener {

	public static final String ACTION_COMMAND_OK = "ACTION_COMMAND_OK";
	public static final String ACTION_COMMAND_CANCEL = "ACTION_COMMAND_CANCEL";

	private JComponent parent;
	private String field_label;
	private JTextField jtf;
	private List<Tuple> updated_requirements;

	public FieldBatchSetterDialog(JComponent parent) {
		this.parent = parent;
		setModalityType(ModalityType.APPLICATION_MODAL);
		init();
		pack();
		setLocationRelativeTo(parent);
	}

	private void init() {
		setLayout(new BorderLayout());
		jtf = new JTextField(30);
		add(jtf, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout());

		JButton b0 = new JButton("Cancel");
		b0.setActionCommand(ACTION_COMMAND_CANCEL);
		b0.addActionListener(this);
		p.add(b0, 0, 0);

		JButton b1 = new JButton("Ok");
		b1.setActionCommand(ACTION_COMMAND_OK);
		b1.addActionListener(this);
		p.add(b1, 0, 1);

		add(p, BorderLayout.SOUTH);
	}

	public void displayDialog(String field_label, String field_value_preset,
			List<Tuple> updated_requirements) {
		this.field_label = field_label;
		this.updated_requirements = updated_requirements;

		jtf.setText(field_value_preset);
		jtf.setCaretPosition(field_value_preset.length());
		jtf.moveCaretPosition(0);
		setTitle("Field Batch Setter: " + field_label);
		setLocationRelativeTo(parent);
		setVisible(true);
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
	public void actionPerformed(ActionEvent arg0) {
		String action_command = arg0.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_OK)) {
			//update the field value of the requirements
			for (Tuple req : updated_requirements) {
				req.setFieldValue(field_label, jtf.getText());
			}

			//hide the dialog
			setVisible(false);
		}

		else

		if (action_command.equals(ACTION_COMMAND_CANCEL)) {
			//hide the dialog
			setVisible(false);
		}
	}

}
