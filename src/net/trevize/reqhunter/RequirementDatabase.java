package net.trevize.reqhunter;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.alexandrine.api.TupleFactory;
import net.trevize.alexandrine.api.TupleList;
import net.trevize.alexandrine.ui.tupletable.InMemoryRequirementIndex;
import net.trevize.alexandrine.ui.tupletable.TupleTable;
import net.trevize.reqhunter.ui.RequirementDisplayerPanel;
import net.trevize.reqhunter.ui.dialogs.FieldBatchSetterDialog;

public class RequirementDatabase implements ListSelectionListener {

	private String requirements_file_path;
	private String database_name;
	private String factory_name;
	private TupleFactory factory;
	private TupleList requirement_list;

	private InMemoryRequirementIndex index1; //case-sensitive search
	private InMemoryRequirementIndex index2; //case-insensitive search

	//user graphical interface components
	private TupleTable requirement_table;
	private RequirementDisplayerPanel requirement_content_panel;
	private FieldBatchSetterDialog field_batch_setter_dialog;

	public RequirementDatabase(String requirements_file_path,
			String database_name, String reqhunter_factory_implementation_name) {
		this.requirements_file_path = requirements_file_path;
		this.database_name = database_name;
		this.factory_name = reqhunter_factory_implementation_name;

		try {
			factory = (TupleFactory) Class.forName(
					reqhunter_factory_implementation_name).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		requirement_list = factory.createRequirementList();

		requirement_list.loadRequirements(requirements_file_path);
		index1 = new InMemoryRequirementIndex(requirement_list);

		initUI();
	}

	private void initUI() {
		//init ReqTable
		requirement_table = new TupleTable(this);
		requirement_table.getTableComponent().getSelectionModel()
				.addListSelectionListener(this);

		//init requirement description panel
		requirement_content_panel = new RequirementDisplayerPanel();
	}

	public String getRequirements_file_path() {
		return requirements_file_path;
	}

	public void setRequirements_file_path(String requirements_file_path) {
		this.requirements_file_path = requirements_file_path;
	}

	public String getDatabase_name() {
		return database_name;
	}

	public void setDatabase_name(String database_name) {
		this.database_name = database_name;
	}

	public TupleList getRequirement_list() {
		return requirement_list;
	}

	public void setRequirement_list(TupleList requirement_list) {
		this.requirement_list = requirement_list;
	}

	public InMemoryRequirementIndex getIndex1() {
		return index1;
	}

	public InMemoryRequirementIndex getIndex2() {
		return index2;
	}

	/***************************************************************************
	 * implementation of ListSelectionListener
	 **************************************************************************/

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = requirement_table.getTableComponent().getSelectedRow();
		row = requirement_table.getTableComponent().convertRowIndexToModel(row);
		if (row != -1) {
			Tuple req = requirement_list.getRequirements().get(row);
			if (requirement_content_panel.getDisplayedRequirement() != req) {
				requirement_content_panel.setRequirement(req);
			}
		}
	}

	public String getFactory_name() {
		return factory_name;
	}

	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}

	public TupleFactory getFactory() {
		return factory;
	}

	public void setFactory(TupleFactory factory) {
		this.factory = factory;
	}

	public TupleTable getRequirement_table() {
		return requirement_table;
	}

	public void setRequirement_table(TupleTable requirement_table) {
		this.requirement_table = requirement_table;
	}

	public RequirementDisplayerPanel getRequirement_content_panel() {
		return requirement_content_panel;
	}

	public void setRequirement_content_panel(
			RequirementDisplayerPanel requirement_content_panel) {
		this.requirement_content_panel = requirement_content_panel;
	}

	public FieldBatchSetterDialog getField_batch_setter_dialog() {
		return field_batch_setter_dialog;
	}

	public void setField_batch_setter_dialog(
			FieldBatchSetterDialog field_batch_setter_dialog) {
		this.field_batch_setter_dialog = field_batch_setter_dialog;
	}

	public void setIndex1(InMemoryRequirementIndex index1) {
		this.index1 = index1;
	}

	public void setIndex2(InMemoryRequirementIndex index2) {
		this.index2 = index2;
	}

}
