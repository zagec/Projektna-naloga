package com.mygdx.game.utils.db;

import org.bson.types.ObjectId;

import java.util.List;

public class Restaurant_Rating {
    String ime;
    String lokacija;
    String cenaSStudentskimBonom;
    String cenaBrezStudentskegaBona;
    List<Double> loc;
    ObjectId id;
    float rating;

    public Restaurant_Rating(){}

    public Restaurant_Rating(String ime, String lokacija, String cenaSStudentskimBonom, String cenaBrezStudentskegaBona, List<Double> loc) {
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
        this.lokacija = lokacija;
        this.loc = loc;
        this.cenaSStudentskimBonom = cenaSStudentskimBonom;
        this.cenaBrezStudentskegaBona = cenaBrezStudentskegaBona;
    }
}
