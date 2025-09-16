package com.casaleff.addition.DTO;

import com.casaleff.addition.model.MenuItem;
import lombok.Data;

import java.util.List;

@Data
public class TableDTO {
    private Integer id;
    private String number;
    private int areaID;
    private List<MenuItem> orders;
    private List<MenuItem> paidItems;
    private boolean locked;
}
