package net.trevize.reqhunter.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.drop.InteriorDropInfo;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import net.trevize.reqhunter.ReqHunterProperties;
import net.trevize.reqhunter.RequirementDatabase;
import net.trevize.reqhunter.ui.dialogs.AboutDialog;
import net.trevize.reqhunter.ui.dialogs.HelpDialog;

public class ReqHunterGUI implements ComponentListener, ActionListener {

	public static final String ACTION_COMMAND_SAVE_ALL = "ACTION_COMMAND_SAVE_ALL";
	public static final String ACTION_COMMAND_EXIT = "ACTION_COMMAND_EXIT";
	public static final String ACTION_COMMAND_ABOUT = "ACTION_COMMAND_ABOUT";
	public static final String ACTION_COMMAND_HELP = "ACTION_COMMAND_HELP";

	private JFrame main_frame;

	//Infonode components
	private RootWindow root_window;
	private SplitWindow split_window;
	private ViewMap view_map;
	private static final String VIEW_TITLE_WORKBENCH = "Workbench";
	private static final String VIEW_TITLE_WELCOME = "Welcome";
	private HashMap<String, View> views;
	private TabWindow tab_window_workbench;
	private TabWindow tab_window_welcome;

	private Workbench workbench;

	private AboutDialog about_dialog;
	private HelpDialog help_dialog;

