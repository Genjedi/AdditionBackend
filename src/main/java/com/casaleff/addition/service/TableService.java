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

@Slf4j
@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private PaymentService paymentService;

    public TableDTO addTable(String number, int areaID) {
        log.info("Attempting to add table '{}' to area ID {}", number, areaID);

        MyTable table = new MyTable();
        table.setNumber(number);

        Optional<Area> area = areasRepository.findById(areaID);
        if (area.isEmpty()) {
            log.error("Area with ID {} not found", areaID);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No area by id " + areaID);
        }

        table.setArea(area.get());
        tableRepository.save(table);

        log.info("Table '{}' added successfully to area '{}'", number, area.get().getName());
        return createDTO(table);
    }

    public TableDTO deleteTable(String tableNumber) {
        log.info("Attempting to delete table '{}'", tableNumber);

        var tableOptional = tableRepository.findByNumber(tableNumber);
        if (tableOptional.isEmpty()) {
            log.error("Table '{}' not found", tableNumber);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No table by number " + tableNumber);
        }

        var table = tableOptional.get();
        tableRepository.delete(table);

        log.info("Table '{}' deleted successfully", tableNumber);
        return createDTO(table);
    }

    public List<TableDTO> findAllTables() {
        log.info("Retrieving all tables");
        var tables = tableRepository.findAll();

        List<TableDTO> dtos = new ArrayList<>();
        for (var table : tables) {
            dtos.add(createDTO(table));
        }

        log.info("Retrieved {} tables", dtos.size());
        return dtos;
    }

    public List<TableDTO> findEmptyTables() {
        log.info("Finding empty tables (no orders)");
        var tables = tableRepository.findAll();

        List<TableDTO> dtos = new ArrayList<>();
        for (var table : tables) {
            if (table.getOrders().isEmpty()) {
                dtos.add(createDTO(table));
            }
        }

        log.info("Found {} empty tables", dtos.size());
        return dtos;
    }

    public List<TableDTO> findNonEmpty() {
        log.info("Finding non-empty tables (with orders)");
        var tables = tableRepository.findAll();

        List<TableDTO> dtos = new ArrayList<>();
        for (var table : tables) {
            if (!table.getOrders().isEmpty()) {
                dtos.add(createDTO(table));
            }
        }

        log.info("Found {} non-empty tables", dtos.size());
        return dtos;
    }

    public TableDTO tableDTOById(int tableID) {
        log.info("Retrieving table DTO by ID {}", tableID);
        return createDTO(findTableById(tableID));
    }

    private MyTable findTableById(int tableID) {
        log.debug("Looking up table by ID {}", tableID);
        return tableRepository.findById(tableID)
                .orElseThrow(() -> {
                    log.error("Table with ID {} not found", tableID);
                    return new BaseException(ErrorType.ENTITY_NOT_FOUND, "No table found with id " + tableID);
                });
    }

    public void setTableLock(int tableID, boolean lock) {
        log.info("Setting lock={} for table ID {}", lock, tableID);
        var table = findTableById(tableID);
        table.setLocked(lock);
        log.info("Table ID {} lock set to {}", tableID, lock);
    }

    public TableDTO createDTO(MyTable table) {
        log.debug("Creating DTO for table ID {}", table.getId());
        TableDTO tableDTO = new TableDTO();
        BeanUtils.copyProperties(table, tableDTO);
        tableDTO.setAreaID(table.getArea().getId());
        return tableDTO;
    }

    public TableDTO addOrders(List<MenuItem> items, int tableID) {
        log.info("Adding {} orders to table ID {}", items.size(), tableID);
        var table = findTableById(tableID);

        for (MenuItem item : items) {
            table.getOrders().add(item);
            log.debug("Added item '{}' to table ID {}", item.getName(), tableID);
        }

        return createDTO(table);
    }

    public TableDTO payOrders(List<MenuItem> items, int tableID) {
        log.info("Processing payment for {} items on table ID {}", items.size(), tableID);
        var table = findTableById(tableID);
        List<MenuItem> orders = table.getOrders();

        List<MenuItem> foundOrders = new ArrayList<>();
        for (MenuItem item : items) {
            if (orders.contains(item)) {
                foundOrders.add(item);
                log.debug("Order '{}' matched for payment", item.getName());
            }
        }

        if (foundOrders.isEmpty() || foundOrders.size() != items.size()) {
            log.error("Mismatch in orders to be paid on table ID {}", tableID);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No orders found");
        }

        orders.removeAll(foundOrders);
        table.getPaidItems().addAll(foundOrders);
        log.info("{} items marked as paid on table ID {}", foundOrders.size(), tableID);

        if (orders.isEmpty()) {
            paymentService.createPayment(table);
            log.info("Payment completed for table ID {}", tableID);
        }

        return createDTO(table);
    }

    public TableDTO removeOrder(MenuItem item, int tableID) {
        log.info("Attempting to remove order '{}' from table ID {}", item.getName(), tableID);
        var table = findTableById(tableID);

        if (!table.getOrders().isEmpty()) {
            if (table.getOrders().contains(item)) {
                table.getOrders().remove(item);
                log.info("Order '{}' removed from table ID {}", item.getName(), tableID);
                return createDTO(table);
            }
            log.warn("Order '{}' not found on table ID {}", item.getName(), tableID);
            throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "No order match found");
        }

        log.warn("No orders present on table ID {} to remove", tableID);
        throw new BaseException(ErrorType.ENTITY_NOT_FOUND, "There is no orders to remove");
    }
}