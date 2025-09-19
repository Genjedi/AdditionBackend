package com.casaleff.addition.service;

import com.casaleff.addition.DTO.AreaDTO;
import com.casaleff.addition.DTO.TableDTO;
import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.model.Area;
import com.casaleff.addition.model.MyTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casaleff.addition.repository.AreasRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AreaService {

    @Autowired
    private AreasRepository areasRepository;

    public AreaDTO addAreas(String name) {
        log.info("Adding area with name: {}", name);

        Area area = new Area();
        area.setName(name);
        areasRepository.save(area);

        log.info("Area added with ID: {}", area.getId());
        return createDTO(area);
    }

    public AreaDTO deleteAreas(Integer areaID) {
        log.info("Attempting to delete area with ID: {}", areaID);

        Optional<Area> area = areasRepository.findById(areaID);
        if (area.isEmpty()) {
            log.error("No area found by id: {}", areaID);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }

        areasRepository.delete(area.get());
        log.info("Area deleted with ID: {}", areaID);
        return createDTO(area.get());
    }

    public List<AreaDTO> getAreas() {
        log.info("Retrieving all areas");

        List<Area> areas = areasRepository.findAll();
        List<AreaDTO> areaDTOS = new ArrayList<>();
        for (Area area : areas) {
            AreaDTO areaDTO = createDTO(area);
            areaDTOS.add(areaDTO);
        }

        log.info("Retrieved {} areas", areaDTOS.size());
        return areaDTOS;
    }

    public AreaDTO getArea(Integer areaID) {
        log.info("Retrieving area with ID: {}", areaID);

        Optional<Area> area = areasRepository.findById(areaID);
        if (area.isEmpty()) {
            log.error("No area found by id: {}", areaID);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }

        log.info("Retrieved area with ID: {}", areaID);
        return createDTO(area.get());
    }

    private AreaDTO createDTO(Area area) {
        log.debug("Creating DTO for area with ID: {}", area.getId());

        AreaDTO areaDTO = new AreaDTO();
        BeanUtils.copyProperties(area, areaDTO);

        List<TableDTO> tableDTOList = new ArrayList<>();
        for (MyTable table : area.getMyTables()) {
            TableDTO tableDTO = new TableDTO();
            BeanUtils.copyProperties(table, tableDTO);
            tableDTO.setAreaID(area.getId());
            tableDTOList.add(tableDTO);
        }

        areaDTO.setTables(tableDTOList);
        log.debug("Created DTO for area with ID: {}", area.getId());
        return areaDTO;
    }
}
