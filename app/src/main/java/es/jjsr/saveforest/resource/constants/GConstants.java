package es.jjsr.saveforest.resource.constants;

/**
 * Clase de recursos.
 * Contiene constantes con valores para poder ser identificadas mejor en el código.
 * Created by José Juan Sosa Rodríguez on 01/12/2017.
 */

public class GConstants {
    public static final int OPERATION_INSERT = 1;
    public static final int OPERATION_UPDATE = 2;
    public static final int OPERATION_DELETE = 3;
    public static final int OPERATION_UPLOAD = 4;

    public static final int SIN_VALOR_INT = -1;
    public static final long SYNC_INTERVAL = 60; //Segundos para sincronización
    public static final boolean VERSION_ADMINISTRATOR = true;
    public static final String SERVER_ROUTE = "http://jjsr.es:8080/WebASaveForest/webresources";
    public static final String ADVICES_SERVER_ROUTE = "http://jjsr.es:8080/WebASaveForest/webresources/advices";
    public static final String IMAGES_SERVER_ROUTE = "http://jjsr.es:8080/WebASaveForest/webresources/image";
}
