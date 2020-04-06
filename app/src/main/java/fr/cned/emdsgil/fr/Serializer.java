package fr.cned.emdsgil.fr;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe qui permet de sérialiser et désérialiser des objets
 * @author Emds
 *
 */
abstract class Serializer {

	/**
	 *
	 * @param object
	 * @param context
	 */
	static void serialize(Object object, Context context) {
		try {
			FileOutputStream file = context.openFileOutput(Global.filename, Context.MODE_PRIVATE) ;
			try {
				ObjectOutputStream oos = new ObjectOutputStream(file);
				oos.writeObject(object) ;
				oos.flush() ;
				oos.close() ;
			} catch (IOException e) {
				// erreur de sérialisation
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// fichier non trouvé
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param context
	 * @return object
	 */
	static Object deSerialize(Context context) {
		try {
			FileInputStream file = context.openFileInput(Global.filename) ;
			try {
				ObjectInputStream ois = new ObjectInputStream(file);
				try {
					Object object = ois.readObject() ;
					ois.close() ;
					return object ;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// fichier non trouvé
			e.printStackTrace();
		}
		return null ;		
	}
	
}
