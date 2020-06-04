package com.example.escuelaje.entidades;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Nota implements Serializable {
    private String tituloPrincipal;
    private String tituloSecundario;
    private String informacion;
    private String date;
    private String year, month, day;

    public Nota() {
    }

    public Nota(String tituloPrincipal, String tituloSecundario, String informacion, String year, String month, String day) {
        this.tituloPrincipal = tituloPrincipal;
        this.tituloSecundario = tituloSecundario;
        this.informacion = informacion;
        this.year = year;
        this.month = month;
        this.day = day;
        this.date=year+month+day;
    }


    public String getTituloPrincipal() {
        return tituloPrincipal;
    }

    public void setTituloPrincipal(String tituloPrincipal) {
        this.tituloPrincipal = tituloPrincipal;
    }

    public String getTituloSecundario() {
        return tituloSecundario;
    }

    public void setTituloSecundario(String tituloSecundario) {
        this.tituloSecundario = tituloSecundario;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String year,String month,String day) {
        String auxMonth=month;
        String auxDay=day;
        if(month.length()==1){
            auxMonth="0"+month;
        }
        if(day.length()==1){
            auxDay="0"+day;
        }

        this.date = year+auxMonth+auxDay;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "tituloPrincipal='" + tituloPrincipal + '\'' +
                ", tituloSecundario='" + tituloSecundario + '\'' +
                ", informacion='" + informacion + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        Nota aux = null;
        if (o != null && o instanceof Nota) {
            aux = (Nota) o;

            Boolean titPrin = tituloPrincipal.trim().equalsIgnoreCase(aux.getTituloPrincipal().trim());
            Boolean titSec = tituloSecundario.trim().equalsIgnoreCase(aux.getTituloSecundario().trim());
            Boolean des = informacion.trim().equalsIgnoreCase(aux.getInformacion().trim());
            Boolean mesito = month.trim().equalsIgnoreCase(aux.getMonth().trim());
            Boolean anito = year.trim().equalsIgnoreCase(aux.getYear().trim());
            Boolean diita = day.trim().equalsIgnoreCase(aux.getDay().trim());
            if (titPrin && titSec && des && mesito && anito && diita) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Nota> ordenarPorFecha(ArrayList<Nota> aux){
        Collections.sort(aux, new Comparator<Nota>() {
            @Override
            public int compare(Nota n1, Nota n2) {
                if(n1.getYear().equalsIgnoreCase(n2.getYear())){
                    if(n1.getMonth().equalsIgnoreCase(n2.getMonth())){
                        if(n1.getDay().equalsIgnoreCase(n2.getDay())){
                            return 0;
                        }else{
                            if(Integer.parseInt(n1.getDay())>Integer.parseInt(n2.getDay())){
                                return 1;
                            }else{
                                return -1;
                            }
                        }
                    }else{
                        if(Integer.parseInt(n1.getMonth())>Integer.parseInt(n2.getMonth())){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                }else{
                    if(Integer.parseInt(n1.getYear())>Integer.parseInt(n2.getYear())){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            }
        });
        return aux;
    }
}
