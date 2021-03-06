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

/**
 *
 * @author mdominguez
 */
public class LoadLog {
    private static String directorio = "/home/mdominguez/desarrollos/tutoriales/ind/monitorsantafe/20160422/";
    private static String archivo = "ge.gc-date_2016042206.log";
    
    public static void main(String[] args){        
        try {
            String x ="mdominguez-098f6bcd4621d373cade4e832627b4f6";
            System.out.println(Arrays.toString(x.split("\\|")));
            String string = "004-034556";
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            System.out.println(Arrays.toString(parts));
            System.out.println(part1);
            System.out.println(part2);                 
        } catch (Exception ex) {
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
                  //System.out.println(cicloGc[0]+"==== "+cicloGc[1]+Arrays.toString(cicloGc)+ " *** " +cicloGc[cicloGc.length-2]);
//                System.out.println(cicloGc[1]+" es.."+cicloGc[1].trim().equalsIgnoreCase("[GC"));                
                try{
                    if(cicloGc[1].trim().equalsIgnoreCase("[GC")){
      //                  System.out.println(cicloGc[cicloGc.length-2]+ "****"+Arrays.toString(cicloGc));
                        tiempoYoung += Double.valueOf(cicloGc[cicloGc.length-2]);
                        r.addYoung(Double.valueOf(cicloGc[cicloGc.length-2]),cicloGc[0]);
                    }else{
                        if(cicloGc[1].trim().equalsIgnoreCase("[Full")){
                            tiempoOld += Double.valueOf(cicloGc[cicloGc.length-2]) ;                   
                            r.addOld(Double.valueOf(cicloGc[cicloGc.length-2]),cicloGc[0]);
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
