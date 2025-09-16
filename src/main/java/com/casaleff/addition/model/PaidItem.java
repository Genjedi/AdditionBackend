package com.casaleff.addition.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaidItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    Payment payment;

    int menuId;

    String name;

    float price;


}
