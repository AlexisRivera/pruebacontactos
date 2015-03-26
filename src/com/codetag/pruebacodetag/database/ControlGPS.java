package com.codetag.pruebacodetag.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


/**
 * Created by asus on 09/03/2015.
 */
public class ControlGPS implements LocationListener {
    private boolean estadoGPS;
    private Context contexto;
     private String latitud, longitud;

    LocationManager handle; //Gestor del servicio de localización
    private boolean servicioActivo;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    private String provider;


    public ControlGPS(Context c) {
   c=contexto;
        iniciarServicio();

    }

    public void pararServicio(){
        //Se para el servicio de localización
        servicioActivo = false;
       // botonActivar.setText("activar");
        //Se desactivan las notificaciones
        handle.removeUpdates(this);
    }
    public void iniciarServicio(){
        //Se activa el servicio de localización
        servicioActivo = true;
       // botonActivar.setText("desactivar");

        //Crea el objeto que gestiona las localizaciones
        handle = (LocationManager)contexto.getSystemService(Context.LOCATION_SERVICE);

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        //obtiene el mejor proveedor en función del criterio asignado
        //(la mejor precisión posible)
        provider = handle.getBestProvider(c, true);
        setProvider(provider);
        //Se activan las notificaciones de localización con los parámetros: proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
        handle.requestLocationUpdates(provider, 30000, 0, this);
        //Obtenemos la última posición conocida dada por el proveedor
        Location loc = handle.getLastKnownLocation(provider);
        muestraPosicionActual(loc);
    }


    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void muestraPosicionActual(Location loc){
        if(loc == null){//Si no se encuentra localización, se mostrará "Desconocida"
            setLatitud("Desconocida");
            setLongitud("Desconocida");
        }else{//Si se encuentra, se mostrará la latitud y longitud
            setLatitud(String.valueOf(loc.getLatitude()));
            setLongitud(String.valueOf(loc.getLongitude()));
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        // Se ha encontrado una nueva localización
        muestraPosicionActual(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Proveedor deshabilitado
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Proveedor habilitado
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Ha cambiado el estado del proveedor
    }
}
