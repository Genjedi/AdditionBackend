package com.casaleff.addition.service;

import com.casaleff.addition.DTO.TableDTO;
import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.model.Area;
import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.model.MyTable;
import com.casaleff.addition.repository.AreasRepository;
import com.casaleff.addition.repository.TableRepository;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private PaymentService  paymentService;

    public TableDTO addTable(String number, int areaID){
        MyTable table = new MyTable();
        table.setNumber(number);
        Optional<Area> area = areasRepository.findById(areaID);
        if(area.isEmpty()){
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }

        table.setArea(area.get());

        tableRepository.save(table);
        return createDTO(table);
    }

    public TableDTO deleteTable(String tableNumber){
        var tableOptional =  tableRepository.findByNumber(tableNumber);
        if(tableOptional.isEmpty()){
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No table by id " + tableNumber);
        }
        var table =  tableOptional.get();
        tableRepository.delete(table);

        return createDTO(table);
    }

    public List<TableDTO> findAllTables(){
        var tables = tableRepository.findAll();
        List<TableDTO> dtos = new ArrayList<>();
        for (var table : tables){
            dtos.add(createDTO(table));
        }
        return dtos;
    }

    public List<TableDTO> findEmptyTables(){
        var tables = tableRepository.findAll();
        List<TableDTO> dtos = new ArrayList<>();

        for (var table : tables){
            if(table.getOrders().isEmpty()){
                dtos.add(createDTO(table));
            }
        }
        return dtos;
    }

    public List<TableDTO> findNonEmpty(){
        var tables = tableRepository.findAll();
        List<TableDTO> dtos = new ArrayList<>();

        for (var table : tables){
            if(!table.getOrders().isEmpty()){
                dtos.add(createDTO(table));
            }
        }
        return dtos;
    }

    public TableDTO tableDTOById(int tableID){
        return createDTO(findTableById(tableID));
    }

    private MyTable findTableById(int tableID){
        return tableRepository.findById(tableID).orElseThrow(() -> new BaseException(ErrorType.ENTITY_NOT_FOUND, "No table found with id " + tableID));
    }

    public void setTableLock(int tableID, boolean lock){
        var table = findTableById(tableID);
        table.setLocked(lock);
    }

    public TableDTO createDTO(MyTable table){
        TableDTO tableDTO = new TableDTO();
        BeanUtils.copyProperties(table, tableDTO);
        tableDTO.setAreaID(table.getArea().getId());
        return tableDTO;
    }

    public TableDTO addOrders(List<MenuItem> items, int tableID){
        var table = findTableById(tableID);
        for (MenuItem item : items){
            table.getOrders().add(item);
        }
        return createDTO(table);
    }

    public TableDTO payOrders(List<MenuItem> items, int tableID){
        var table = findTableById(tableID);
        List<MenuItem> orders = table.getOrders();
        List<MenuItem> foundOrders = new ArrayList<>();
        for (MenuItem item : items){
            if(orders.contains(item)){
                foundOrders.add(item);
            }
        }
        if (foundOrders.isEmpty() || foundOrders.size() != items.size()){
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No orders found");
        }
        orders.removeAll(foundOrders);
        table.getPaidItems().addAll(foundOrders);

        if (orders.isEmpty()){
            paymentService.createPayment(table);
            log.info("Payment Done");
        }

        return createDTO(table);
    }

    public TableDTO removeOrder(MenuItem item, int tableID){
        var table = findTableById(tableID);

        if (!table.getOrders().isEmpty()) {
            if (table.getOrders().contains(item)) {
                table.getOrders().remove(item);
                return createDTO(table);
            }
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No order match found");
        }
        throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "There is no orders to remove");
    }
}
