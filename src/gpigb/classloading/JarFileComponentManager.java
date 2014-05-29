package gpigb.classloading;

import gpigb.configuration.Configurable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileComponentManager<Interface> implements ComponentManager<Interface>
{
	// Maintain a record of available plugins and searchable directories
	Set<File> moduleDirectories = Collections.synchronizedSet(new HashSet<File>());
	Map<Integer, ClassRecord> modules = Collections.synchronizedMap(new HashMap<Integer, ClassRecord>());

	// And references to instantiated objects
	Map<InstanceSummary, StrongReference<Interface>> instances = Collections
			.synchronizedMap(new HashMap<InstanceSummary, StrongReference<Interface>>());

	// Used to ensure that discovered plugins are applicable to this loader
	private Class<?> classAncestor;

	// A new class loader is created on each refresh to allow reloading of
	// classes
	private GPIGClassLoader loader;
	

	/**
	 * Maintains a list of plugin directories which will be scanned for JAR
	 * files. For any JAR file found in the available search directories each
	 * will be scanned and all valid classes loaded. Any existing objects of
	 * this type will be updated.
	 * 
	 * @param superClass
	 *            An ancestor of any plugins that should be loaded
	 */
	public JarFileComponentManager(Class<? extends Interface> superClass)
	{
		this.classAncestor = superClass;
	}

	public Integer getModuleIDByName(String searchName)
	{
		List<ModuleSummary> aLoaded = this.getAvailableModules();
		Iterator<ModuleSummary> li = aLoaded.iterator();
		Integer moduleID = null;
		while(li.hasNext())
		{
			ModuleSummary loadedModule = li.next();
			System.out.println(loadedModule.displayName);
			if(loadedModule.displayName.equals(searchName) )
			{
				moduleID = loadedModule.moduleID;
			}
		}
		
		return moduleID;
	}
	
	/**
	 * Adds a new directory to the list of searchable directories.
	 * 
	 * @param path
	 *            The path to be added
	 * @return true iff the path passed refers to a valid directory
	 */
	@Override
	public boolean addModuleDirectory(String path)
	{
		path = path.replace("~", System.getProperty("user.home"));
		System.out.println("Path: " + path);
		File newDir = new File(path);

		if (newDir.exists() && newDir.isDirectory()) {
			moduleDirectories.add(newDir);
			return true;
		}

		return false;
	}

	/**
	 * Refreshes the available plugin directories, loading new versions of
	 * classes and updating existing references as required. Does not scan
	 * subdirectories.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void refreshModules()
	{
		// Scan each directory in turn to find JAR files
		// Subdirectories are not scanned
		List<File> jarFiles = new ArrayList<>();

		for (File dir : moduleDirectories) {
			for (File jarFile : dir.listFiles(jarFileFilter)) {
				jarFiles.add(jarFile);
			}
		}

		// Create a new class loader to ensure there are no class name clashes.
		loader = new GPIGClassLoader(jarFiles);
		for (String className : loader.moduleVersions.keySet()) {
			try {
				// Update the record of each class
				Class<? extends Interface> clz = (Class<? extends Interface>) loader.loadClass(className);
				ClassRecord rec = null;
				for (ClassRecord searchRec : modules.values()) {
					if (searchRec.clz.getName().equals(className)) {
						rec = searchRec;
						break;
					}
				}

				if (rec != null) {
					// This is not an upgrade, ignore it
					if (rec.summary.moduleVersion >= loader.moduleVersions.get(className)) continue;

					// Otherwise update the version number stored
					rec.summary.moduleVersion = loader.moduleVersions.get(className);
				}
				else {
					rec = new ClassRecord();
					rec.summary = new ModuleSummary(IDGenerator.getNextID(), loader.moduleVersions.get(className),
							className);
					modules.put(rec.summary.moduleID, rec);
				}
				rec.clz = clz;

				// Update references to existing objects
				for (StrongReference<Interface> ref : instances.values()) {
					if (ref.get().getClass().getName().equals(className)) {
						Constructor<? extends Interface> ctor = clz.getConstructor(Object.class);
						ref.object = ctor.newInstance(ref.get());
					}
				}
			}
			catch (NoSuchMethodException e) {
				// Thrown when trying to find a suitable constructor
				System.err.println("Discovered class which has no available upgrade constructor: " + className + "\n\t"
						+ e.getLocalizedMessage());
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// All thrown by the instantiate call
				System.err.println("Unable to create new instance of class: " + className + "\n\t"
						+ e.getLocalizedMessage());
			}
			catch (ClassNotFoundException e) {
				// Should never occur but required to stop the compiler moaning
				System.err.println("Discovered class which has no available upgrade constructor: " + className + "\n\t"
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Provides a list of all available modules
	 * 
	 * @return A List of
	 *         {@link gpigb.classloading.ComponentManager.ModuleSummary}s which
	 *         have been loaded
	 */
	@Override
	public List<ModuleSummary> getAvailableModules()
	{
		List<ModuleSummary> ret = new ArrayList<>();
		for (ClassRecord rec : modules.values()) {
			ret.add(rec.summary);
		}
		return ret;
	}

	/**
	 * Given a module ID, as returned by {@link #getAvailableModules()},
	 * instantiate a new object and return its ID
	 * 
	 * @param moduleID
	 *            The ID relating to the class of the object to be created
	 * @return The ID of the newly created object, or -1 if an object could not
	 *         be created
	 */
	@Override
	public int createObjectOfModule(int moduleID)
	{
		int ret = -1;
		try {
			// Lookup the required module and create a new instance and record
			Interface instance = modules.get(moduleID).clz.newInstance();
			ret = IDGenerator.getNextID();
			if(instance instanceof Configurable)
				((Configurable)instance).setID(ret);
			instances.put(new InstanceSummary(ret, moduleID, instance.getClass().getSimpleName()), new StrongReference<Interface>(instance));
		}
		catch (Exception e) {
			System.err.println("Failed to instantiate object of type " + moduleID + "\n\t" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Similar to {@link #getAvailableModules()} but for use with instantiated
	 * objects
	 * 
	 * @return A List of
	 *         {@link gpigb.classloading.ComponentManager.InstanceSummary}s
	 */
	@Override
	public List<InstanceSummary> getAvailableObjects()
	{
		return new ArrayList<InstanceSummary>(instances.keySet());
	}

	/**
	 * Acquire a reference to an instantiated object
	 * 
	 * @param instanceID
	 *            The ID of the object to be returned
	 * @return A {@link gpigb.classloading.StrongReference} to the requested
	 *         object, or null if an invalid ID is passed
	 */
	@Override
	public StrongReference<Interface> getObjectByID(int instanceID)
	{
		for (InstanceSummary summary : instances.keySet()) {
			if (summary.instanceID == instanceID) { return instances.get(summary); }
		}
		return null;
	}

	/**
	 * Where the magic happens. Upon instantiation scans the JAR files provided.
	 * Each class is loaded in turn (unconditionally) and a record is created
	 * iff the class is relevant.
	 */
	private class GPIGClassLoader extends ClassLoader
	{
		private HashMap<String, Integer> moduleVersions;

		public GPIGClassLoader(List<File> jarFiles)
		{
			super();
			this.moduleVersions = new HashMap<>();

			for (File file : jarFiles) {
				JarFile jarFile = null;
				try {
					// Open the JAR file and read its contents
					jarFile = new JarFile(file);
					Enumeration<JarEntry> e = jarFile.entries();
					JarEntry entry;
					while (e.hasMoreElements()) {
						entry = e.nextElement();

						// For each class file found
						if (entry.getName().endsWith(".class")) {
							// Read it into memory
							byte[] buffer = new byte[1024];
							InputStream is = jarFile.getInputStream(entry);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();

							int bytesRead;
							do {
								bytesRead = is.read(buffer, 0, 1024);
								if (bytesRead <= 0) break;

								baos.write(buffer, 0, bytesRead);
							} while (bytesRead > 0);

							baos.flush();

							// Determine its name from its path within the JAR
							// file
							String className = entry.getName();
							className = className.substring(0, className.length() - ".class".length());
							className = className.replace('/', '.');
							className = className.replace('\\', '.');

							// And load it into the JVM
							Class<?> clz = defineClass(className, baos.toByteArray(), 0, baos.size());

							// If the class is not one of interest the do no
							// more
							if (!classAncestor.isAssignableFrom(clz)) continue;

							// Otherwise extract its version number (if present)
							int version = -1;
							for (Field f : clz.getFields()) {
								if (f.getName().equals(versionNumberFieldName)) {
									version = f.getInt(clz);
								}
							}

							// And maintain a record of it
							moduleVersions.put(className, version);
						}
					}
				}
				catch (Exception e) {
				}
				finally {
					if (jarFile != null) try {
						jarFile.close();
					}
					catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * A small utility class to provide storage for modules and related
	 * information
	 */
	private class ClassRecord
	{
		private Class<? extends Interface> clz;
		private ModuleSummary summary;
	}

	/**
	 * Used to filter out JAR files based on name
	 */
	private static FilenameFilter jarFileFilter = new FilenameFilter()
	{
		@Override
		public boolean accept(File dir, String name)
		{
			return name.endsWith(".jar");
		}
	};
}
