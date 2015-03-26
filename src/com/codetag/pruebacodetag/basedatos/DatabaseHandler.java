package com.codetag.pruebacodetag.basedatos;

import com.codetag.pruebacodetag.modelos.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	// Ruta por defecto de las bases de datos en el sistema Android.

	// Nombre de la Base de Datos.
	private static String NOMBRE_BASE_DATOS = "contactos";
		
	// Version de la Base de Datos.
	private static final int VERSION_BASE_DATOS = 1;
		 
	// Objeto Base de Datos.
	private SQLiteDatabase base_datos;
		 
	// Objeto Contexto.
	private Context contexto;
		
	// Constante privada
	private String SENTENCIA_SQL_CREAR_BASE_DATOS_PERSONAS = "CREATE TABLE if not exists contactos (_id INTEGER PRIMARY KEY autoincrement, " +
		"name TEXT, phone TEXT, addres TEXT, ruta_imagen TEXT)";
		
	/**
	 * Constructor
	 * Toma referencia hacia el contexto de la aplicación que lo invoca para poder acceder a los 'assets' y 
	 * 'resources' de la aplicación.
	 * Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de la base de datos.
	 * @param context
	*/
	public DatabaseHandler(Context context) {
		super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
		this.contexto = context;
	}
			 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla personas.
	    db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_PERSONAS);
	}
			 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    // Se elimina la versión anterior de la tabla Personas.
	    db.execSQL("DROP TABLE IF EXISTS contactos");
	 
	    // Se crea la nueva versión de la tabla personas.
	    db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_PERSONAS);
	}
	
	/**
	 * Metodo publico para insertar una nueva persona.
	 */
	public void insertarPersona(Contact persona){
		ContentValues valores = new ContentValues();
		valores.put("name", persona.getName());
		valores.put("phone", persona.getPhone());
		valores.put("addres", persona.getAddres());
		valores.put("ruta_imagen", persona.getUriImage());
		this.getWritableDatabase().insert("Personas", null, valores);
	}
	
	/**
	 * Metodo publico para actualizar una persona.
	 */
	public void actualizarRegistros(int id, String nombre, String apellido, String edad, String ruta_imagen){
		ContentValues actualizarDatos = new ContentValues();  
		actualizarDatos.put("name", nombre);
		actualizarDatos.put("phone", apellido);
		actualizarDatos.put("addres", edad);
		actualizarDatos.put("ruta_imagen", ruta_imagen);
		String where = "_id=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		
		try{    
		    this.getReadableDatabase().update("Personas", actualizarDatos, where, whereArgs);
		}
		catch (Exception e){
		    String error =  e.getMessage().toString();
		}
	}
	
	/**
	 * Metodo publico que retorna una persona especifica.
	 * @param id
	 * @return
	 */
	public Contact getPersona(int p_id) {
	    String[] columnas = new String[]{"_id", "name", "phone", "addres", "ruta_imagen"};
	    Cursor cursor = this.getReadableDatabase().query("contactos", columnas, "_id" + "= " + p_id, null, null, null, null);
	
	    if (cursor != null){
	    	cursor.moveToFirst();
	    }
	    	
		Contact persona = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
		    cursor.getString(4));
		    
		// Retorna la persona especifica.
		return persona;
	}
		
	/**
	 * Metodo publico que cierra la base de datos.
	 */
	public void cerrar(){
		this.close();
	}
		
	/**
	 * Metodo publico que devuelve todas las personas.
	 * @return
	 */
	public Cursor obtenerTodasPersonas(){
		String[] columnas = new String[]{"_id", "name", "phone", "addres", "ruta_imagen"};
		Cursor cursor = this.getReadableDatabase().query("contactos", columnas, null, null, null, null, null);
		
		if(cursor != null) {
		    cursor.moveToFirst();
		}
		return cursor;
	}
	
	
	/**
	 * Metodo publico que elimina una persona especifica.
	 * @param rowId
	 * @return
	 */
	public boolean eliminaPersona(long id){
		return this.getWritableDatabase().delete("contactos", "_id" + "=" + id, null) > 0;
	}	
}