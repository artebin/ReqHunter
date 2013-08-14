package net.trevize.alexandrine.ui.tupletable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.reqhunter.RequirementDatabase;
import net.trevize.reqhunter.ui.WorkbenchTableHeaderRenderer;
import net.trevize.reqhunter.ui.dialogs.FieldBatchSetterDialog;
import net.trevize.reqhunter.ui.dialogs.RequirementEditionDialog;

import org.netbeans.swing.etable.ETable;

public class TupleTable extends JPanel implements MouseListener,
		ActionListener, KeyListener {

	public static final String ICON_PATH_LIST_ADD = "./gfx/org/freedesktop/tango/list-add.png";
	public static final String ICON_PATH_LIST_REMOVE = "./gfx/org/freedesktop/tango/list-remove.png";

	public static final String ACTION_COMMAND_ADD_NEW_REQUIREMENT = "ACTION_COMMAND_ADD_NEW_REQUIREMENT";
	public static final String ACTION_COMMAND_REMOVE_REQUIREMENTS = "ACTION_COMMAND_REMOVE_REQUIREMENTS";
	public static final String ACTION_COMMAND_FIELD_BATCH_SETTER = "ACTION_COMMAND_FIELD_BATCH_SETTER";

	private static FieldBatchSetterDialog field_batch_setter_dialog;

	private RequirementDatabase req_db;
	private JScrollPane scrollpane;
	private ETable etable;
	private TupleTableModel requirement_table_model;
	private JPopupMenu popup_menu;
	private HashMap<JMenuItem, String> field_batch_setter;

	public TupleTable(RequirementDatabase req_db) {
		this.req_db = req_db;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		etable = new ETable();
		requirement_table_model = new TupleTableModel(
				req_db.getRequirement_list());
		etable.setModel(requirement_table_model);

		//		etable.setRowSorter(new TableRowSorter(requirement_table_model) {
		//			class ComparatorInteger implements Comparator<Integer> {
		//				@Override
		//				public int compare(Integer o1, Integer o2) {
		//					return o1.compareTo(o2);
		//				}
		//			}
		//
		//			class ComparatorString implements Comparator<String> {
		//				@Override
		//				public int compare(String o1, String o2) {
		//					return o1.compareTo(o2);
		//				}
		//			}
		//
		//			@Override
		//			public Comparator<?> getComparator(int column) {
		//				if (column == 0) {
		//					return new ComparatorInteger();
		//				} else {
		//					return new ComparatorString();
		//				}
		//			}
		//		});

		etable.setAutoscrolls(false);

		//etable.setPopupUsedFromTheCorner(true);
		etable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		etable.setDefaultRenderer(String.class, new MultiLineCellRenderer());
		etable.setRowHeight(26);

		for (int i = 0; i < etable.getColumnCount(); ++i) {
			String column_name = etable.getColumnModel().getColumn(i)
					.getHeaderValue().toString();

			Component header_component = etable
					.getColumnModel()
					.getColumn(i)
					.getHeaderRenderer()
					.getTableCellRendererComponent(etable, column_name, false,
							false, 0, 0);

			if (etable.getColumnModel().getColumn(i).getPreferredWidth() < header_component
					.getPreferredSize().getWidth()) {
				etable.getColumnModel()
						.getColumn(i)
						.setPreferredWidth(
								(int) header_component.getPreferredSize()
										.getWidth()
										+ UIManager.getIcon(
												"Table.ascendingSortIcon")
												.getIconWidth() * 2);
			}
		}

		etable.getTableHeader().addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int column_index = etable.getTableHeader().columnAtPoint(
						e.getPoint());

				if (column_index == -1) {
					return;
				}

				int column_width = etable.getColumnModel()
						.getColumn(column_index).getWidth();

				if (e.getWheelRotation() > 0) {
					etable.getColumnModel()
							.getColumn(column_index)
							.setPreferredWidth(
									column_width + e.getScrollAmount() * 5);
					etable.repaint();
				} else {
					etable.getColumnModel()
							.getColumn(column_index)
							.setPreferredWidth(
									column_width - e.getScrollAmount() * 5);
					etable.repaint();
				}
			}
		});

		etable.addMouseListener(this);
		etable.addKeyListener(this);

		//		for (int vci = 0; vci < etable.getColumnCount(); ++vci) {
		//			TableColumn c = etable.getColumnModel().getColumn(vci);
		//			WorkbenchTableHeaderRenderer rhr = new WorkbenchTableHeaderRenderer();
		//			rhr.setHorizontalAlignment(SwingConstants.CENTER);
		//			c.setHeaderRenderer(rhr);
		//		}

		scrollpane = new JScrollPane(etable);
		add(scrollpane, BorderLayout.CENTER);

		/* init search and action panel ************************************************/
		JPanel subpanels = new JPanel();
		subpanels.setLayout(new BoxLayout(subpanels, BoxLayout.Y_AXIS));
		subpanels.add(new SearchPanel(this, new InMemoryRequirementIndex(req_db
				.getRequirement_list())));
		//subpanels.add(new ActionPanel(this));
		add(subpanels, BorderLayout.NORTH);

		/* initializing the popup menu ************************************************/

		popup_menu = new JPopupMenu();
		popup_menu.setBorder(new LineBorder(Color.GRAY));

		JMenu mitem0 = new JMenu("Set Field for selected requirement(s)");
		popup_menu.add(mitem0);
		field_batch_setter = new HashMap<JMenuItem, String>();

		for (String field_label : req_db.getRequirement_list()
				.getRequirements().get(0).getFieldLabels()) {
			JMenuItem mi = new JMenuItem(field_label);
			field_batch_setter.put(mi, field_label);
			mi.setActionCommand(ACTION_COMMAND_FIELD_BATCH_SETTER);
			mi.addActionListener(this);
			mitem0.add(mi);
		}

		JMenuItem mitem1 = new JMenuItem("Add new requirement", new ImageIcon(
				ICON_PATH_LIST_ADD));
		mitem1.setActionCommand(ACTION_COMMAND_ADD_NEW_REQUIREMENT);
		mitem1.addActionListener(this);
		popup_menu.add(mitem1);

		JMenuItem mitem2 = new JMenuItem("Remove requirement(s)",
				new ImageIcon(ICON_PATH_LIST_REMOVE));
		mitem2.setActionCommand(ACTION_COMMAND_REMOVE_REQUIREMENTS);
		mitem2.addActionListener(this);
		popup_menu.add(mitem2);

		field_batch_setter_dialog = new FieldBatchSetterDialog(this);
	}

	public int getRowForFieldValue(String field_label, String field_value) {
		int result = -1;

		int column = req_db.getRequirement_list().getRequirements().get(0)
				.getFieldLabels().indexOf(field_label);
		for (int i = 0; i < etable.getRowCount(); ++i) {
			if (etable.getValueAt(i, column).equals(field_value)) {
				break;
			}
		}

		return result;
	}

	public JTable getTableComponent() {
		return etable;
	}

	/***************************************************************************
	 * implementation of MouseListener
	 **************************************************************************/

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2 && arg0.getButton() == MouseEvent.BUTTON1) {
			System.out.println(etable.getSelectedRow());
			int selected_row = etable.getSelectedRow();
			int model_index = etable.convertRowIndexToModel(selected_row);
			RequirementEditionDialog dialog = new RequirementEditionDialog(
					etable, req_db.getRequirement_list().getRequirements()
							.get(model_index));
			dialog.setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			popup_menu.setVisible(false);
		}

		else

		if (arg0.getButton() == MouseEvent.BUTTON3) {
			/*
			 * if the click is not performed over a selected requirement,
			 * we withdraw the current (eventually multiple) selection and 
			 * select the requirement below the mouse pointer
			 */

			Point p = arg0.getPoint();
			int clicked_row = etable.rowAtPoint(p);

			boolean found = false;
			for (int row : etable.getSelectedRows()) {
				if (clicked_row == row) {
					found = true;
					break;
				}
			}

			if (!found) {
				int clicked_column = etable.columnAtPoint(p);
				etable.changeSelection(clicked_row, clicked_column, false,
						false);
			}

			//we display the JPopupMenu.
			popup_menu.show(etable, arg0.getX(), arg0.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action_command = arg0.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_FIELD_BATCH_SETTER)) {
			//get the field label
			String field_label_to_set = field_batch_setter
					.get(arg0.getSource());

			/* get the implied requirements ***********************************/

			//get the list of the selected rows
			int[] selected_rows = etable.getSelectedRows();

			ArrayList<Tuple> updated_requirements = new ArrayList<Tuple>();
			for (int i = 0; i < selected_rows.length; ++i) {
				int model_index = etable
						.convertRowIndexToModel(selected_rows[i]);
				updated_requirements.add(req_db.getRequirement_list()
						.getRequirements().get(model_index));
			}

			//get the preset value for the dialog
			String field_value_preset = req_db
					.getRequirement_list()
					.getRequirements()
					.get(etable.convertRowIndexToModel(etable.getSelectedRow()))
					.getFieldValue(field_label_to_set);

			//display the dialog
			field_batch_setter_dialog.displayDialog(field_label_to_set,
					field_value_preset, updated_requirements);

			//update the table content and rendering
			((DefaultTableModel) etable.getModel()).fireTableDataChanged();
			etable.repaint();
		}

		else

		if (action_command.equals(ACTION_COMMAND_ADD_NEW_REQUIREMENT)) {
			Tuple req = req_db.getRequirement_list()
					.insertNewRequirement();

			int selected_row = etable.getSelectedRow();
			int model_index = etable.convertRowIndexToModel(selected_row);

			req_db.getRequirement_list().getRequirements()
					.add(model_index, req);
			((TupleTableModel) etable.getModel()).fireTableDataChanged();

			//update the selection and scroll the table
			int new_req_row = etable.convertRowIndexToView(model_index);
			etable.getSelectionModel().setSelectionInterval(new_req_row,
					new_req_row);
			etable.scrollRectToVisible(new Rectangle(etable.getCellRect(
					new_req_row, 0, true)));

			//refresh the display
			etable.updateUI();
		}

		else

		if (action_command.equals(ACTION_COMMAND_REMOVE_REQUIREMENTS)) {
			int[] selection = etable.getSelectedRows();
			Arrays.sort(selection);
			for (int i = 0; i < selection.length; ++i) {
				int model_index = etable.convertRowIndexToModel(selection[i]);
				req_db.getRequirement_list().getRequirements()
						.remove(model_index);
			}

			//update the selection
			etable.getSelectionModel().clearSelection();

			//refresh the display
			etable.updateUI();
		}
	}

	/***************************************************************************
	 * implementation of KeyListener
	 **************************************************************************/

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if ((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0
				&& arg0.getKeyCode() == KeyEvent.VK_U) {

			/*
			 * Verify that only one requirement is selected.
			 */

			int[] selected_rows = etable.getSelectedRows();
			if (selected_rows == null || selected_rows.length == 0
					|| selected_rows.length > 1) {
				System.out
						.println("decrease requirement id: this operation needs one (and only one) selected requirement.");
				return;
			}

			int selected_row = etable.getSelectedRow();

			/*
			 * Verify that the selected requirement is not the first requirement, else
			 * we can't perform the operation.
			 */

			if (selected_row == 0) {
				System.out
						.println("decrease requirement id: can't decrease the first requirement.");
				return;
			}

			int model_item_index = etable.convertRowIndexToModel(selected_row);
			System.out.println("selected: "
					+ req_db.getRequirement_list().getRequirements()
							.get(model_item_index).getId());

			int prev_row = etable.getSelectedRow() - 1;
			int prev_model_item_index = etable.convertRowIndexToModel(prev_row);
			System.out.println("previous: "
					+ req_db.getRequirement_list().getRequirements()
							.get(prev_model_item_index).getId());

			Tuple selected_req = req_db.getRequirement_list()
					.getRequirements().get(model_item_index);
			Tuple prev_req = req_db.getRequirement_list()
					.getRequirements().get(prev_model_item_index);

			String selected_id = selected_req.getId();
			String prev_id = prev_req.getId();

			selected_req.setId(prev_id);
			prev_req.setId(selected_id);

			//update the selection and scroll the table
			etable.getSelectionModel().setSelectionInterval(selected_row - 1,
					selected_row - 1);
			etable.scrollRectToVisible(new Rectangle(etable.getCellRect(
					selected_row - 1, 0, true)));

			//refresh the display
			etable.updateUI();
		}

		else

		if ((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0
				&& arg0.getKeyCode() == KeyEvent.VK_D) {

			/*
			 * Verify that only one requirement is selected.
			 */

			int[] selected_rows = etable.getSelectedRows();
			if (selected_rows == null || selected_rows.length == 0
					|| selected_rows.length > 1) {
				System.out
						.println("increase requirement id: this operation needs one (and only one) selected requirement.");
				return;
			}

			int selected_row = etable.getSelectedRow();

			/*
			 * Verify that the selected requirement is not the last requirement, else
			 * we can't perform the operation.
			 */

			if (selected_row == etable.getRowCount() - 1) {
				System.out
						.println("increase requirement id: can't increase the last requirement.");
				return;
			}

			int model_item_index = etable.convertRowIndexToModel(selected_row);
			System.out.println("selected: "
					+ req_db.getRequirement_list().getRequirements()
							.get(model_item_index).getId());

			int next_row = etable.getSelectedRow() + 1;
			int next_model_item_index = etable.convertRowIndexToModel(next_row);
			System.out.println("next: "
					+ req_db.getRequirement_list().getRequirements()
							.get(next_model_item_index).getId());

			Tuple selected_req = req_db.getRequirement_list()
					.getRequirements().get(model_item_index);
			Tuple next_req = req_db.getRequirement_list()
					.getRequirements().get(next_model_item_index);

			String selected_id = selected_req.getId();
			String next_id = next_req.getId();

			selected_req.setId(next_id);
			next_req.setId(selected_id);

			//update the selection and scroll the table
			etable.getSelectionModel().setSelectionInterval(selected_row + 1,
					selected_row + 1);
			etable.scrollRectToVisible(new Rectangle(etable.getCellRect(
					selected_row + 1, 0, true)));

			//refresh the display
			etable.updateUI();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