	public ReqHunterGUI(Workbench workbench) {
		this.workbench = workbench;

		/*
		 * main initialization: application components and GUI components.
		 */
		init();

		//set the Infonode theme.
		try {
			InfonodeHelper.setInfonodeTheme((DockingWindowsTheme) Class
					.forName(ReqHunterProperties.getDocking_windows_theme())
					.newInstance(), root_window);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		main_frame.setVisible(true);
	}

	public void addRequirementDatabaseUI(RequirementDatabase req_db) {
		/* Requirements table view ********************************************/
		String view_title_req_table = req_db.getDatabase_name()
				+ ": Requirements table";
		View view_req_table = new View(view_title_req_table, null,
				req_db.getRequirement_table());
		views.put(view_title_req_table, view_req_table);
		view_map.addView(views.size() - 1, view_req_table);

		/* Requirement description view ***************************************/
		String view_title_req_description = req_db.getDatabase_name()
				+ ": Requirements description";
		View view_req_description = new View(view_title_req_description, null,
				req_db.getRequirement_content_panel());
		views.put(view_title_req_description, view_req_description);
		view_map.addView(views.size() - 1, view_req_description);

		/* the TabWindow for this database ************************************/
		TabWindow tab_window = new TabWindow(new View[] { view_req_table,
				view_req_description });
		tab_window.setSelectedTab(0);
		//InfonodeHelper.initInfonodeTabWindow(tab_window);

		if (views.get(VIEW_TITLE_WELCOME).isVisible()) {
			tab_window_welcome.addTab(view_req_table);
			tab_window_welcome.addTab(view_req_description);
		} else {

		}
	}

	private void init() {
		/* setting the main frame *********************************************/

		main_frame = new JFrame("ReqHunter");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame
				.setSize(
						(int) (Toolkit.getDefaultToolkit().getScreenSize().width * .80),
						(int) (Toolkit.getDefaultToolkit().getScreenSize().height * .80));
		main_frame.setLocationRelativeTo(null);

		main_frame.getContentPane().setLayout(new BorderLayout());

		/*
		 * the following is for hiding popup menus or popup menu buttons 
		 * (if any) when the main window is resized.
		 */
		main_frame.getContentPane().addComponentListener(this);

		/* instantiate the GUI ************************************************/
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				views = new HashMap<String, View>();
				view_map = new ViewMap();

				/* Welcome view ***********************************************/
				View view = new View(VIEW_TITLE_WELCOME, null, new JPanel());
				views.put(VIEW_TITLE_WELCOME, view);
				view_map.addView(views.size() - 1, view);

				/* Workbench view ************************************/
				view = new View(VIEW_TITLE_WORKBENCH, null,
						new WorkbenchManagerPanel(ReqHunterGUI.this, workbench));
				views.put(VIEW_TITLE_WORKBENCH, view);
				view_map.addView(views.size() - 1, view);

				/* Root window creation, configuration and addition of the views *******/
				root_window = DockingUtil.createRootWindow(view_map, true);
				main_frame.getContentPane().add(root_window,
						BorderLayout.CENTER);
				root_window.getRootWindowProperties().setRecursiveTabsEnabled(
						false);
				root_window.getRootWindowProperties()
						.getFloatingWindowProperties().setUseFrame(true);
				root_window.getRootWindowProperties().getTabWindowProperties()
						.getTabProperties().getTitledTabProperties()
						.setFocusMarkerEnabled(false);
				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getTabProperties()
						.getNormalButtonProperties().getCloseButtonProperties()
						.setVisible(true);
				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getTabProperties()
						.getNormalButtonProperties().getDockButtonProperties()
						.setVisible(true);
				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getTabProperties()
						.getNormalButtonProperties()
						.getMinimizeButtonProperties().setVisible(true);
				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getTabProperties()
						.getNormalButtonProperties()
						.getRestoreButtonProperties().setVisible(true);
				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getTabProperties()
						.getNormalButtonProperties()
						.getUndockButtonProperties().setVisible(true);

				//removing the ugly border on the InfoNode default theme
				root_window.getRootWindowProperties().getWindowAreaProperties()
						.setBorder(new EmptyBorder(0, 0, 0, 0));


				root_window.getWindowBar(Direction.RIGHT).setEnabled(true);
				root_window.getWindowBar(Direction.RIGHT).setContentPanelSize(
						256);

				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getDropFilterProperties()
						.setInteriorDropFilter(new DropFilter() {
							public boolean acceptDrop(DropInfo dropInfo) {
								InteriorDropInfo interiorDropInfo = (InteriorDropInfo) dropInfo;
								// If the drop window is a split window and the drag window is a
								// tab window, no drops are allowed
								if (interiorDropInfo.getDropWindow() instanceof SplitWindow
										&& interiorDropInfo.getWindow() instanceof TabWindow)
									return false;
								return true;
							}
						});

				/* creates TabWindow and SplitWindow for the initial frame layout **************/

				//the TabWindow for Workbench Panel
				tab_window_workbench = new TabWindow(new View[] { views
						.get(VIEW_TITLE_WORKBENCH) });
				tab_window_workbench.setSelectedTab(0);
				//InfonodeHelper.initInfonodeTabWindow(tab_window_workbench);

				//the TabWindow for Welcome Panel
				tab_window_welcome = new TabWindow(new View[] { views
						.get(VIEW_TITLE_WELCOME) });
				tab_window_welcome.setSelectedTab(0);
				//InfonodeHelper.initInfonodeTabWindow(tab_window_welcome);

				//a splitwindow for separating the two main views
				split_window = new SplitWindow(true, 0.20f,
						tab_window_workbench, tab_window_welcome);

				root_window.setWindow(split_window);
			}
		});

		//initialize the dialogs
		about_dialog = new AboutDialog(main_frame.getRootPane());
		help_dialog = new HelpDialog(main_frame.getRootPane());

		//initialize the main frame menu bar (the Application MenuBar)
		initApplicationMenuBar();
	}

	private void initApplicationMenuBar() {
		JMenuBar menu_bar = new JMenuBar();
		main_frame.setJMenuBar(menu_bar);

		/* File menu **********************************************************/

		JMenu menu1 = new JMenu("File");
		menu1.setMnemonic('F');
		menu_bar.add(menu1);

		JMenuItem item12 = new JMenuItem("Save");
		item12.setMnemonic('x');
		item12.setActionCommand(ACTION_COMMAND_SAVE_ALL);
		item12.addActionListener(this);
		menu1.add(item12);

		menu1.add(new JSeparator());

		JMenuItem item6 = new JMenuItem("Exit");
		item6.setMnemonic('x');
		item6.setActionCommand(ACTION_COMMAND_EXIT);
		item6.addActionListener(this);
		menu1.add(item6);

		/* Views menu *********************************************************/

		JMenu menu2 = new JMenu("Views");
		menu2.setMnemonic('V');
		menu_bar.add(menu2);

		//		final JCheckBox item2 = new JCheckBox(
		//				views_titles[VIEW_REQUIREMENTS_TABLE]);
		//		item2.setBorder(new JMenuItem().getBorder());
		//		item2.setSelected(true);
		//		item2.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				if (!item2.isSelected()) {
		//					views[VIEW_REQUIREMENTS_TABLE].close();
		//				} else {
		//					views[VIEW_REQUIREMENTS_TABLE].restore();
		//				}
		//			}
		//		});
		//		views[VIEW_REQUIREMENTS_TABLE].addListener(new DockingWindowListener() {
		//			@Override
		//			public void windowUndocking(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowUndocked(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowShown(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowRestoring(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowRestored(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
		//			}
		//
		//			@Override
		//			public void windowMinimizing(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowMinimized(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowMaximizing(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowMaximized(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowHidden(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowDocking(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowDocked(DockingWindow arg0) {
		//			}
		//
		//			@Override
		//			public void windowClosing(DockingWindow arg0)
		//					throws OperationAbortedException {
		//			}
		//
		//			@Override
		//			public void windowClosed(DockingWindow arg0) {
		//				item2.setSelected(false);
		//			}
		//
		//			@Override
		//			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
		//			}
		//
		//			@Override
		//			public void viewFocusChanged(View arg0, View arg1) {
		//			}
		//		});
		//		menu2.add(item2);
		//
		//		/**********************************************************************/
		//
		//		final JCheckBox item11 = new JCheckBox(
		//				views_titles[VIEW_REQUIREMENT_DESCRIPTION]);
		//		item11.setBorder(new JMenuItem().getBorder());
		//		item11.setSelected(true);
		//		item11.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				if (!item11.isSelected()) {
		//					views[VIEW_REQUIREMENT_DESCRIPTION].close();
		//				} else {
		//					views[VIEW_REQUIREMENT_DESCRIPTION].restore();
		//				}
		//			}
		//		});
		//		views[VIEW_REQUIREMENT_DESCRIPTION]
		//				.addListener(new DockingWindowListener() {
		//					@Override
		//					public void windowUndocking(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowUndocked(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowShown(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowRestoring(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowRestored(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowRemoved(DockingWindow arg0,
		//							DockingWindow arg1) {
		//					}
		//
		//					@Override
		//					public void windowMinimizing(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowMinimized(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowMaximizing(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowMaximized(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowHidden(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowDocking(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowDocked(DockingWindow arg0) {
		//					}
		//
		//					@Override
		//					public void windowClosing(DockingWindow arg0)
		//							throws OperationAbortedException {
		//					}
		//
		//					@Override
		//					public void windowClosed(DockingWindow arg0) {
		//						item11.setSelected(false);
		//					}
		//
		//					@Override
		//					public void windowAdded(DockingWindow arg0,
		//							DockingWindow arg1) {
		//					}
		//
		//					@Override
		//					public void viewFocusChanged(View arg0, View arg1) {
		//					}
		//				});
		//		menu2.add(item11);

		/* Help menu **********************************************************/

		JMenu menu3 = new JMenu("Help");
		menu3.setMnemonic('H');
		menu_bar.add(menu3);

		JMenuItem item1 = new JMenuItem("About");
		item1.setMnemonic('A');
		item1.setActionCommand(ACTION_COMMAND_ABOUT);
		item1.addActionListener(this);
		menu3.add(item1);

		JMenuItem item9 = new JMenuItem("Help");
		item9.setMnemonic('H');
		item9.setActionCommand(ACTION_COMMAND_HELP);
		item9.addActionListener(this);
		menu3.add(item9);
	}

	public View getView(String view_title) {
		return views.get(view_title);
	}

	/***************************************************************************
	 * implementation of ComponentListener.
	 **************************************************************************/

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_SAVE_ALL)) {
			//			requirement_list.writeRequirements(ReqHunterProperties
			//					.getRequirements_file_path());
		}

		else

		if (action_command.equals(ACTION_COMMAND_EXIT)) {
			workbench.writeRC();
			System.exit(0);
		}

		else

		if (action_command.equals(ACTION_COMMAND_ABOUT)) {
			about_dialog.setVisible(true);
		}

		else

		if (action_command.equals(ACTION_COMMAND_HELP)) {
			help_dialog.setVisible(true);
		}
	}

	public JFrame getMain_frame() {
		return main_frame;
	}

}
