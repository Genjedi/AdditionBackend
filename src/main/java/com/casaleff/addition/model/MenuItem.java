package com.casaleff.addition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @Column(length = 1000)
    private String description;

    float price;

    String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem other = (MenuItem) o;
        return this.id == other.id && this.getPrice() == other.getPrice();  // assuming `id` uniquely identifies a menu item
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.getPrice());
    }
}
