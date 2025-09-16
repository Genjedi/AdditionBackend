package com.casaleff.addition.model;

import com.casaleff.addition.service.TableRestoreService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Slf4j
public class MyTable {
    @Id
    @GeneratedValue
    private Integer id;

    private String number; // e.g. "Table 1", "Table 2"

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @Transient  // 👈 orders are in memory only, not persisted
    private List<MenuItem> orders = new ArrayList<>();

    @Transient
    private List<MenuItem> paidItems = new ArrayList<>();

    @Transient
    private boolean locked = false;

    @PostLoad
    public void restore() {
        TableRestoreService.getCache(this);
    }
}

