package com.casaleff.addition.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    List<MenuItem> items;

    @OneToMany(cascade = CascadeType.ALL)
    List<Menu> subMenus;
}
