package com.casaleff.addition.controller;

import com.casaleff.addition.DTO.AreaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.casaleff.addition.service.AreasService;

import java.util.List;

@RestController
@RequestMapping("/area")
@Slf4j
public class AreasController {

    @Autowired
    AreasService areasService;

    @PostMapping("/add")
    public AreaDTO addArea(@RequestParam("area") String area){
        log.info("Add area : {}", area);
        return areasService.addAreas(area);
    }

    @DeleteMapping("/delete")
    public AreaDTO deleteArea(@RequestParam("areaID") Integer areaID){
        log.info("Delete area : {}", areaID);
        return areasService.deleteAreas(areaID);
    }

    @GetMapping("/get")
    public AreaDTO getArea(@RequestParam("areaID") Integer areaID){
        log.info("Get area : {}", areaID);
        return areasService.getArea(areaID);
    }

    @GetMapping("/getAll")
    public List<AreaDTO> getAreas(){
        log.info("they hit the second api");
        return areasService.getAreas();
    }
}
