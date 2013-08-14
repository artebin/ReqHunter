package net.trevize.alexandrine.ui.tupletable;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.trevize.tinker.CellStyle;
import net.trevize.tinker.XGridBag;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

public class SearchPanel extends JPanel implements KeyListener, ActionListener {

	public static final String ACTION_COMMAND_SAVE = "ACTION_COMMAND_SAVE";
	public static final String ACTION_COMMAND_PREVIOUS_RESULT = "ACTION_COMMAND_PREVIOUS_RESULT";
	public static final String ACTION_COMMAND_NEXT_RESULT = "ACTION_COMMAND_NEXT_RESULT";

	public static final String ICON_PATH_SAVE = "./gfx/org/freedesktop/tango/media-floppy.png";
	public static final String ICON_PATH_GO_PREVIOUS = "./gfx/org/freedesktop/tango/go-previous.png";
	public static final String ICON_PATH_GO_NEXT = "./gfx/org/freedesktop/tango/go-next.png";
	public static final String ICON_PATH_GO_CLOSE = "./gfx/org/freedesktop/tango/close.png";

	private TupleTable requirement_table;
	private InMemoryRequirementIndex index;
	private XGridBag xgb;
	private JButton button_save;
	private JTextField search_textfield;
	private JCheckBox button_match_case;
	private JButton button_previous;
	private JButton button_next;
	private JLabel label_results;
	private int currently_selected_result;

	public SearchPanel(TupleTable requirement_table,
			InMemoryRequirementIndex index) {
		this.requirement_table = requirement_table;
		this.index = index;

		setLayout(new GridBagLayout());
		xgb = new XGridBag(this);
		setBorder(new EmptyBorder(2, 0, 0, 0));

		CellStyle style0 = new CellStyle(0., 1., GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

		int next_column = 0;

		button_save = new JButton(new ImageIcon(ICON_PATH_SAVE));
		button_save.setActionCommand(ACTION_COMMAND_PREVIOUS_RESULT);
		button_save.addActionListener(this);
		xgb.add(button_save, style0, 0, next_column++);

		xgb.add(Box.createHorizontalStrut(5), style0, 0, next_column++);

		JLabel search_label = new JLabel("search: ");
		xgb.add(search_label, style0, 0, next_column++);

		search_textfield = new JTextField();
		search_textfield.setMinimumSize(new Dimension(196, search_textfield
				.getPreferredSize().height));
		search_label.setLabelFor(search_textfield);
		search_textfield.addKeyListener(this);
		search_textfield.setColumns(30);
		xgb.add(search_textfield, style0, 0, next_column++);

		xgb.add(Box.createHorizontalStrut(5), style0, 0, next_column++);

		button_match_case = new JCheckBox("Match case");
		xgb.add(button_match_case, style0, 0, next_column++);

		xgb.add(Box.createHorizontalStrut(5), style0, 0, next_column++);

		button_previous = new JButton(new ImageIcon(ICON_PATH_GO_PREVIOUS));
		button_previous.setActionCommand(ACTION_COMMAND_PREVIOUS_RESULT);
		button_previous.addActionListener(this);
		xgb.add(button_previous, style0, 0, next_column++);

		button_next = new JButton(new ImageIcon(ICON_PATH_GO_NEXT));
		button_next.setActionCommand(ACTION_COMMAND_NEXT_RESULT);
		button_next.addActionListener(this);
		xgb.add(button_next, style0, 0, next_column++);

		xgb.add(Box.createHorizontalStrut(5), style0, 0, next_column++);

		label_results = new JLabel();
		xgb.add(label_results, style0, 0, next_column++);

		CellStyle style1 = new CellStyle(1., 0., GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(Box.createHorizontalGlue(), style1, 0, next_column++);
	}

	public void takeFocus() {
		search_textfield.requestFocus();
	}

	/***************************************************************************
	 * implementation of KeyListener.
	 **************************************************************************/

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			//System.out.println("query=" + search_textfield.getText());

			//do the search.
			index.search(search_textfield.getText());

			//update the label_results.
			int nb_results = index.getTopdocs().totalHits;
			label_results.setText(nb_results + " results.");

			if (nb_results != 0) {
				//get the first result.
				currently_selected_result = 0;
				Document doc = null;
				try {
					doc = index
							.getSearcher()
							.doc(index.getTopdocs().scoreDocs[currently_selected_result].doc);
				} catch (CorruptIndexException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				//select and scroll to the first results.
				int row_index = Integer.parseInt(doc
						.get(InMemoryRequirementIndex.FIELD_ROW_INDEX));
				int row = requirement_table.getTableComponent()
						.convertRowIndexToView(row_index);
				requirement_table.getTableComponent().changeSelection(
						row,
						requirement_table.getTableComponent()
								.getSelectedColumn(), false, false);
				requirement_table.getTableComponent().scrollRectToVisible(
						new Rectangle(requirement_table.getTableComponent()
								.getCellRect(
										row,
										requirement_table.getTableComponent()
												.getSelectedColumn(), true)));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_PREVIOUS_RESULT)) {
			/*
			 * if the user clicks previous but no search has been done.
			 */
			if (index.getTopdocs() == null) {
				return;
			}

			if (currently_selected_result != 0) {
				--currently_selected_result;
				Document doc = null;
				try {
					doc = index
							.getSearcher()
							.doc(index.getTopdocs().scoreDocs[currently_selected_result].doc);
				} catch (CorruptIndexException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				int row_index = Integer.parseInt(doc
						.get(InMemoryRequirementIndex.FIELD_ROW_INDEX));
				int row = requirement_table.getTableComponent()
						.convertRowIndexToView(row_index);
				requirement_table.getTableComponent().changeSelection(
						row,
						requirement_table.getTableComponent()
								.getSelectedColumn(), false, false);
				requirement_table.getTableComponent().scrollRectToVisible(
						new Rectangle(requirement_table.getTableComponent()
								.getCellRect(
										row,
										requirement_table.getTableComponent()
												.getSelectedColumn(), true)));
			}
		}

		else

		if (action_command.equals(ACTION_COMMAND_NEXT_RESULT)) {
			/*
			 * if the user clicks next but no search has been done.
			 */
			if (index.getTopdocs() == null) {
				return;
			}

			if (currently_selected_result < (index.getTopdocs().totalHits - 1)) {
				++currently_selected_result;
				Document doc = null;
				try {
					doc = index
							.getSearcher()
							.doc(index.getTopdocs().scoreDocs[currently_selected_result].doc);
				} catch (CorruptIndexException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				int row_index = Integer.parseInt(doc
						.get(InMemoryRequirementIndex.FIELD_ROW_INDEX));
				int row = requirement_table.getTableComponent()
						.convertRowIndexToView(row_index);
				requirement_table.getTableComponent().changeSelection(
						row,
						requirement_table.getTableComponent()
								.getSelectedColumn(), false, false);
				requirement_table.getTableComponent().scrollRectToVisible(
						new Rectangle(requirement_table.getTableComponent()
								.getCellRect(
										row,
										requirement_table.getTableComponent()
												.getSelectedColumn(), true)));
			}
		}
	}

}
