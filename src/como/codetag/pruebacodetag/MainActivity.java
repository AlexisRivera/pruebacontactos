package como.codetag.pruebacodetag;

import com.codetag.pruebacodetag.basedatos.DatabaseHandler;
import com.codetag.pruebacodetag.modelos.Contact;
import com.codetag.pruebacodetag.utilis.ImagenAdapter;

import net.proyectosbeta.pruebabasedatos.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// Objetos.
	private DatabaseHandler baseDatos;
	private ImagenAdapter cursorAdapter;
	private ListView listViewPersonas;
	private Button botonAgregarPersona;
	
	// Constantes privadas.
	private int CODIGO_RESULT_EDITAR_PERSONA = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Hace referencia a la parte xml (activity_main.xml).
		listViewPersonas = (ListView) findViewById(R.id.listViewContacts);
		botonAgregarPersona = (Button)findViewById(R.id.btnAddContact);
		

		/**
		 * Al hacer click en el boton agregar Persona se abre una ventana para la edicion de una
		 * nueva persona..
		 */
		botonAgregarPersona.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Se crea una nueva persona.
				editarPersona(0);
			}
		});
		
		// Se recuperan todas las personas de la base de datos.
		recuperarTodasPersonas();
		
		// Asociamos los menús contextuales al listViewPersonas.
	    registerForContextMenu(listViewPersonas);
	}
	
	/**
	 * Metodo publico que se sobreescribe. En este metodo crearmos el menu contextual
	 * para el ListView de personas. 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
	    super.onCreateContextMenu(menu, v, menuInfo);
	    android.view.MenuInflater inflater = getMenuInflater();
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    inflater.inflate(R.menu.opciones_personas, menu);
	}
		
	/**
	 * Metodo publico que se sobreescribe. En este metodo colocamos las acciones de las opciones del menu contextual
	 * para el ListView de personas. 
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
	    switch (item.getItemId()) {
	        case R.id.menu_contextual_editar_persona:
	        	editarPersona((int)info.id);
	            return true;
	        case R.id.menu_contextual_eliminar_persona:
	        	eliminarPersona((int)info.id);
	        	recuperarTodasPersonas();
	            return true;
	        default:
	            return super.onContextItemSelected((android.view.MenuItem) item);
	    }
	}

	@Override
	protected void onStart(){
		super.onStart();
	}

	@Override
	protected void onResume(){
		super.onResume();;
	}
	
	/**
	 * Metodo privado que recupera todos las personas existentes de la base de datos.
	 */
	private void recuperarTodasPersonas() {
		try{
			baseDatos = new DatabaseHandler(this);
			
			// Devuelve todas las personas en el objeto Cursor.
			Cursor cursor = baseDatos.obtenerTodasPersonas();
		
		    String[] from = new String[]{
		    	"name", 
		    	"phone", 
		    	"uriImage"
		    };
			    
		    int[] to = new int[]{
		    	R.id.tvNameContact,
		    	R.id.tvPhone,
		    	R.id.imageViewContact,
		    };
	    	cursorAdapter = new ImagenAdapter(this, cursor, from, to);
	    	listViewPersonas.setAdapter(cursorAdapter);
	    }catch(Exception e){
	    	Log.d("Error", "El mensaje de error es: " + e.getMessage());
	    }finally{
	    	// Se cierra la base de datos.
	    	baseDatos.cerrar();
	    }
	}
	
	/**
	 * Metodo publico que edita una persona.
	 * @param p_id
	 */
	public void editarPersona(int p_id){
		// Si el p_id es 0, entonces se crea una nueva persona.
		if(p_id == 0){
			// Se dirige a la actividad EditarPersonaActivity.
	        Intent actividad_editarPersona = new Intent(MainActivity.this, EditarContactActivity.class);
	        startActivityForResult(actividad_editarPersona, CODIGO_RESULT_EDITAR_PERSONA); 
		}else{
			// Recupera una persona especifica.
			Contact persona;
			
			try{    		
				persona = baseDatos.getPersona(p_id);
		   	    
		   	    // Se dirige a la actividad EditarPersonaActivity.
		        Intent actividad_editarPersona = new Intent(this, EditarContactActivity.class);
		            
		        // Se le coloca parametros para enviar a la actividad EditarPersonaActivity.
		        actividad_editarPersona.putExtra("id", p_id);
		        actividad_editarPersona.putExtra("name", persona.getName());
		        actividad_editarPersona.putExtra("phone", persona.getPhone());
		        actividad_editarPersona.putExtra("addres", persona.getAddres());
		        actividad_editarPersona.putExtra("ruta_imagen", persona.getUriImage());
		            
		        startActivityForResult(actividad_editarPersona, CODIGO_RESULT_EDITAR_PERSONA); 
			}catch (Exception e){
			     Toast.makeText(getApplicationContext(), "Error al editar persona!!!", Toast.LENGTH_SHORT).show();
			     e.printStackTrace();
			}finally{
			     baseDatos.cerrar();
			}
		}
	}
	
	/**
	 * Metodo privado que elimina una persona.
	 * @param id_persona
	 */
	private void eliminarPersona(int id_persona){
		// Objetos. 
		AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);  	
		
		// Variables.
		final int v_id_persona = id_persona;
		    
		mensaje_dialogo.setTitle("Importante");  
		mensaje_dialogo.setMessage("¿Está seguro de eliminar esta persona?");            
		mensaje_dialogo.setCancelable(false);  
		mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialogo1, int id) {  
	            try{    		
	    	        baseDatos.eliminaPersona(v_id_persona);
	    		    
	    		    recuperarTodasPersonas();
	    		}catch(Exception e){
	    		     Toast.makeText(getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
	    			 e.printStackTrace();
	    		}finally{
	    		     baseDatos.cerrar();
	    	    }
	        }  
	    });  
		mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialogo1, int id) {  
	        	recuperarTodasPersonas();
	        }  
	    });            
		mensaje_dialogo.show();  
	}
	
	/**
	 * El metodo protegido se sobreescribe. Se llama con el resultado de otra actividad
	 * requestCode es el codigo original que se manda a la actividad
	 * resultCode es el codigo de retorno, 0 significa que todo salió bien
	 * intent es usado para obtener alguna información del caller.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    recuperarTodasPersonas();
	}
} // Fin de la actividad MainActivity.