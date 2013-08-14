package net.trevize.reqhunter.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.BasicPanel;
import org.xhtmlrenderer.swing.FSMouseListener;
import org.xhtmlrenderer.swing.LinkListener;
import org.xhtmlrenderer.swing.SelectionHighlighter;

public class AboutDialog extends JDialog implements WindowListener,
		HyperlinkListener {

	private JPanel main_panel;
	private XHTMLPanel xhtml_panel;
	private FSScrollPane scrollpane;
	private JButton close_button;

	public AboutDialog(JComponent parent) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("About");
		init();
		setSize(442, 356);
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void init() {
		//setting swing components.
		main_panel = new JPanel();
		setLayout(new BorderLayout());
		add(main_panel, BorderLayout.CENTER);
		main_panel.setLayout(new BorderLayout());
		main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		scrollpane = new FSScrollPane();
		main_panel.add(scrollpane, BorderLayout.CENTER);

		xhtml_panel = new XHTMLPanel();
		xhtml_panel.setDocument(new File("./html/about.html").toURI()
				.toString());
		xhtml_panel.getSharedContext().getTextRenderer()
				.setSmoothingThreshold(1.f);

		//remove the default LinkListener
		List<FSMouseListener> mouse_tracking_listeners = xhtml_panel
				.getMouseTrackingListeners();
		for (FSMouseListener listener : mouse_tracking_listeners) {
			if (listener instanceof LinkListener) {
				xhtml_panel.removeMouseTrackingListener(listener);
			}
		}

		//install our LinkListener
		xhtml_panel.addMouseTrackingListener(new LinkListener() {
			@Override
			public void linkClicked(BasicPanel panel, String uri) {
				try {
					java.awt.Desktop.getDesktop().browse(new URI(uri));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		SelectionHighlighter sh = new SelectionHighlighter();
		sh.install(xhtml_panel);

		scrollpane.setViewportView(xhtml_panel);
		scrollpane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//create a panel for the close button at the bottom of the HelpDialog
		//the Close button is centered using two Box.createGlue()
		JPanel panel0 = new JPanel();
		panel0.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
		panel0.add(Box.createGlue());
		close_button = new JButton("Close");
		close_button.setMnemonic('C');
		close_button.setIcon(new ImageIcon("./gfx/gtk-close.png"));
		close_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel0.add(close_button);
		panel0.add(Box.createGlue());
		main_panel.add(panel0, BorderLayout.SOUTH);
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

	/***************************************************************************
	* implementation of HyperlinkListener.
	**************************************************************************/

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("link clicked");
			System.out.println(e.getDescription());

			try {
				java.awt.Desktop.getDesktop().browse(
						new URI(e.getDescription()));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

}