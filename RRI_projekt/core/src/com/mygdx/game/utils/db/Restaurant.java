package com.mygdx.game.utils.db;

import java.util.List;

public class Restaurant {
    String ime;
    String lokacija;
    List<Double> loc;

    public Restaurant(String name, List<Double> loc, String street){}
    public Restaurant(String ime, Boolean isBlooming, String lokacija, List<Double> loc) {
        this.ime = ime;
        this.lokacija = lokacija;
        this.loc = loc;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setLoc(List<Double> loc) {
        this.loc = loc;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getIme() {
        return ime;
    }

    public List<Double> getLoc() {
        return loc;
    }

    public String getLokacija() {
        return lokacija;
    }
}
