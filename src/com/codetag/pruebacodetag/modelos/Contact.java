package com.codetag.pruebacodetag.modelos;

import java.io.Serializable;

/**
 * Clase Persona.
 * @author proyectosbeta
 *
 */
public class Contact implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int Id;
    public String 	name,phone,addres,uriImage;
    
    public Contact(){
    	
    	
    	
    }
   
	public Contact(int iD,String name,String phone,String addres,String uriImage) {
this.Id=iD;
		this.name=name;
this.phone=phone;
this.addres=addres;
this.uriImage=uriImage;
		
		
	}
	
	public Contact(String name,String phone,String addres,String uriImage) {
		
				this.name=name;
		this.phone=phone;
		this.addres=addres;
		this.uriImage=uriImage;
				
				
			}
	

	public int getId() {
		return Id;
	}


	public void setId(int id) {
		Id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAddres() {
		return addres;
	}


	public void setAddres(String addres) {
		this.addres = addres;
	}


	public String getUriImage() {
		return uriImage;
	}


	public void setUriImage(String uriImage) {
		if(uriImage==null){
		this.uriImage = "no tiene imagen";
		}else{
			
			this.uriImage=uriImage;
		}
		}
	
}
