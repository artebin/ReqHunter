package net.trevize.reqhunter.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.trevize.tinker.HeaderPanel;

public class ChooseSessionDialog extends JDialog implements WindowListener {

	public static final String header_text_content = "<html><body style='background-color: #ffffff;margin: 5px 10px 5px 10px;'><h1>Add a requirement database</h1>The following data is needed to add a requirement database to the workbench:<br><ul><li>the name of the database,</li><li>the file path of the database,</li><li>the ReqHunter binding plugin to use (i.e. the type of requirement).</li></ul></body></html>";
	private JPanel main_panel;
	private JComboBox combo;

	public ChooseSessionDialog() {
		init();
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

		JPanel p0 = new JPanel();
		p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));
		combo = new JComboBox();
		p0.add(combo);
	}

	/***************************************************************************
	 * implementation of WindowListener
	 **************************************************************************/

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
