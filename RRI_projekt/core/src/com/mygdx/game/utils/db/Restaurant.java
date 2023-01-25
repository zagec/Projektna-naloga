package com.mygdx.game.utils.db;

import org.bson.types.ObjectId;

import java.util.List;

public class Restaurant {
    String ime;
    String lokacija;
    String cenaSStudentskimBonom;
    String cenaBrezStudentskegaBona;
    List<Double> loc;
    ObjectId id;
    float rating;

    public Restaurant(){}

    public Restaurant(Restaurant rest, float rating){
        this.id = rest.getId();
        this.ime = rest.getIme();
        this.lokacija =  rest.getLokacija();
        this.loc =  rest.getLoc();
        this.cenaSStudentskimBonom =  rest.getCenaSStudentskimBonom();
        this.cenaBrezStudentskegaBona =  rest.getCenaBrezStudentskegaBona();
        this.rating = rating;

    }

    public Restaurant(ObjectId id, String ime, String lokacija, String cenaSStudentskimBonom, String cenaBrezStudentskegaBona, List<Double> loc) {
        ime = ime.replaceAll("š","s");
        ime = ime.replaceAll("Š","S");
        ime = ime.replaceAll("č","c");
        ime = ime.replaceAll("Č","C");
        ime = ime.replaceAll("ž","z");
        ime = ime.replaceAll("Ž","Z");

        lokacija = lokacija.replaceAll("š","s");
        lokacija = lokacija.replaceAll("Š","S");
        lokacija = lokacija.replaceAll("č","c");
        lokacija = lokacija.replaceAll("Č","C");
        lokacija = lokacija.replaceAll("ž","z");
        lokacija = lokacija.replaceAll("Ž","Z");
        this.ime = ime;
        this.id = id;
        this.lokacija = lokacija;
        this.loc = loc;
        this.cenaSStudentskimBonom = cenaSStudentskimBonom;
        this.cenaBrezStudentskegaBona = cenaBrezStudentskegaBona;
    }

    public void setIme(String ime) {
        ime = ime.replaceAll("š","s");
        ime = ime.replaceAll("Š","S");
        ime = ime.replaceAll("č","c");
        ime = ime.replaceAll("Č","C");
        ime = ime.replaceAll("ž","z");
        ime = ime.replaceAll("Ž","Z");

        this.ime = ime;
    }
    public void setLoc(List<Double> loc) {this.loc = loc;}
    public void setLokacija(String lokacija) {
        lokacija = lokacija.replaceAll("š","s");
        lokacija = lokacija.replaceAll("Š","S");
        lokacija = lokacija.replaceAll("č","c");
        lokacija = lokacija.replaceAll("Č","C");
        lokacija = lokacija.replaceAll("ž","z");
        lokacija = lokacija.replaceAll("Ž","Z");
        this.lokacija = lokacija;
    }
    public void setCenaSStudentskimBonom(String cenaSStudentskimBonom) {this.cenaSStudentskimBonom = cenaSStudentskimBonom;}
    public void setCenaBrezStudentskegaBona(String cenaBrezStudentskegaBona) {this.cenaBrezStudentskegaBona = cenaBrezStudentskegaBona;}
    public void setId(ObjectId id) { this.id = id; }
    public void setRating(float rating) { this.rating = rating; }

    public String toString(){
        return "id: " + this.id.toString() + ", name: " + this.ime + ", lokacija: " + this.lokacija + ", coords: " + this.loc.get(0) + " + " + this.loc.get(1) +
                 ", price Without bon: " +this.cenaSStudentskimBonom + ", price with bon: " + this.cenaBrezStudentskegaBona + ", rating: " +this.rating;
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
    public String getCenaSStudentskimBonom() {
        return cenaSStudentskimBonom;
    }
    public String getCenaBrezStudentskegaBona() {
        return cenaBrezStudentskegaBona;
    }
    public ObjectId getId() {return id;}
    public float getRating() {return rating;}
}
