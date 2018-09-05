package com.example.crist.pictogramsapp;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * La clase que se usa para descomprimir archivos zip.
 *
 * @author Cristian Gonzalez Lopez
 */
public class Decompress {
    private String _zipFile;
    private String _location;

    /**
     * Instancia una nueva clase.
     *
     * @param zipFile  the zip file
     * @param location the location
     */
    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    /**
     * Funcion que descomprime un archivo zip en una carpeta.
     */
    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1){
                        bufout.write(buffer,0,read);
                    }

                   /* for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }*/
                    zin.closeEntry();
                    bufout.close();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
} 