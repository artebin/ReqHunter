package net.trevize.reqhunter.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.reqhunter.ReqHunterProperties;

public class RequirementDisplayerPanel extends JScrollPane {

	private Tuple req;
	private JEditorPane view;
	private HTMLEditorKit kit;

	public RequirementDisplayerPanel() {
		super();

		//create the jeditorpane.
		view = new JEditorPane();
		view.setEditable(false);
		kit = new HTMLEditorKit();
		view.setEditorKit(kit);

		//loading the stylesheet.
		StyleSheet ss = new StyleSheet();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(
					ReqHunterProperties.getMain_css_path());
			BufferedReader br = new BufferedReader(fr);
			sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ss.addRule(sb.toString());
		kit.getStyleSheet().addStyleSheet(ss);

		setViewportView(view);
		getHorizontalScrollBar().setUnitIncrement(10);
		getVerticalScrollBar().setUnitIncrement(10);
	}

	public void setRequirement(Tuple req) {
		this.req = req;

		if (req == null) {
			view.setText("");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		for (String field_label : req.getFieldLabels()) {
			sb.append("<h1>" + field_label + "</h1>");
			sb.append(req.getFieldValue(field_label));
			sb.append("<hr>");
		}
		sb.append("</body></html>");
		view.setText(sb.toString());

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

		view.repaint();
	}

	public Tuple getDisplayedRequirement() {
		return req;
	}

}
