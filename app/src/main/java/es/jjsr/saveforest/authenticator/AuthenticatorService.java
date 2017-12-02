package es.jjsr.saveforest.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Servicio que permite instanciar el autentificador cuando es iniciado.
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    @Override
    public void onCreate(){
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent){
        return mAuthenticator.getIBinder();
    }
}
