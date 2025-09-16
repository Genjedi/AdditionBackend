package com.casaleff.addition.service;

import com.casaleff.addition.DTO.AreaDTO;
import com.casaleff.addition.DTO.TableDTO;
import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.model.Area;
import com.casaleff.addition.model.MyTable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casaleff.addition.repository.AreasRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AreasService {

    @Autowired
    AreasRepository areasRepository;

    public AreaDTO addAreas(String name){
        Area area = new Area();
        area.setName(name);
        areasRepository.save(area);

        return createDTO(area);
    }

    public AreaDTO deleteAreas(Integer areaID){
        Optional<Area> area = areasRepository.findById(areaID);
        if (area.isEmpty()) {
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }
        areasRepository.delete(area.get());
        return createDTO(area.get());
    }

    public List<AreaDTO> getAreas(){
        List<Area> areas = areasRepository.findAll();
        List<AreaDTO> areaDTOS = new ArrayList<>();
        for (Area area : areas) {
            AreaDTO areaDTO = createDTO(area);
            areaDTOS.add(areaDTO);
        }
        return areaDTOS;
    }

    public AreaDTO getArea(Integer areaID){
        Optional<Area> area = areasRepository.findById(areaID);
        if (area.isEmpty()) {
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }
        return createDTO(area.get());
    }

    private AreaDTO createDTO(Area area) {
        AreaDTO areaDTO = new AreaDTO();
        BeanUtils.copyProperties(area, areaDTO);
        List<TableDTO> tableDTOList = new ArrayList<>();
        for (MyTable table : area.getMyTables()){
            TableDTO tableDTO = new TableDTO();
            BeanUtils.copyProperties(table, tableDTO);
            tableDTO.setAreaID(area.getId());
            tableDTOList.add(tableDTO);
        }
        areaDTO.setTables(tableDTOList);
        return areaDTO;
    }
}
