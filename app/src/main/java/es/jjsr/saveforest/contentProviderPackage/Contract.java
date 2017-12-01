package es.jjsr.saveforest.contentProviderPackage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contiene la información necesaria para conectarse a la base de datos.
 * Created by José Juan Sosa Rodríguez on 29/10/2017.
 */

public class Contract {

    public static final String AUTHORITY = "es.jjsr.saveforest.contentProviderPackage.MyContentProvider";

    public static final class Advice implements BaseColumns{

        public static final Uri CONTENT_URI_ADVICE = Uri.parse(
                "content://" + AUTHORITY + "/Advice"
        );

        public static final String ID_ADVICE = "idAdvice";
        public static final String NAME = "name";
        public static final String DATE = "date";
        public static final String DESCRIPTION = "description";
        public static final String ID_COUNTRY = "idCountry";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String NAME_IMAGE = "nameImage";
    }

    public static final class Country implements BaseColumns{

        public static final Uri CONTENT_URI_COUNTRY = Uri.parse(
                "content://" + AUTHORITY + "/Country"
        );

        public static final String ID_COUNTRY = "idCountry";
        public static final String NAME_COUNTRY = "nameCountry";
        public static final String CODE_COUNTRY = "codeCountry";
    }

    public static final class Binnacle implements BaseColumns{

        public static final Uri CONTENT_URI_BINNACLE = Uri.parse(
                "content://" + AUTHORITY + "/Binnacle"
        );

        public static final String ID = "id";
        public static final String ID_ADVICE = "idAdvice";
        public static final String OPERATION = "operation";
    }
}