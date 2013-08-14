package net.trevize.reqhunter.ui.dialogs;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JTable;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.reqhunter.ui.RequirementEditorPanel;

public class RequirementEditionDialog extends JDialog implements WindowListener {

	private JTable jtable;
	private Tuple req;

	public RequirementEditionDialog(JTable jtable, Tuple req) {
		this.jtable = jtable;
		this.req = req;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Requirement edition");
		init();
		setSize(768, 512);
		setLocationRelativeTo(jtable);
	}

	private void init() {
		getContentPane().add(new RequirementEditorPanel(jtable, req));
	}

	/***************************************************************************
	 * implementation of WindowListener.
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

}
