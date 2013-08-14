package net.trevize.alexandrine.ui.tupletable;

import org.netbeans.swing.outline.RenderDataProvider;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * RenderDataProvider.java - Jun 15, 2011
 */

public class TupleRenderDataProvider implements RenderDataProvider {

	@Override
	public java.awt.Color getBackground(Object o) {
		return null;
	}

	@Override
	public String getDisplayName(Object o) {
		String value = o.toString();
		int n = 20;

		if (value != null && value.length() > n) {
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body>");
			for (int i = 0; i < (value.length() / n); ++i) {
				sb.append(value.substring(i * n, i * n + 1) + "<br/>");
			}
			sb.append(value.substring(value.length() % n, value.length()));
			sb.append("</body></html>");
		}

		return value;
	}

	@Override
	public java.awt.Color getForeground(Object o) {
		return null;
	}

	@Override
	public javax.swing.Icon getIcon(Object o) {
		return null;

	}

	@Override
	public String getTooltipText(Object o) {
		return null;
	}

	@Override
	public boolean isHtmlDisplayName(Object o) {
		return true;
	}

}
