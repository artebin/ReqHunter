package net.trevize.reqhunter.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import net.trevize.reqhunter.RequirementDatabase;

public class Workbench extends DefaultTableModel {

	private Map<String, RequirementDatabase> databases;

	public Workbench() {
		super();
		databases = new HashMap<String, RequirementDatabase>();
	}

	public void addRequirementDatabase(RequirementDatabase req_db) {
		databases.put(req_db.getDatabase_name(), req_db);
	}

	public void writeRC() {
		File rc_file = new File("./reqhunter.rc");
		if (rc_file.exists()) {
			rc_file.delete();
			try {
				rc_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileWriter fw = new FileWriter(rc_file);
			StringBuffer sb = new StringBuffer();
			for (String key : databases.keySet()) {
				sb.append(key + ",");
				RequirementDatabase req_db = databases.get(key);
				sb.append(req_db.getRequirements_file_path() + ",");
				sb.append(req_db.getFactory().getClass().getCanonicalName());
				sb.append("\n");
			}
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadRC() {
		File rc_file = new File("./reqhunter.rc");
		if (!rc_file.exists()) {
			return;
		}

		try {
			FileReader fr = new FileReader("./reqhunter.rc");
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				String database_name = fields[0];
				String requirements_file_path = fields[1];
				String factory_name = fields[2];
				databases.put(database_name, new RequirementDatabase(
						requirements_file_path, database_name, factory_name));
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * implementation of TableModel
	 **************************************************************************/

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return "Database name";
	}

	@Override
	public int getRowCount() {
		if (databases == null || databases.keySet() == null) {
			return 0;
		} else {
			return databases.keySet().size();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return (RequirementDatabase) (databases.values().toArray()[rowIndex]);
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}
