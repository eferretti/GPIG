package gpigb.classloading;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Patchable
{
	public Patchable()
	{

	}

	/**
	 * Default implementation of a dynamically patchable class. Uses reflection
	 * to extract fields from the old and new objects and assign values based on
	 * common variable names.
	 * 
	 * @param oldInstance
	 *            The object being upgraded from.
	 */
	public Patchable(Object oldInstance)
	{
		if (oldInstance == null) { return; }

		// Obtain a list of old and new fields
		Field[] oldFields = oldInstance.getClass().getDeclaredFields();
		Field[] newFields = this.getClass().getDeclaredFields();

		for (Field newField : newFields) {
			// Skip static fields
			if (Modifier.isStatic(newField.getModifiers())) continue;

			// Scan for a match
			Field matchingField = null;
			for (Field oldField : oldFields) {
				if (oldField.getName() == newField.getName()) {
					matchingField = oldField;
					break;
				}
			}

			if (matchingField != null) {
				// If we find one then update the value of this instance to that
				// of the old one
				try {
					newField.set(this, matchingField.get(oldInstance));
				}
				catch (IllegalArgumentException e) {
				}
				catch (IllegalAccessException e) {
				}
			}
		}
	}

	/**
	 * Uses reflection to determine a version number for this object
	 * 
	 * @return The version number set as a field with the appropriate name or -1
	 *         if this field is not found.
	 */
	public final int getVersionNumber()
	{
		try {
			for (Field f : getClass().getFields()) {
				if (f.getName() == ComponentManager.versionNumberFieldName) return f.getInt(this);
			}
		}
		catch (Exception e) {

		}

		return -1;
	}
}
