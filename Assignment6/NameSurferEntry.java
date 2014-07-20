/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		// You fill this in //
		int nameEnd = line.indexOf(" ");
		name = line.substring(0, nameEnd);
		nameRank = new int[NDECADES];
		int decadeStart = nameEnd + 1;
		int decadeEnd = line.indexOf(" ", decadeStart);
		for(int i = 0; i < NDECADES; i++){
			if(decadeEnd != -1){
				nameRank[i] = Integer.parseInt(line.substring(decadeStart, decadeEnd));
			} else{
				nameRank[i] = Integer.parseInt(line.substring(decadeStart));
			}
			decadeStart = decadeEnd + 1;
			decadeEnd = line.indexOf(" ", decadeStart);
		}
		
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		// You need to turn this stub into a real implementation //
		
		return name;
		//return null;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		// You need to turn this stub into a real implementation //
		if(decade < NDECADES){
			return nameRank[decade];
		} else return 0;

		//return 0;
		
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		// You need to turn this stub into a real implementation //
		String returnName = name + " [ "; 
		String temp = returnName;
		for(int i = 0; i < NDECADES; i++){
			temp = connectStrings(temp, nameRank[i] + " ");
		}
		temp = connectStrings(temp, "]");
		return temp;
	}
	
	private String connectStrings(String a, String b){
		String temp;
		temp = a + b;
		return temp;
	}
	
	private String name;
	private int[] nameRank;
}

