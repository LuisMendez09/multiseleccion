package com.luis.corte.Controlers.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.luis.corte.complementos.Complementos;
import com.luis.corte.complementos.FileLog;
import com.luis.corte.complementos.KeyValues;
import com.luis.corte.models.CatalogoActividades;
import com.luis.corte.models.CatalogoCajas;
import com.luis.corte.models.CatalogoPuestos;
import com.luis.corte.models.Configuracion;
import com.luis.corte.models.Producto;
import com.luis.corte.models.Puestos;
import com.luis.corte.models.ReporteProduccion;
import com.luis.corte.models.Settings;
import com.luis.corte.models.Trabajadores;

import java.util.ArrayList;


/**
 * Created by josu on 24/9/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Contacts table name
    private static final String TABLE_PRODUCCION = "Produccion";
    private static final String TABLE_PUESTOS = "Puestos";
    private static final String TABLE_TRABAJADORES = "Trabajadores";
    private static final String TABLE_CATOLOGO_ACTIVIDADES = "CatalogoActividades";
    private static final String TABLE_CATOLOGO_TAMANIOCAJAS = "CatalogoTamaniosCajas";
    private static final String TABLE_CATOLOGO_PUESTOS = "CatalogoPuestos";
    private static final String TABLE_SETTINGS = "Settings";
    private static final String TABLE_CONFIGURACION = "Configuracion";

    //settings table columnas name
    private static final String KEY_ID_SETTINGS = "Id";
    private static final String KEY_DATEINICIO_SETTINGS = "DateInicio";
    private static final String KEY_DATEFIN_SETTINGS = "DateFin";
    private static final String KEY_FECHASTRING_SETTINGS = "FechaString";
    private static final String KEY_HORAINICIOSTRING_SETTINGS = "HoraIncioString";
    private static final String KEY_HORAFINSTRING_SETTINGS = "HoraFinString";
    private static final String KEY_JORNADAFINALIZADA_SETTINGS = "FinJornada";//INDICADOR DE FIN DE JORNADA
    private static final String KEY_JORNADAINICIADA_SETTINGS = "InicioJornada";//INDICADOR DE INICIO DE JORNADA
    private static final String KEY_ENVIODATOS_SETTINGS = "EnviarInformacion";//INDICADOR DE INFORMACION PENDIENTE
    private static final String KEY_TOTALEMPLEADOS_SETTINGS = "Empleados";//TOTAL EMPLEADOS
    private static final String KEY_IDACTIVIDAD_SETTINGS = "IdActividad";
    private static final String KEY_ACTIVIDAD_SETTINGS = "Actividad";

    // Configuracion table columnas name
    private static final String KEY_ID_CONFIGURACION = "id";
    private static final String KEY_URL_CONFIGURACION = "URL";
    private static final String KEY_PARA_CONFIGURACION = "Para";
    private static final String KEY_CC_CONFIGURACION = "Cc";
    private static final String KEY_ASUNTO_CONFIGURACION = "Asunto";
    private static final String KEY_MENSAJE_CONFIGURACION = "Mensaje";
    private static final String KEY_TIEMPOMAS_CONFIGURACION = "TiempoMas";
    private static final String KEY_TIEMPOMENOS_CONFIGURACION = "TiempoMenos";

    // catalogo actividades Table Columns names
    private static final String KEY_ID_ACTIVIDADES = "id";
    private static final String KEY_DESCRIPCION_ACTIVIDADES = "descripcion";
    private static final String KEY_IDTAMANIOCAJAS_ACTIVIDADES = "idTamanioCaja";

    // catalogo Tamaños de cajas Table Columns names
    private static final String KEY_ID_TAMANIOCAJAS = "id";
    private static final String KEY_DESCRIPCION_TAMANIOCAJAS = "descripcion";
    private static final String KEY_CANTIDAD_TAMANIOCAJAS = "Cantidad";

    // catalogo actividades Table Columns names
    private static final String KEY_ID_CATALOGOPUESTOS = "id";
    private static final String KEY_DESCRIPCION_CATALOGOPUESTOS= "descripcion";

    // trabajadores Table Columns names
    private static final String KEY_ID_TRABAJADORES = "Id";
    private static final String KEY_CONSECUTIVO_TRABAJADORES = "Consecutivo";
    private static final String KEY_NOMBRE_TRABAJADORES = "Nombre";
    private static final String KEY_APELLIDOPATERNO_TRABAJADORES = "ApellidoPaterno";
    private static final String KEY_APELLIDOMATERNO_TRABAJADORES = "ApellidoMaterno";
    private static final String KEY_IDPUESTO_TRABAJADORES = "IdPuesto";

    // CatalogoPuestos Table Columns names
    private static final String KEY_ID_PUESTOS = "Id";
    private static final String KEY_DATE_PUESTOS = "DateInicio";
    private static final String KEY_FECHASTRING_PUESTOS = "FechaString";
    private static final String KEY_HORAINICIOSTRING_PUESTOS = "HoraInicioString";
    private static final String KEY_DATEFIN_PUESTOS = "DateFin";
    private static final String KEY_HORAFINSTRING_PUESTOS = "HoraFinString";
    private static final String KEY_IDTRABAJADOR_PUESTOS = "IdTrabajador";
    private static final String KEY_CONSECUTIVO_PUESTOS = "Consecutivo";
    private static final String KEY_TRABAJADOR_PUESTOS = "Trabajador";
    private static final String KEY_IDPUESTO_PUESTOS = "IdPuesto";
    private static final String KEY_ENVIADO_PUESTOS = "Enviado";

    // Producto Table Columns names
    private static final String KEY_ID_PRODUCCION = "Id";
    private static final String KEY_DATE_PRODUCCION = "Date";
    private static final String KEY_HORASTRING_PRODUCCION = "HoraString";
    private static final String KEY_FECHASTRING_PRODUCCION = "FechaString";
    private static final String KEY_IDTRABAJADOR_PRODUCCION = "IdTrabajador";
    private static final String KEY_CONSECUTIVO_PRODUCCION = "Consecutivo";
    private static final String KEY_TRABAJADOR_PRODUCCION = "Trabajador";
    private static final String KEY_IDACTIVIDAD_PRODUCCION = "IdActividad";
    private static final String KEY_PRIMERA_PRODUCCION = "Primera";
    private static final String KEY_SEGUNDA_PRODUCCION = "Segunda";
    private static final String KEY_AGRANEL_PRODUCCION = "Agranel";
    private static final String KEY_ENVIADO_PRODUCCION = "Enviado";

    public DBHandler(final Context context) {

        super(new DatabaseContext(context), KeyValues.MY_DATABASE_NAME, null, KeyValues.DATABASE_VERSION);
        //super(context, KeyValues.MY_DATABASE_NAME, null, KeyValues.DATABASE_VERSION);
        FileLog.v(Complementos.TAG_BDHANDLER,"llamada al metodo DBHandler()");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FileLog.v(Complementos.TAG_BDHANDLER,"INICIA CREACION DE LAS TABLAS EN LA DB");

        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
        +KEY_ID_SETTINGS + " INTEGER PRIMARY KEY,"
        +KEY_DATEINICIO_SETTINGS+ " INTEGER,"
        +KEY_DATEFIN_SETTINGS+ " INTEGER,"
        +KEY_FECHASTRING_SETTINGS+ " TEXT,"
        +KEY_HORAINICIOSTRING_SETTINGS+ " TEXT,"
        +KEY_HORAFINSTRING_SETTINGS+ " TEXT,"
        +KEY_JORNADAFINALIZADA_SETTINGS+ " INTEGER,"
        +KEY_JORNADAINICIADA_SETTINGS+ " INTEGER,"
        +KEY_ENVIODATOS_SETTINGS+ " INTEGER,"
        +KEY_TOTALEMPLEADOS_SETTINGS+ " INTEGER,"
        +KEY_IDACTIVIDAD_SETTINGS+ " INTEGER,"
        +KEY_ACTIVIDAD_SETTINGS+ " TEXT"
        + ")";
        db.execSQL(CREATE_SETTINGS_TABLE);

        String CREATE_CONFIGURACION_TABLE = "CREATE TABLE " + TABLE_CONFIGURACION + "("
                + KEY_ID_CONFIGURACION + " INTEGER PRIMARY KEY,"
                + KEY_URL_CONFIGURACION + " TEXT,"
                + KEY_PARA_CONFIGURACION + " TEXT,"
                + KEY_CC_CONFIGURACION + " TEXT,"
                + KEY_ASUNTO_CONFIGURACION + " TEXT,"
                + KEY_MENSAJE_CONFIGURACION + " TEXT,"
                + KEY_TIEMPOMAS_CONFIGURACION + " INTEGER,"
                + KEY_TIEMPOMENOS_CONFIGURACION + " INTEGER"
                + ")";
        db.execSQL(CREATE_CONFIGURACION_TABLE);

        String CREATE_ACTIVIDADES_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_ACTIVIDADES + "("
        + KEY_ID_ACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_ACTIVIDADES + " TEXT,"
                + KEY_IDTAMANIOCAJAS_ACTIVIDADES + " INTEGER"
                + ")";
        db.execSQL(CREATE_ACTIVIDADES_TABLE);

        String CREATE_TAMANOCAJA_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_TAMANIOCAJAS + "("
                + KEY_ID_TAMANIOCAJAS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TAMANIOCAJAS + " TEXT,"
                + KEY_CANTIDAD_TAMANIOCAJAS + " INTEGER"
                + ")";
        db.execSQL(CREATE_TAMANOCAJA_TABLE);

        String CREATE_CATALOGOPUESTOS_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_PUESTOS + "("
                + KEY_ID_CATALOGOPUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_CATALOGOPUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_CATALOGOPUESTOS_TABLE);

        String CREATE_TRABAJADORES_TABLE = "CREATE TABLE " + TABLE_TRABAJADORES + "("
                + KEY_ID_TRABAJADORES + " INTEGER PRIMARY KEY,"
                + KEY_CONSECUTIVO_TRABAJADORES + " INTEGER,"
                + KEY_NOMBRE_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOPATERNO_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOMATERNO_TRABAJADORES + " TEXT,"
                + KEY_IDPUESTO_TRABAJADORES + " INTEGER"
                + ")";
        db.execSQL(CREATE_TRABAJADORES_TABLE);

        String CREATE_PUESTOS_TABLE = "CREATE TABLE " + TABLE_PUESTOS + "("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                +KEY_DATE_PUESTOS+" INTEGER,"
                +KEY_FECHASTRING_PUESTOS+" TEXT,"
                +KEY_HORAINICIOSTRING_PUESTOS+" TEXT,"
                +KEY_DATEFIN_PUESTOS+" INTEGER,"
                +KEY_HORAFINSTRING_PUESTOS+" TEXT,"
                +KEY_IDTRABAJADOR_PUESTOS+" INTEGER,"
                +KEY_CONSECUTIVO_PUESTOS+" INTEGER,"
                +KEY_TRABAJADOR_PUESTOS+" TEXT,"
                +KEY_IDPUESTO_PUESTOS+" INTEGER,"
                +KEY_ENVIADO_PUESTOS+" INTEGER"
                + ")";
        db.execSQL(CREATE_PUESTOS_TABLE);

        String CREATE_PRODUCCION_TABLE = "CREATE TABLE " + TABLE_PRODUCCION + "("
        +KEY_ID_PRODUCCION + " INTEGER PRIMARY KEY,"
        +KEY_DATE_PRODUCCION +" INTEGER,"
        +KEY_HORASTRING_PRODUCCION +" TEXT,"
        +KEY_FECHASTRING_PRODUCCION+" TEXT,"
        +KEY_IDTRABAJADOR_PRODUCCION +" INTEGER,"
        +KEY_CONSECUTIVO_PRODUCCION +" INTEGER,"
        +KEY_TRABAJADOR_PRODUCCION +" TEXT,"
        +KEY_IDACTIVIDAD_PRODUCCION +" INTEGER,"
        +KEY_PRIMERA_PRODUCCION +" INTEGER,"
        +KEY_SEGUNDA_PRODUCCION +" INTEGER,"
        +KEY_AGRANEL_PRODUCCION +" INTEGER,"
        +KEY_ENVIADO_PRODUCCION +" INTEGER"
        + ")";
        db.execSQL(CREATE_PRODUCCION_TABLE);

        FileLog.v(Complementos.TAG_BDHANDLER,"TERMINA CREACION DE LAS TABLAS EN LA DB");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        FileLog.v(Complementos.TAG_BDHANDLER,"AINICIA ACTUALIZACION DE LAS TABLAS");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURACION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATOLOGO_ACTIVIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATOLOGO_TAMANIOCAJAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUESTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCCION);

        // Creating tables again

        onCreate(db);
        Log.e("Log android","actualiuzacion tablas en metodo onUpgrade()");

    }

    public void recrearTablaActividades(){
        FileLog.v(Complementos.TAG_BDHANDLER,"REINICIAR LA TABLA ACTIVIDADES");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATOLOGO_ACTIVIDADES);

        String CREATE_ACTIVIDADES_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_ACTIVIDADES + "("
                + KEY_ID_ACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_ACTIVIDADES + " TEXT,"
                + KEY_IDTAMANIOCAJAS_ACTIVIDADES + " INTEGER"
                + ")";
        db.execSQL(CREATE_ACTIVIDADES_TABLE);

        String CREATE_TAMANOCAJA_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_TAMANIOCAJAS + "("
                + KEY_ID_TAMANIOCAJAS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TAMANIOCAJAS + " TEXT,"
                + KEY_CANTIDAD_TAMANIOCAJAS + " INTEGER"
                + ")";
        db.execSQL(CREATE_TAMANOCAJA_TABLE);
    }


    public void recrearTablaCatalogoPuestos(){
        FileLog.v(Complementos.TAG_BDHANDLER,"REINICIAR LA TABLA CATALOGO PUESTOS");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATOLOGO_ACTIVIDADES);

        String CREATE_CATALOGOPUESTOS_TABLE = "CREATE TABLE " + TABLE_CATOLOGO_PUESTOS + "("
                + KEY_ID_CATALOGOPUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_CATALOGOPUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_CATALOGOPUESTOS_TABLE);
    }
    public void recrearTablaTrabajadores(){
        FileLog.v(Complementos.TAG_BDHANDLER,"REINICIAR LA TABLA TRABAJADORES");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJADORES);

        String CREATE_TRABAJADORES_TABLE = "CREATE TABLE " + TABLE_TRABAJADORES + "("
                + KEY_ID_TRABAJADORES + " INTEGER PRIMARY KEY,"
                + KEY_CONSECUTIVO_TRABAJADORES + " INTEGER,"
                + KEY_NOMBRE_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOPATERNO_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOMATERNO_TRABAJADORES + " TEXT,"
                + KEY_IDPUESTO_TRABAJADORES + " INTEGER"
                + ")";
        db.execSQL(CREATE_TRABAJADORES_TABLE);
    }

    public void reMakeTables(int limpiarTodo){

        FileLog.v(Complementos.TAG_BDHANDLER,"INICIA EL REINICIO DE LAS TABLAS METODO reMakeTables() PARAMETRO limpiarTodo="+limpiarTodo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUESTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCCION);

        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                +KEY_ID_SETTINGS + " INTEGER PRIMARY KEY,"
                +KEY_DATEINICIO_SETTINGS+ " INTEGER,"
                +KEY_DATEFIN_SETTINGS+ " INTEGER,"
                +KEY_FECHASTRING_SETTINGS+ " TEXT,"
                +KEY_HORAINICIOSTRING_SETTINGS+ " TEXT,"
                +KEY_HORAFINSTRING_SETTINGS+ " TEXT,"
                +KEY_JORNADAFINALIZADA_SETTINGS+ " INTEGER,"
                +KEY_JORNADAINICIADA_SETTINGS+ " INTEGER,"
                +KEY_ENVIODATOS_SETTINGS+ " INTEGER,"
                +KEY_TOTALEMPLEADOS_SETTINGS+ " INTEGER,"
                +KEY_IDACTIVIDAD_SETTINGS+ " INTEGER,"
                +KEY_ACTIVIDAD_SETTINGS+ " TEXT"
                + ")";
        db.execSQL(CREATE_SETTINGS_TABLE);

        String CREATE_TRABAJADORES_TABLE = "CREATE TABLE " + TABLE_TRABAJADORES + "("
                + KEY_ID_TRABAJADORES + " INTEGER PRIMARY KEY,"
                + KEY_CONSECUTIVO_TRABAJADORES + " INTEGER,"
                + KEY_NOMBRE_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOPATERNO_TRABAJADORES + " TEXT,"
                + KEY_APELLIDOMATERNO_TRABAJADORES + " TEXT,"
                + KEY_IDPUESTO_TRABAJADORES + " INTEGER"
                + ")";
        db.execSQL(CREATE_TRABAJADORES_TABLE);

        String CREATE_PUESTOS_TABLE = "CREATE TABLE " + TABLE_PUESTOS + "("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                +KEY_DATE_PUESTOS+" INTEGER,"
                +KEY_FECHASTRING_PUESTOS+" TEXT,"
                +KEY_HORAINICIOSTRING_PUESTOS+" TEXT,"
                +KEY_DATEFIN_PUESTOS+" INTEGER,"
                +KEY_HORAFINSTRING_PUESTOS+" TEXT,"
                +KEY_IDTRABAJADOR_PUESTOS+" INTEGER,"
                +KEY_CONSECUTIVO_PUESTOS+" INTEGER,"
                +KEY_TRABAJADOR_PUESTOS+" TEXT,"
                +KEY_IDPUESTO_PUESTOS+" INTEGER,"
                +KEY_ENVIADO_PUESTOS+" INTEGER"
                + ")";
        db.execSQL(CREATE_PUESTOS_TABLE);

        String CREATE_PRODUCCION_TABLE = "CREATE TABLE " + TABLE_PRODUCCION + "("
                +KEY_ID_PRODUCCION + " INTEGER PRIMARY KEY,"
                +KEY_DATE_PRODUCCION +" INTEGER,"
                +KEY_HORASTRING_PRODUCCION +" TEXT,"
                +KEY_FECHASTRING_PRODUCCION+" TEXT,"
                +KEY_IDTRABAJADOR_PRODUCCION +" INTEGER,"
                +KEY_CONSECUTIVO_PRODUCCION +" INTEGER,"
                +KEY_TRABAJADOR_PRODUCCION +" TEXT,"
                +KEY_IDACTIVIDAD_PRODUCCION +" INTEGER,"
                +KEY_PRIMERA_PRODUCCION +" INTEGER,"
                +KEY_SEGUNDA_PRODUCCION +" INTEGER,"
                +KEY_AGRANEL_PRODUCCION +" INTEGER,"
                +KEY_ENVIADO_PRODUCCION +" INTEGER"
                + ")";
        db.execSQL(CREATE_PRODUCCION_TABLE);
    }

    /////////////// ADD  /////////////////////
    //añadir Settings
    public Long addSettings(Settings settings) {
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR SETTING "+settings.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DATEINICIO_SETTINGS, settings.getFechaInicio());
        values.put(KEY_DATEFIN_SETTINGS, settings.getFechaFinal());
        values.put(KEY_FECHASTRING_SETTINGS,settings.getFechaString());
        values.put(KEY_HORAINICIOSTRING_SETTINGS, settings.getHoraInicioString());
        values.put(KEY_HORAFINSTRING_SETTINGS, settings.getHoraFinalString());
        values.put(KEY_JORNADAFINALIZADA_SETTINGS, settings.getFinJornada());
        values.put(KEY_JORNADAINICIADA_SETTINGS, settings.getInicioJornada());
        values.put(KEY_ENVIODATOS_SETTINGS, settings.getEnviarInformacion());
        values.put(KEY_TOTALEMPLEADOS_SETTINGS, settings.getCapturaTrabaajdor());
        values.put(KEY_IDACTIVIDAD_SETTINGS, (settings.getActividades()==null?0:settings.getActividades().getId()));
        values.put(KEY_ACTIVIDAD_SETTINGS, (settings.getActividades()==null?"":settings.getActividades().getDescripcion()));

        // Inserting Row
        Long insert = db.insert(TABLE_SETTINGS, null, values);
        if(insert !=-1)
            settings.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return insert;
    }

    //añadir configuracion
    public Long addConfiguracion(Configuracion configuracion) {
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR CONFIGURACION "+configuracion.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_URL_CONFIGURACION,configuracion.getUrlApi());
        values.put(KEY_PARA_CONFIGURACION ,configuracion.getPara());
        values.put(KEY_CC_CONFIGURACION,configuracion.getCc());
        values.put(KEY_ASUNTO_CONFIGURACION ,configuracion.getAsunto());
        values.put(KEY_MENSAJE_CONFIGURACION ,configuracion.getMensaje());
        values.put(KEY_TIEMPOMAS_CONFIGURACION,configuracion.getTiempoMas());
        values.put(KEY_TIEMPOMENOS_CONFIGURACION,configuracion.getTiempoMenos());

        // Inserting Row
        Long insert = db.insert(TABLE_CONFIGURACION, null, values);
        if(insert !=-1)
            configuracion.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return insert;
    }

    //añadir actividad
    public Long addCatalogoActividades(CatalogoActividades catalogoActividades){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR CATALOGO ACTIVIDADES "+catalogoActividades.toString1());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DESCRIPCION_ACTIVIDADES, catalogoActividades.getDescripcion());
        values.put(KEY_IDTAMANIOCAJAS_ACTIVIDADES, catalogoActividades.getTamanioCaja().getId());

        Long insert = db.insert(TABLE_CATOLOGO_ACTIVIDADES, null, values);
        if(insert !=-1)
            catalogoActividades.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return  insert;
    }

    //añadir tamaños de cajas
    public Long addCatalogoCajas(CatalogoCajas catalogoCajas){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR CATALOGO TAMAÑOS CAJAS "+catalogoCajas.toString1());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DESCRIPCION_TAMANIOCAJAS, catalogoCajas.getDescripcion());
        values.put(KEY_CANTIDAD_TAMANIOCAJAS, catalogoCajas.getTotalCajas());

        Long insert = db.insert(TABLE_CATOLOGO_TAMANIOCAJAS, null, values);
        if(insert !=-1)
            catalogoCajas.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return  insert;
    }

    //añadir Puestos
    public Long addCatalogoPuestos(CatalogoPuestos catalogoPuestos){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR CATALOGO ACTIVIDADES "+catalogoPuestos.toString1());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DESCRIPCION_CATALOGOPUESTOS, catalogoPuestos.getDescripcion());

        Long insert = db.insert(TABLE_CATOLOGO_PUESTOS, null, values);
        if(insert !=-1)
            catalogoPuestos.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return insert;
    }

    //añadir  trabajador
    public Long addTrabajadores(Trabajadores trabajadores){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR TRABAJADOR "+trabajadores.toString());
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CONSECUTIVO_TRABAJADORES, trabajadores.getConsecutivo());
            values.put(KEY_NOMBRE_TRABAJADORES , trabajadores.getNombre());
            values.put(KEY_APELLIDOPATERNO_TRABAJADORES, trabajadores.getApellidoPaterno());
            values.put(KEY_APELLIDOMATERNO_TRABAJADORES, trabajadores.getApellidoMaterno());
            values.put(KEY_IDPUESTO_TRABAJADORES, trabajadores.getPuestosActual().getId());

            Long insert = db.insert(TABLE_TRABAJADORES, null, values);
            if(insert !=-1)
                trabajadores.setIdTrabajdor(insert);
            else
                FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
            db.close(); // Closing database connection
            return insert;
        }catch (NullPointerException e){
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION VALORES NULOS");
            return Long.valueOf(-1);
        }

    }
    //añadir puestos
    public Long addPuesto(Puestos puestos){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR PUESTO "+puestos.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DATE_PUESTOS, puestos.getDateInicio());
        values.put(KEY_FECHASTRING_PUESTOS, puestos.getFechaString());
        values.put(KEY_HORAINICIOSTRING_PUESTOS, puestos.getHoraInicioString());
        values.put(KEY_DATEFIN_PUESTOS, puestos.getDateFin());
        values.put(KEY_HORAFINSTRING_PUESTOS, puestos.getHoraFinalString());
        values.put(KEY_IDTRABAJADOR_PUESTOS, puestos.getIdTrabajdor());
        values.put(KEY_CONSECUTIVO_PUESTOS, puestos.getConsecutivo());
        values.put(KEY_TRABAJADOR_PUESTOS, puestos.getTrabajador());
        values.put(KEY_IDPUESTO_PUESTOS, puestos.getPuestos().getId());
        values.put(KEY_ENVIADO_PUESTOS, puestos.getEnviado());

        Long insert = db.insert(TABLE_PUESTOS, null, values);
        if(insert !=-1)
            puestos.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");
        db.close(); // Closing database connection

        return insert;
    }

    //añadir produccion
    public Long addProduccion(Producto produccion){
        FileLog.v(Complementos.TAG_BDHANDLER,"AÑADIR TRABAJADOR "+produccion.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DATE_PRODUCCION ,produccion.getDate());
        values.put(KEY_HORASTRING_PRODUCCION ,produccion.getHoraString());
        values.put(KEY_FECHASTRING_PRODUCCION ,produccion.getFechaString());
        values.put(KEY_IDTRABAJADOR_PRODUCCION,produccion.getIdTrabajdor());
        values.put(KEY_CONSECUTIVO_PRODUCCION ,produccion.getConsecutivo());
        values.put(KEY_TRABAJADOR_PRODUCCION ,produccion.getTrabajador());
        values.put(KEY_IDACTIVIDAD_PRODUCCION,produccion.getActividades().getId());
        values.put(KEY_PRIMERA_PRODUCCION ,produccion.getPrimera());
        values.put(KEY_SEGUNDA_PRODUCCION,produccion.getSegunda());
        values.put(KEY_AGRANEL_PRODUCCION ,produccion.getAgranel());
        values.put(KEY_ENVIADO_PRODUCCION,produccion.getEnviado());

        Long insert = db.insert(TABLE_PRODUCCION, null, values);

        if(insert !=-1)
            produccion.setId(insert);
        else
            FileLog.v(Complementos.TAG_BDHANDLER,"ERROR DE INSERSION ");

        db.close(); // Closing database connection

        return insert;
    }

    //////////////////// GET //////////////////////////
    // Getting setting
    public Settings getSetting(Long id) {

        Settings settings = null;

        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS +" WHERE "+KEY_ID_SETTINGS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                settings = new Settings(cursor,getcatalogoActividades(Long.parseLong(cursor.getString(10))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(settings != null)
            FileLog.v(Complementos.TAG_BDHANDLER,"Setting "+settings.toString());
        return settings;
    }

    // Getting configuracion
    public Configuracion getConfiguracion(Long id) {

        Configuracion configuracion = null;

        String selectQuery = "SELECT * FROM " + TABLE_CONFIGURACION +" WHERE "+KEY_ID_CONFIGURACION+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                configuracion = new Configuracion(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(configuracion != null)
            FileLog.v(Complementos.TAG_BDHANDLER,"Configuracion "+configuracion.toString());
        return configuracion;
    }

    // Getting categorias tamaños de cajas
    public ArrayList<CatalogoCajas> getCatalogoTamaniosCajas() {

        ArrayList<CatalogoCajas>  catalogoCajas = new ArrayList<CatalogoCajas>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATOLOGO_TAMANIOCAJAS ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catalogoCajas.add(new CatalogoCajas(cursor));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"Total Tamaño cajas "+catalogoCajas.size());
        return catalogoCajas;
    }

    //obtener un tamaño de caja
    public CatalogoCajas getCatalogoTamaniosCajas(Long id) {

        CatalogoCajas catalogoCajas = null;

        String selectQuery = "SELECT * FROM " + TABLE_CATOLOGO_TAMANIOCAJAS +" WHERE "+KEY_ID_TAMANIOCAJAS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catalogoCajas = new CatalogoCajas(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"Tamanio Caja "+catalogoCajas.toString1());
        return catalogoCajas;
    }

    // Getting categorias actividades
    public ArrayList<CatalogoActividades> getCatalogoActividades() {

        ArrayList<CatalogoActividades>  catalogoActividades = new ArrayList<CatalogoActividades>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATOLOGO_ACTIVIDADES ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catalogoActividades.add(new CatalogoActividades(cursor,getCatalogoTamaniosCajas(cursor.getLong(2))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"Total Actividades "+catalogoActividades.size());
        return catalogoActividades;
    }

    //obtener una actividad
    public CatalogoActividades getcatalogoActividades(Long id) {

        CatalogoActividades actividades = null;

        String selectQuery = "SELECT * FROM " + TABLE_CATOLOGO_ACTIVIDADES +" WHERE "+KEY_ID_ACTIVIDADES+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actividades = new CatalogoActividades(cursor,getCatalogoTamaniosCajas(cursor.getLong(2)));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"actividades "+actividades.toString1());
        return actividades;
    }
    /***
     * obter catalogo puestos
     * @return Lista de catalogo de puestos
     */
    public ArrayList<CatalogoPuestos> getCatalogoPuestos(){
        ArrayList<CatalogoPuestos> catalogo = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE_CATOLOGO_PUESTOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catalogo.add(new CatalogoPuestos(cursor));

            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"lista puestos "+catalogo.size());
        return catalogo;
    }

    /***
     * obtener un catalogo de puestos
     * @param id puestos a obtener
     * @return null si el puestos no se encontro de otra forma retorna CatalogoPuestos
     */
    public CatalogoPuestos getCatalogoPuestos(Long id) {

        CatalogoPuestos puesto = null;

        String selectQuery = "SELECT * FROM " + TABLE_CATOLOGO_PUESTOS +" WHERE "+KEY_ID_CATALOGOPUESTOS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puesto = new CatalogoPuestos(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"puesto "+puesto.toString1());
        return puesto;
    }

    /***
     * obtener lista de trabajadores
     * @return arrayList de Trabajadores
     */
    public ArrayList<Trabajadores> getTrabajdores() {

        ArrayList<Trabajadores> listaTrabajadores = new ArrayList<Trabajadores>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" ORDER BY "+KEY_CONSECUTIVO_TRABAJADORES ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                listaTrabajadores.add(new Trabajadores(cursor,getCatalogoPuestos(Long.parseLong(cursor.getString(5)))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"total trabajadores "+listaTrabajadores.size());
        return listaTrabajadores;
    }

    /***
     * obtener un trabajador
     * @param id id del trabajador
     * @return null si no se encontro de otra forma retorna un Trabajdor
     */
    public Trabajadores getTrabajdores(long id) {

        Trabajadores trabajador = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_ID_TRABAJADORES+" = '"+id+"' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajador = new Trabajadores(cursor,getCatalogoPuestos(Long.parseLong(cursor.getString(5))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"total Vehiculos "+trabajador.toString());
        return trabajador;
    }

    /***
     * obtener un trabajador
     * @param consecutivo consecutivo del trabajador
     * @return null si no se encontro de otra forma retorna un Trabajdor
     */
    public Trabajadores getTrabajdores(Integer consecutivo) {

        Trabajadores trabajador = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_CONSECUTIVO_TRABAJADORES+" = '"+consecutivo+"' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajador = new Trabajadores(cursor,getCatalogoPuestos(Long.parseLong(cursor.getString(5))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(trabajador!= null)
            FileLog.v(Complementos.TAG_BDHANDLER,"total Vehiculos "+trabajador.toString());
        return trabajador;
    }
    //get puestos sin enviar


    public ArrayList<Puestos> getPuestos(Trabajadores trabajadores,Long fechaInicial,Long fechaFinal) {

        ArrayList<Puestos> puestos = new ArrayList<Puestos>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS +" WHERE "+KEY_IDTRABAJADOR_PUESTOS +" = "+trabajadores.getIdTrabajdor();

        if(fechaFinal!=null && fechaInicial!=null){
            selectQuery = selectQuery+" AND "+KEY_DATE_PUESTOS+" between "+fechaInicial+" AND "+fechaFinal;
        }

        Log.i("EMAIL",selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puestos.add(new Puestos(cursor,trabajadores,getCatalogoPuestos(Long.parseLong(cursor.getString(9))))) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"total Producto "+puestos.size());
        return puestos;
    }

    public ArrayList<Puestos> getPuestosTrabajador(String fecha,Trabajadores trabajadores) {

        ArrayList<Puestos> puestos = new ArrayList<Puestos>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS +" WHERE "+KEY_ENVIADO_PUESTOS +" = 0 AND "+KEY_FECHASTRING_PUESTOS+"='"+fecha
                +"' AND "+KEY_CONSECUTIVO_PUESTOS+" = "+trabajadores.getConsecutivo()+" ORDER BY "+KEY_DATE_PUESTOS +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Puestos p = new Puestos(cursor,trabajadores,getCatalogoPuestos(Long.parseLong(cursor.getString(9))));
                puestos.add(p);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"total puestos "+puestos.size());

        return puestos;
    }


    /***
     * obtiene el ultimo puestos del trabajdor
     * @param fecha dia donde se quiere obtener el ultimo puestos del trabajdor
     * @param trabajadores trabajador a bucar
     * @return null si no se enccuentra puestos de otra forma retorna el ultimo puestos del trabajador
     */
    public Puestos getUltimoPuesto(String fecha,Trabajadores trabajadores) {

        Puestos puestos = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS
                +" WHERE "+KEY_ENVIADO_PUESTOS +" = 0 AND "+KEY_FECHASTRING_PUESTOS+"='"+fecha+"'" +
                " AND "+KEY_CONSECUTIVO_PUESTOS+" = "+trabajadores.getConsecutivo()+" ORDER BY "+KEY_HORAINICIOSTRING_PUESTOS +" desc,"+KEY_ID_PUESTOS+" desc limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puestos = new Puestos(cursor,getTrabajdores(Long.parseLong(cursor.getString(6))),getCatalogoPuestos(Long.parseLong(cursor.getString(9))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        if(puestos!=null)
            FileLog.v(Complementos.TAG_BDHANDLER,"total Producto "+puestos.toString());

        return puestos;
    }

    public Producto getUltimoResgistroProduccion(Trabajadores trabajadores,String fecha) {

        Producto produccion = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCCION +" WHERE "+KEY_IDTRABAJADOR_PRODUCCION +" = "+trabajadores.getIdTrabajdor() +" AND "+KEY_FECHASTRING_PRODUCCION+" = '"+ fecha+"' order by "+KEY_DATE_PRODUCCION +" DESC limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                produccion = new Producto(cursor,getTrabajdores(Long.parseLong(cursor.getString(4))),getcatalogoActividades(Long.parseLong(cursor.getString(7))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        if(produccion!=null)
            FileLog.v(Complementos.TAG_BDHANDLER,"total Vehiculos "+produccion.toString());

        return produccion;
    }

    public ArrayList<Producto> getResgistrosProduccion(Trabajadores trabajadores,String fecha) {

        ArrayList<Producto> produccion = new ArrayList<Producto>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCCION +" WHERE "+KEY_IDTRABAJADOR_PRODUCCION +" = "+trabajadores.getIdTrabajdor()
                +" AND "+KEY_FECHASTRING_PRODUCCION+" = '"+ fecha+"' order by "+KEY_DATE_PRODUCCION +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                produccion.add(new Producto(cursor,getTrabajdores(Long.parseLong(cursor.getString(4))),getcatalogoActividades(Long.parseLong(cursor.getString(7)))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        FileLog.v(Complementos.TAG_BDHANDLER,"total total produccion"+produccion.size());

        return produccion;
    }

    public ArrayList<ReporteProduccion> getResgistrosProduccion(Trabajadores trabajador,Puestos puestos) {

        ArrayList<ReporteProduccion> produccion = new ArrayList<ReporteProduccion>();
        // Select All Query
        String selectQuery = "SELECT "+KEY_IDTRABAJADOR_PUESTOS+","+KEY_IDACTIVIDAD_PRODUCCION+", SUM("+KEY_PRIMERA_PRODUCCION+"),SUM("+KEY_SEGUNDA_PRODUCCION+"),SUM("+KEY_AGRANEL_PRODUCCION+"),MIN("+KEY_DATE_PRODUCCION+"),MAX("+KEY_DATE_PRODUCCION+")"
                +" FROM " + TABLE_PRODUCCION +" WHERE "+KEY_IDTRABAJADOR_PRODUCCION +" = "+trabajador.getIdTrabajdor()+" AND "+KEY_FECHASTRING_PRODUCCION+" = '"+ puestos.getFechaString()+"'"
                +" AND "+KEY_DATE_PRODUCCION+" BETWEEN  "+puestos.getDateInicio()+" AND "+puestos.getDateFin()
                +" GROUP BY "+KEY_IDTRABAJADOR_PUESTOS+","+KEY_IDACTIVIDAD_PRODUCCION
                +" order by "+KEY_DATE_PRODUCCION +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        //Log.i("EMAIL",selectQuery);
        if (cursor.moveToFirst()) {
            do {
                produccion.add(new ReporteProduccion(cursor, trabajador, getcatalogoActividades(cursor.getLong(1)),puestos));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        FileLog.v(Complementos.TAG_BDHANDLER,"total total produccion"+produccion.size());

        return produccion;
    }

    /**
     * obtener la produccion por trabajador y actividad
     * @param trabajador
     * @param actividad
     * @param fecha
     * @return
     */
    public ReporteProduccion getTotalProduccionTrabajador(Trabajadores trabajador, CatalogoActividades actividad , String fecha) {
        ReporteProduccion reporte = null;
        // Select All Query
        String selectQuery = "SELECT "+KEY_ID_TRABAJADORES+","+KEY_IDACTIVIDAD_PRODUCCION+", SUM("+KEY_PRIMERA_PRODUCCION+"),SUM("+KEY_SEGUNDA_PRODUCCION+"),SUM("+KEY_AGRANEL_PRODUCCION+")" +
                " FROM " + TABLE_PRODUCCION + " WHERE " + KEY_ENVIADO_PRODUCCION + " = 0 AND "+KEY_FECHASTRING_PRODUCCION+" = '"+fecha+"' "+
                " AND "+KEY_IDTRABAJADOR_PRODUCCION+" = "+trabajador.getIdTrabajdor()+" AND "+KEY_IDACTIVIDAD_PRODUCCION+" = "+actividad.getId()+
                " GROUP BY "+KEY_FECHASTRING_PRODUCCION+","+KEY_IDACTIVIDAD_PRODUCCION+","+KEY_IDTRABAJADOR_PRODUCCION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                reporte = new ReporteProduccion(cursor,trabajador,actividad);
                FileLog.v(Complementos.TAG_BDHANDLER,"Recargas sin enviar "+reporte.toString());
            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        return reporte;
    }

    /***
     * obtener total de produccion por fecha
     * @param fecha dia a obtener la produccion
     * @param trabajador si es diferente de null se obtentra total de produccion por actividad del trabajado
     * @return ArrayList de ReporteProduccion
     */
    public ArrayList<ReporteProduccion> getReporte(String fecha,Trabajadores trabajador) {
        ArrayList<ReporteProduccion> reporte = new ArrayList<ReporteProduccion>();
        // Select All Query
        String selectQuery = "SELECT "+KEY_IDTRABAJADOR_PRODUCCION+","+KEY_IDACTIVIDAD_PRODUCCION+",SUM("+KEY_PRIMERA_PRODUCCION+"),SUM("+KEY_SEGUNDA_PRODUCCION+"),SUM("+KEY_AGRANEL_PRODUCCION+")" +
                " FROM " + TABLE_PRODUCCION + " WHERE " + KEY_ENVIADO_PRODUCCION + " = 0 AND "+KEY_FECHASTRING_PRODUCCION+" = '"+fecha+"' " ;

        if(trabajador!=null){
            selectQuery=selectQuery +" AND "+KEY_IDTRABAJADOR_PRODUCCION+" = "+trabajador.getIdTrabajdor();
        }
        selectQuery=selectQuery+" GROUP BY "+KEY_IDTRABAJADOR_PRODUCCION+","+KEY_IDACTIVIDAD_PRODUCCION+","+KEY_FECHASTRING_PRODUCCION
                +" ORDER BY "+KEY_IDTRABAJADOR_PRODUCCION+","+KEY_IDACTIVIDAD_PRODUCCION+","+KEY_FECHASTRING_PRODUCCION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                Trabajadores t = trabajador!=null?trabajador:getTrabajdores(Long.parseLong(cursor.getString(0)));
                CatalogoActividades actividades = getcatalogoActividades(Long.parseLong(cursor.getString(1)));
                reporte.add(new ReporteProduccion(cursor,t,actividades));
            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        FileLog.v(Complementos.TAG_BDHANDLER,"Recargas sin enviar "+reporte.size());
        return reporte;
    }

    //////////////////// UPDATE //////////////////////////

    /**
     * actualizar la tabla configuracion
     * @param configuracion valor a actualizar
     * @return -1 si la actualziacion fallo de los contrario retorna el numero de filas actualizadas
     */
    public int updateConfiguracion(Configuracion configuracion){
        FileLog.v(Complementos.TAG_BDHANDLER,"ACTUALIZAR CONFIGURACION="+configuracion.getId());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_URL_CONFIGURACION,configuracion.getUrlApi());
            values.put(KEY_PARA_CONFIGURACION,configuracion.getPara());
            values.put(KEY_CC_CONFIGURACION,configuracion.getCc());
            values.put(KEY_ASUNTO_CONFIGURACION,configuracion.getAsunto());
            values.put(KEY_MENSAJE_CONFIGURACION,configuracion.getMensaje());
            values.put(KEY_TIEMPOMAS_CONFIGURACION,configuracion.getTiempoMas());
            values.put(KEY_TIEMPOMENOS_CONFIGURACION,configuracion.getTiempoMenos());

            i = db.update(TABLE_CONFIGURACION, values, KEY_ID_CONFIGURACION + " = ?",
                    new String[]{String.valueOf(configuracion.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

    /**
     * actuliza la tabla setting
     * @param settings valor a actualizar
     * @return -1 si la actualziacion fallo de los contrario retorna el numero de filas actualizadas
     */
    public int updateSetting(Settings settings){
        FileLog.v(Complementos.TAG_BDHANDLER,"ACTUALIZAR SETTING "+settings.getId());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_DATEINICIO_SETTINGS, settings.getFechaInicio());
            values.put(KEY_DATEFIN_SETTINGS, settings.getFechaFinal());
            values.put(KEY_FECHASTRING_SETTINGS,settings.getFechaString());
            values.put(KEY_HORAINICIOSTRING_SETTINGS, settings.getHoraInicioString());
            values.put(KEY_HORAFINSTRING_SETTINGS, settings.getHoraFinalString());
            values.put(KEY_JORNADAFINALIZADA_SETTINGS, settings.getFinJornada());
            values.put(KEY_JORNADAINICIADA_SETTINGS, settings.getInicioJornada());
            values.put(KEY_ENVIODATOS_SETTINGS, settings.getEnviarInformacion());
            values.put(KEY_TOTALEMPLEADOS_SETTINGS, settings.getCapturaTrabaajdor());
            values.put(KEY_IDACTIVIDAD_SETTINGS, settings.getActividades().getId());
            values.put(KEY_ACTIVIDAD_SETTINGS, settings.getActividades().getDescripcion());

            i = db.update(TABLE_SETTINGS, values, KEY_ID_SETTINGS + " = ?",
                    new String[]{String.valueOf(settings.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

    /**
     * actualiza la tabla puestos
     * @param puestos valor a actualizar
     * @return -1 si la actualziacion fallo de los contrario retorna el numero de filas actualizadas
     */
    public int updatePuestos(Puestos puestos){
        FileLog.v(Complementos.TAG_BDHANDLER,puestos.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_DATE_PUESTOS, puestos.getDateInicio());
            values.put(KEY_FECHASTRING_PUESTOS, puestos.getFechaString());
            values.put(KEY_HORAINICIOSTRING_PUESTOS, puestos.getHoraInicioString());
            values.put(KEY_DATEFIN_PUESTOS, puestos.getDateFin());
            values.put(KEY_HORAFINSTRING_PUESTOS, puestos.getHoraFinalString());
            values.put(KEY_IDTRABAJADOR_PUESTOS, puestos.getIdTrabajdor());
            values.put(KEY_CONSECUTIVO_PUESTOS, puestos.getConsecutivo());
            values.put(KEY_TRABAJADOR_PUESTOS, puestos.getTrabajador());
            values.put(KEY_IDPUESTO_PUESTOS, puestos.getPuestos().getId());
            values.put(KEY_ENVIADO_PUESTOS, puestos.getEnviado());

            i = db.update(TABLE_PUESTOS, values, KEY_ID_PUESTOS + " = ?",new String[]{String.valueOf(puestos.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }
    public int updateNombreTrabajadorPuestos(Trabajadores trabajador){
        FileLog.v(Complementos.TAG_BDHANDLER,trabajador.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_TRABAJADOR_PUESTOS ,trabajador.getTrabajador());

            i = db.update(TABLE_PUESTOS, values, KEY_IDTRABAJADOR_PUESTOS + " = ? ",
                    new String[]{String.valueOf(trabajador.getIdTrabajdor().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

    /**
     * actuliza la tabla Producto
     * @param produccion valor a actualizar
     * @return -1 si la actualziacion fallo de los contrario retorna el numero de filas actualizadas
     */
    public int updateProduccion(Producto produccion){
        FileLog.v(Complementos.TAG_BDHANDLER,produccion.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_DATE_PRODUCCION ,produccion.getDate());
            values.put(KEY_HORASTRING_PRODUCCION ,produccion.getHoraString());
            values.put(KEY_FECHASTRING_PRODUCCION ,produccion.getFechaString());
            values.put( KEY_IDTRABAJADOR_PRODUCCION,produccion.getIdTrabajdor());
            values.put(KEY_CONSECUTIVO_PRODUCCION ,produccion.getConsecutivo());
            values.put(KEY_TRABAJADOR_PRODUCCION ,produccion.getTrabajador());
            values.put(KEY_PRIMERA_PRODUCCION ,produccion.getPrimera());
            values.put(KEY_SEGUNDA_PRODUCCION,produccion.getSegunda());
            values.put(KEY_AGRANEL_PRODUCCION ,produccion.getAgranel());
            values.put( KEY_ENVIADO_PRODUCCION,produccion.getEnviado());

            i = db.update(TABLE_PRODUCCION, values, KEY_ID_PUESTOS + " = ?",
                    new String[]{String.valueOf(produccion.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

    public int updateNombreTrabajadorProduccion(Trabajadores trabajador){
        FileLog.v(Complementos.TAG_BDHANDLER,trabajador.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_TRABAJADOR_PRODUCCION ,trabajador.getTrabajador());

            i = db.update(TABLE_PRODUCCION, values, KEY_IDTRABAJADOR_PRODUCCION + " = ? ",
                    new String[]{String.valueOf(trabajador.getIdTrabajdor().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

    /**
     * actualiza la tabla trabajdores
     * @param trabajadores valor a actualizar
     * @return -1 si la actualziacion fallo de los contrario retorna el numero de filas actualizadas
     */
    public int updateTrabajdores(Trabajadores trabajadores){
        FileLog.v(Complementos.TAG_BDHANDLER,trabajadores.toString());

        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CONSECUTIVO_TRABAJADORES, trabajadores.getConsecutivo());
            values.put(KEY_NOMBRE_TRABAJADORES , trabajadores.getNombre());
            values.put(KEY_APELLIDOPATERNO_TRABAJADORES, trabajadores.getApellidoPaterno());
            values.put(KEY_APELLIDOMATERNO_TRABAJADORES, trabajadores.getApellidoMaterno());
            values.put(KEY_IDPUESTO_TRABAJADORES, trabajadores.getPuestosActual().getId());

            i = db.update(TABLE_TRABAJADORES, values, KEY_ID_TRABAJADORES + " = ?",
                    new String[]{String.valueOf(trabajadores.getIdTrabajdor().toString())});
            db.close();

            FileLog.i("verificacionHora",i+"--"+trabajadores.toString());
        }catch (Exception e){
            FileLog.v(Complementos.TAG_BDHANDLER,"error "+e.getMessage());
        }
        return i;
    }

}
