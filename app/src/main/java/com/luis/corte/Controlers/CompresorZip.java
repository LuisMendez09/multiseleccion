package com.luis.corte.Controlers;

import android.os.Build;

import com.luis.corte.complementos.FileLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompresorZip {
    private static final String TAG ="CompresorZip";
    private static boolean respuesta;

    /**
     * funcion para respaldar, comrpimir y eliminar archivos
     * @param archivoAcomprimir ruta del archivo a comprimir
     * @param destinoArchivo ruta donde se guardaran los archivos comprimidos
     * @param antiguedad tiempo en que se mantendran los archivos respaldados
     * @return
     */
     public static final boolean comprimir(String archivoAcomprimir,String destinoArchivo,Integer antiguedad) {
         ZipOutputStream zipOutputStream = null;
         FileInputStream fileInputStreamEntrada = null;
         ZipEntry zipEntry = null;
         File archivoComprimir = null;
         File ArchivoDestino = null;

         FileLog.i(TAG,"inicia el proceso de compresion");

         String origen =archivoAcomprimir;

         String destino =destinoArchivo;
         ArchivoDestino = new File(destino);

         if(!ArchivoDestino.getParentFile().exists()){
             ArchivoDestino.getParentFile().mkdirs();
         }

         FileOutputStream  fileOutputStreamDestico = null;
         try {
             fileOutputStreamDestico = new FileOutputStream(destino);

             zipOutputStream = new ZipOutputStream(fileOutputStreamDestico);

             archivoComprimir = new File(origen);

             fileInputStreamEntrada = new FileInputStream(archivoComprimir);
             zipEntry = new ZipEntry(archivoComprimir.getName());
             zipOutputStream.putNextEntry(zipEntry);

             byte[] bytes = new byte[1024];
             int length;
             while((length = fileInputStreamEntrada.read(bytes)) >= 0) {
                 zipOutputStream.write(bytes, 0, length);
             }

             zipOutputStream.flush();
             zipOutputStream.close();
             fileInputStreamEntrada.close();
             fileOutputStreamDestico.close();
             FileLog.i(TAG,"termina el proceso de compresion");

             if(antiguedad>0){
                 //borrar archivos antiguos
                 borrarArchivos(ArchivoDestino,antiguedad);
             }

             respuesta = true;
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             FileLog.e(TAG,e.getMessage());
             respuesta = false;
         } catch (IOException e) {
             e.printStackTrace();
             FileLog.e(TAG,e.getMessage());
             respuesta = false;
         }finally {
             zipOutputStream = null;
             fileInputStreamEntrada = null;
             zipEntry = null;
             archivoComprimir = null;
             ArchivoDestino = null;
         }

         return respuesta;
     }

    /**
     * borrado de archivos de respaldo
     * @param archivo archovo a borrar
     * @param antiguedad tiempo de antiguedad en minutos
     * @throws IOException
     */
     private static void borrarArchivos(File archivo,Integer antiguedad) throws IOException {
        File directorio = new File(archivo.getParent());
         FileLog.i(TAG,"Inicia el proceso borrado");
        if(directorio.isDirectory()){
            long timeUltimoArchivo;
            BasicFileAttributes attributes = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                attributes = Files.readAttributes(archivo.toPath(), BasicFileAttributes.class);
                timeUltimoArchivo = attributes.creationTime().toMillis();
            }else{
                timeUltimoArchivo = archivo.lastModified();
            }

            File archivos [] = directorio.listFiles();

            for (int i =0;i<archivos.length;i++){
                Long time;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    attributes = Files.readAttributes(archivos[i].toPath(), BasicFileAttributes.class);
                    time = attributes.creationTime().toMillis();
                }else{
                    time = archivos[i].lastModified();
                }

                long dif =  (((timeUltimoArchivo-time)/1000)/60);
                if(dif>10){
                    borrar(archivos[i]);
                }
                FileLog.i(TAG,"Finalizo el proceso borrado");
            }
        }
     }

     private static void borrar(File archivoBorrar){
         if(archivoBorrar.isDirectory()){
             for (File a:archivoBorrar.listFiles()) {
                 borrar(a);
             }
             archivoBorrar.delete();
         }else{
             archivoBorrar.delete();
         }
         FileLog.i(TAG,"borrar archivo: "+archivoBorrar.getName());
     }
    }
