package net.trevize.alexandrine.ui.tupletable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.trevize.tinker.CellStyle;
import net.trevize.tinker.XGridBag;

public class ActionPanel extends JPanel implements ActionListener {

	public static final String ICON_PATH_LIST_ADD = "./gfx/org/freedesktop/tango/list-add.png";
	public static final String ICON_PATH_LIST_REMOVE = "./gfx/org/freedesktop/tango/list-remove.png";

	public static final String ACTION_COMMAND_ADD_NEW_REQUIREMENT = "ACTION_COMMAND_ADD_NEW_REQUIREMENT";
	public static final String ACTION_COMMAND_REMOVE_REQUIREMENT = "ACTION_COMMAND_REMOVE_REQUIREMENT";

	private TupleTable requirement_table;
	private XGridBag xgb;

	public ActionPanel(TupleTable requirement_table) {
		this.requirement_table = requirement_table;
		init();
	}

	private void init() {
		setLayout(new GridBagLayout());
		xgb = new XGridBag(this);
		setBorder(new EmptyBorder(2, 0, 0, 0));

		CellStyle style0 = new CellStyle(0., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

		int next_column = 0;

		JButton b_add_new_requirement = new JButton("Add new requirement",
				new ImageIcon(ICON_PATH_LIST_ADD));
		xgb.add(b_add_new_requirement, style0, 0, next_column++);

		JButton b_remove_requirements = new JButton("Remove requirements",
				new ImageIcon(ICON_PATH_LIST_REMOVE));
		xgb.add(b_remove_requirements, style0, 0, next_column++);

		CellStyle style1 = new CellStyle(1., 0., GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(Box.createHorizontalGlue(), style1, 0, next_column++);
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

}
