package es.jjsr.saveforest.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.dto.Binnacle;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.contentProviderPackage.BinnacleProvider;
import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.volley.CicloVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by José Juan Sosa Rodríguez on 03/12/2017.
 */
public class Sincronizacion {
    private static final String LOGTAG = "JJSR - Sincronizacion";
    private static ContentResolver resolvedor;
    private static Context contexto;
    private static boolean esperandoRespuestaDeServidor = false;

    public Sincronizacion(Context contexto){
        this.resolvedor = contexto.getContentResolver();
        this.contexto = contexto;
        //recibirActualizacionesDelServidor(); //La primera vez se cargan los datos siempre
    }

    public synchronized static boolean isEsperandoRespuestaDeServidor() {
        return esperandoRespuestaDeServidor;
    }

    public synchronized static void setEsperandoRespuestaDeServidor(boolean esperandoRespuestaDeServidor) {
        Sincronizacion.esperandoRespuestaDeServidor = esperandoRespuestaDeServidor;
    }

    public synchronized boolean sincronizar(){
        Log.i("sincronizacion","SINCRONIZAR");

        if(isEsperandoRespuestaDeServidor()){
            return true;
        }

        if(GConstants.VERSION_ADMINISTRADOR){
            enviarActualizacionesAlServidor();
        } else {
            recibirActualizacionesDelServidor();
        }

        return true;
    }



    private static void enviarActualizacionesAlServidor(){
        ArrayList<Binnacle> registrosBitacora = BinnacleProvider.readAllRecord(resolvedor);
        for(Binnacle binnacle : registrosBitacora){

            switch(binnacle.getOperation()){
                case GConstants.OPERATION_INSERT:
                    Advice advice = null;
                    try {
                        advice = AdviceProvider.readRecord(resolvedor, binnacle.getId_advice());
                        CicloVolley.addCiclo(advice, true, binnacle.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case GConstants.OPERATION_UPDATE:
                    try {
                        advice = AdviceProvider.readRecord(resolvedor, binnacle.getId_advice());
                        CicloVolley.updateCiclo(advice, true, binnacle.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case GConstants.OPERATION_DELETE:
                    CicloVolley.delCiclo(binnacle.getId_advice(), true, binnacle.getId());
                    break;
            }
            Log.i("sincronizacion", "acabo de enviar");
        }
    }

    private static void recibirActualizacionesDelServidor(){
        CicloVolley.getAllCiclo();
    }

    public static void realizarActualizacionesDelServidorUnaVezRecibidas(JSONArray jsonArray){
        Log.i("sincronizacion", "recibirActualizacionesDelServidor");

        try {
            ArrayList<Integer> identificadoresDeRegistrosActualizados = new ArrayList<Integer>();
            ArrayList<Ciclo> registrosNuevos = new ArrayList<>();
            ArrayList<Ciclo> registrosViejos = CicloProveedor.readAll(resolvedor);
            ArrayList<Integer> identificadoresDeRegistrosViejos = new ArrayList<Integer>();
            for(Ciclo i : registrosViejos) identificadoresDeRegistrosViejos.add(i.getID());

            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++ ){
                obj = jsonArray.getJSONObject(i);
                registrosNuevos.add(new Ciclo(obj.getInt("PK_ID"), obj.getString("nombre"), obj.getString("abreviatura")));
            }

            for(Ciclo ciclo: registrosNuevos) {
                try {
                    if(identificadoresDeRegistrosViejos.contains(ciclo.getID())) {
                        CicloProveedor.update(resolvedor, ciclo);
                    } else {
                        CicloProveedor.insert(resolvedor, ciclo);
                    }
                    identificadoresDeRegistrosActualizados.add(ciclo.getID());
                } catch (Exception e){
                    Log.i("sincronizacion",
                            "Probablemente el registro ya existía en la BD."+"" +
                                    " Esto se podría controlar mejor con una Bitácora.");
                }
            }

            for(Ciclo ciclo: registrosViejos){
                if(!identificadoresDeRegistrosActualizados.contains(ciclo.getID())){
                    try {
                        CicloProveedor.delete(resolvedor, ciclo.getID());
                    }catch(Exception e){
                        Log.i("sincronizacion", "Error al borrar el registro con id:" + ciclo.getID());
                    }
                }
            }

            //CicloVolley.getAllCiclo(); //Los baja y los guarda en SQLite
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
