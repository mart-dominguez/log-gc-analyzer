/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gov.santafe.meduc.load.gclog;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/**
 *
 * @author mdominguez
 */
public class LoadLog {
    private static String directorio = "/home/mdominguez/desarrollos/tutoriales/ind/log2/20160422/";
    private static String archivo = "ge.gc-date_2016042206.log";
    
    public static void main(String[] args){        
        try {
            /* LinkedHashSet<Reporte> reportes = new LinkedHashSet<Reporte>();
            Integer inicio = 20160422;
            Integer horaInicio = 6;
            //avanzo por dia
            for(int i=inicio;i<20160429;i++){
            //avanzo por hora
            for(int h=horaInicio;h<20;h++){
            Reporte rAux = getReporte(i, h);
            System.out.print(rAux);
            reportes.add(rAux);
            }
            }
            System.out.print(reportes);*/
            Key key = new AesKey(ByteUtil.randomBytes(16));
            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setPayload("Hello World!");
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
            jwe.setKey(key);
            String serializedJwe = jwe.getCompactSerialization();
            System.out.println("Serialized Encrypted JWE: " + serializedJwe);
            jwe = new JsonWebEncryption();
            jwe.setKey(key);
            jwe.setCompactSerialization(serializedJwe);
            System.out.println("Payload: " + jwe.getPayload());
        } catch (JoseException ex) {
            Logger.getLogger(LoadLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Reporte getReporte(Integer dia,Integer hora){
        ArrayList<String[]> elementos = new ArrayList<String[]>();
        Reporte r = new Reporte(dia, hora);
        System.out.println(r.getArchivoPath());
        try {
            Scanner scnr = new Scanner(new File(r.getArchivoPath()));
            Set<Integer> longitudes = new TreeSet<Integer>();
            while(scnr.hasNextLine()){
                String line = scnr.nextLine();
                if(line.length()>50)  {
                    String[] aux = line.split(" ");
                   // System.out.println("Agrego line   " + aux.length + " ----> "+line);
                    longitudes.add(aux.length);
                    elementos.add(aux);
                }
            }    
            //System.out.println(elementos.size());
            //System.out.println(Arrays.toString(elementos.get(20)));
            //System.out.println(longitudes.size());
            //System.out.println(longitudes);
            double tiempoYoung = 0.0;
            double tiempoOld = 0.0;
            for(String[] cicloGc : elementos){
                //System.out.println(cicloGc[1]+Arrays.toString(cicloGc)+ " *** " +cicloGc[cicloGc.length-2]);
//                System.out.println(cicloGc[1]+" es.."+cicloGc[1].trim().equalsIgnoreCase("[GC"));                
                try{
                    if(cicloGc[1].trim().equalsIgnoreCase("[GC")){
      //                  System.out.println(cicloGc[cicloGc.length-2]+ "****"+Arrays.toString(cicloGc));
                        tiempoYoung += Double.valueOf(cicloGc[cicloGc.length-2]);
                        r.addYoung(Double.valueOf(cicloGc[cicloGc.length-2]));
                    }else{
                        if(cicloGc[1].trim().equalsIgnoreCase("[Full")){
                            tiempoOld += Double.valueOf(cicloGc[cicloGc.length-2]) ;                   
                            r.addOld(Double.valueOf(cicloGc[cicloGc.length-2]));
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(Arrays.toString(cicloGc));
                }
            }
//                System.out.println("Young:"+tiempoYoung);
//                System.out.println("Old:"+tiempoOld);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoadLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
