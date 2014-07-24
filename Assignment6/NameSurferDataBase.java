import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		nameInventory = new HashMap<String, NameSurferEntry>();
		String a = null;
		try {
			BufferedReader in  = new BufferedReader(new FileReader(filename));
			while((a = in.readLine()) != null){
				NameSurferEntry entry = new NameSurferEntry(a);
				nameInventory.put(entry.getName(), entry);
			}
	    } catch(Exception e) {
	        throw new ExceptionInInitializerError(e);
	    }
			
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		if(nameInventory.containsKey(name)){
			return nameInventory.get(name);
		} else return null;
	}
	HashMap<String, NameSurferEntry> nameInventory;
}

