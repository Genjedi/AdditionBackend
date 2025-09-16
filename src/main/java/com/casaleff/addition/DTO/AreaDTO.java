package com.casaleff.addition.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AreaDTO {
    private Integer id;
    private String name;
    List<TableDTO> tables;
}
