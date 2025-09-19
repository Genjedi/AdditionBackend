package com.casaleff.addition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // Menu items
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) // DB-level cascade
    private List<MenuItem> items = new ArrayList<>();

    // Parent menu for hierarchy
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_menu_id")
    private Menu parentMenu;

    // Submenus
    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) // DB-level cascade
    private List<Menu> subMenus = new ArrayList<>();
}
