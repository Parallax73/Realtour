package br.com.realtour.entity;


import br.com.realtour.util.CreateUnitDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Unit {


    // make integration with google maps api
    @Id
    private String id;
    private String address;
    private String number;
    private String neighbourhood;
    private String city;
    private Float price;
    private Realtor realtor;


    public Unit(CreateUnitDTO dto){
        this.number = dto.number();
        this.price = dto.price();
    }

    public void changePrice(Float price){
        this.price = price;
    }

    
}
