/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gov.santafe.meduc.load.gclog;

import java.util.LinkedHashSet;

/**
 *
 * @author mdominguez
 */
public class ReporteGCLog {
    private Integer dia;
    private Integer hora;
    private Double young;
    private Integer cantidadYoung;
    private Double old;
    private Integer cantidadOld;
    private String ultimaHora;
    private LinkedHashSet<String> horas;
    public ReporteGCLog(Integer dia,Integer hora){
        horas=new LinkedHashSet<String>();
        this.dia=dia;
        this.hora=hora;
        this.young = 0.0;
        this.cantidadYoung=0;
        this.old=0.0;
        this.cantidadOld=0;   
        this.ultimaHora="-1";
    }
    
    public String getClave(){
        if(hora<10) return dia+"0"+hora;
        return dia+""+hora;
    }
    
    public void addYoung(Double y,String hora){
        if(!this.ultimaHora.equalsIgnoreCase(hora)) {
            this.ultimaHora=hora;
            this.horas.add(hora);
            this.young=y;
            ++cantidadYoung;
        }
    }
    
    public void addOld(Double y,String hora){
        if(!this.ultimaHora.equalsIgnoreCase(hora)) {
            this.ultimaHora=hora;
            this.horas.add(hora);
            this.old=y;
            ++cantidadOld;
        }
    }
    
    public void printReport(){
        System.out.println(this.dia+";"+this.hora+";"+this.cantidadYoung+";"+this.young+";"+this.cantidadOld+";"+this.old);
    }
    
    public String getArchivoPath(){
        //return "/home/mdominguez/desarrollos/tutoriales/ind/log2/"+this.dia+"/ge.gc-date_"+this.getClave()+".log";   
        return "e:/srv/gclogs/ge.gc-date_"+this.getClave()+".log";   
    }
    
    public String toString(){
        return this.getClave()+";"+this.dia+";"+this.hora+";"+this.cantidadYoung+";"+this.young+";"+this.cantidadOld+";"+this.old+";"+horas.size()+"\n";
    }
}
