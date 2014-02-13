package gpigb.classloading;

import java.util.List;

public interface ComponentManager<Interface>
{
	public static final String displayNameFieldName = "DISPLAY_NAME";
	public static final String versionNumberFieldName = "VERSION_NUMBER";

	/**
	 * Add a directory to be scanned for available modules. Sub directories
	 * should not be scanned in most cases.
	 * 
	 * @param path
	 *            The directory path to be added
	 * @return true iff the path is a valid directory and will be scanned on
	 *         future refreshes
	 */
	public boolean addModuleDirectory(String path);

	/**
	 * Refreshes the available modules, canning the directories provided and
	 * updating references as required
	 */
	public void refreshModules();

	/**
	 * Provides a list of {@link ModuleSummary}s for the loaded modules.
	 * 
	 * @return A List<{@link ModuleSummary}> of loaded modules.
	 */
	public List<ModuleSummary> getAvailableModules();

	/**
	 * Instantiate an object of the specified module type
	 * 
	 * @param moduleID
	 *            The type of object to create
	 * @return The ID of the created object or -1 if an error occurred
	 */
	public int createObjectOfModule(int moduleID);

	/**
	 * Provides a list of instantiated objects
	 * 
	 * @return A List of object summaries
	 */
	public List<InstanceSummary> getAvailableObjects();

	/**
	 * Retrieve a reference to an object with the given ID
	 * 
	 * @param instanceID
	 *            the ID of the object to be retrieved
	 * @return A {@link gpigb.classloading.StrongReference} to the requested
	 *         object, or null if an error occurs
	 */
	public StrongReference<Interface> getObjectByID(int instanceID);

	public class ModuleSummary
	{
		public final String displayName;
		public final int moduleID;
		public int moduleVersion;

		public ModuleSummary(int moduleID, int moduleVersion, String displayName)
		{
			this.displayName = displayName;
			this.moduleID = moduleID;
			this.moduleVersion = moduleVersion;
		}

		@Override
		public String toString()
		{
			return "Module " + moduleID + ": " + this.displayName + "(version " + this.moduleVersion + ")";
		}
	}

	public class InstanceSummary
	{
		public final String displayName;
		public final int instanceID;
		public final int moduleID;

		public InstanceSummary(int instanceID, int moduleID, String displayName)
		{
			this.displayName = displayName;
			this.instanceID = instanceID;
			this.moduleID = moduleID;
		}
	}
}
