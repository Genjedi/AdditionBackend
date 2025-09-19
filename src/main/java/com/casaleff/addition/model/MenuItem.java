package com.casaleff.addition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Data
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(length = 1000)
    private String description;

    private float price;

    private String path;

    // Each item belongs to one Menu
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // DB-level cascade
    private Menu menu;

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
