package br.com.realtour.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Unit {


    // make integration with google maps api
    private String id;
    private Float latitude;
    private Float longitude;
    private Float price;
    private Realtor realtor;

/*
    public Unit(createUnitDTO dto){

    }*/

    public void changePrice(Float price){
        this.price = price;
    }

    
}
