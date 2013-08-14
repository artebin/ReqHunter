package net.trevize.reqhunter;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.trevize.reqhunter.plugins.ReqHunterPlugin;
import net.trevize.reqhunter.plugins.ReqHunterPluginType;
import net.trevize.reqhunter.ui.ReqHunterGUI;
import net.trevize.reqhunter.ui.Workbench;

import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLocation;

public class ReqHunter {

	public static void main(String[] args) {
		ReqHunter.setLookAndFeel();
		ReqHunter.loadPlugins();
		ReqHunter.launchRequirementManager();
	}

	private static void setLookAndFeel() {
		//setting the look and feel
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		} else {
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}

		if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())) {
			UIManager.put("FileChooserUI",
					"eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI");
		}
	}

	private static PluginManager plugin_manager;
	private static HashMap<String, PluginDescriptor> requirement_binding_plugin_descriptors;

	public static PluginManager getPlugin_manager() {
		return plugin_manager;
	}

	public static HashMap<String, PluginDescriptor> getRequirement_binding_plugin_descriptors() {
		return requirement_binding_plugin_descriptors;
	}

	private static void loadPlugins() {
		// create a plugin manager.
		plugin_manager = ObjectFactory.newInstance().createManager();

		// retrieve the list of the plugin archives.
		File plugins_dir = new File("./plugins");
		File[] plugins = plugins_dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		// if no plugins found we return.
		if (plugins == null) {
			return;
		}

		// get PluginLocation objects and publish the plugins.
		try {
			PluginLocation[] locations = new PluginLocation[plugins.length];

			for (int i = 0; i < plugins.length; i++) {
				locations[i] = StandardPluginLocation.create(plugins[i]);
			}

			plugin_manager.publishPlugins(locations);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// iterate over all the found plugins.
		Iterator<PluginDescriptor> it = plugin_manager.getRegistry()
				.getPluginDescriptors().iterator();

		System.out.println(plugin_manager.getRegistry().getPluginDescriptors()
				.size()
				+ " plugins found.");

		requirement_binding_plugin_descriptors = new HashMap<String, PluginDescriptor>();

		while (it.hasNext()) {
			PluginDescriptor p = (PluginDescriptor) it.next();
			try {
				ReqHunterPlugin reqhunter_plugin = (ReqHunterPlugin) plugin_manager
						.getPlugin(p.getId());
				String plugin_name = reqhunter_plugin.getPluginName();
				System.out.println("Detected plugin: " + plugin_name);

				if (reqhunter_plugin
						.getPluginType()
						.equals(ReqHunterPluginType.REQHUNTER_PLUGIN_TYPE_REQUIREMENT_BINDING)) {
					requirement_binding_plugin_descriptors.put(plugin_name, p);
					URL jar_url = null;
					String jar_url_value = p.getLocation().toString();
					//retrieve the name of the Jar file
					//the p.getLocation() URL is something like jar:file:/...!plugin.xml
					try {
						jar_url = new URL(jar_url_value.substring(
								"jar:".length(), jar_url_value.indexOf("!")));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					addURLsToSystemClassloader(new URL[] { jar_url });
				}
			} catch (PluginLifecycleException e) {
				e.printStackTrace();
			}
		}

		if (requirement_binding_plugin_descriptors.keySet().size() == 0) {
			System.out
					.println("No requirement binding plugin found! But at least one of such a plugin must be present to use ReqHunter.\nExiting...");
			System.exit(1);
		}
	}

	public static void addURLsToSystemClassloader(URL[] urls) {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		if (cl instanceof URLClassLoader) {
			URLClassLoader ul = (URLClassLoader) cl;
			// addURL is a protected method, but we can use reflection to call it
			Class<?>[] paraTypes = new Class[1];
			paraTypes[0] = URL.class;
			Method method = null;
			try {
				method = URLClassLoader.class.getDeclaredMethod("addURL",
						paraTypes);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}
			// change access to true, otherwise, it will throw exception
			method.setAccessible(true);
			Object[] args = new Object[1];
			for (int i = 0; i < urls.length; i++) {
				args[0] = urls[i];
				try {
					method.invoke(ul, args);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} else {
			// SystemClassLoader is not URLClassLoader...
		}
	}

	private static void launchRequirementManager() {
		Workbench workbench = new Workbench();
		workbench.loadRC();
		new ReqHunterGUI(workbench);
	}

}
