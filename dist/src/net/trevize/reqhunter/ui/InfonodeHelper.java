package net.trevize.reqhunter.ui;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;

public class InfonodeHelper {

	public static void setInfonodeTheme(
			DockingWindowsTheme docking_windows_theme, RootWindow root_window) {
		RootWindowProperties properties = new RootWindowProperties();

		// Set gradient theme. The theme properties object is the super object
		// of our properties object, which
		// means our property value settings will override the theme values
		properties.addSuperObject(docking_windows_theme
				.getRootWindowProperties());

		// Our properties object is the super object of the root window
		// properties object, so all property values of the
		// theme and in our property object will be used by the root window
		root_window.getRootWindowProperties().addSuperObject(properties);
	}

	public static void initInfonodeView(View view) {
		view.getWindowProperties().getDropFilterProperties()
				.setChildDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setInsertTabDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setInteriorDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setSplitDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
	}

	public static void initInfonodeTabWindow(final TabWindow tab_window) {
		tab_window.addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
				int window_count = tab_window.getChildWindowCount();
				for (int i = 0; i < window_count; ++i) {
					DockingWindow window = tab_window.getChildWindow(i);
					window.close();
				}
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
	}

}
