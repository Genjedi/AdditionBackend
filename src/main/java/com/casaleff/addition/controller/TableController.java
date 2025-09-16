package com.casaleff.addition.controller;

import com.casaleff.addition.DTO.TableDTO;
import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@Slf4j
public class TableController {

    @Autowired
    TableService tableService;

    @PostMapping("/add")
    TableDTO addTable(@RequestParam String number, @RequestParam int areaID) {
        log.info("Add table by the number {}", number);
        return tableService.addTable(number, areaID);
    }

    @DeleteMapping("/delete")
    TableDTO deleteTable(@RequestParam String number) {
        log.info("Delete table by the number {}", number);
        return tableService.deleteTable(number);
    }

    @PostMapping("/order")
    TableDTO addItems(@RequestBody List<MenuItem> menuItem, @RequestParam int tableID) {
        log.info("Add item to table by the id {}", tableID);
        return tableService.addOrders(menuItem, tableID);
    }

    @DeleteMapping("/remove")
    TableDTO deleteItems(@RequestBody MenuItem menuItem, @RequestParam int tableID) {
        log.info("Delete items by the id {}", tableID);
        return tableService.removeOrder(menuItem, tableID);
    }

    @PutMapping("/payment")
    TableDTO pay(@RequestBody List<MenuItem> items, @RequestParam int tableID) {
        log.info("Pay table by the id {}", tableID);
        return tableService.payOrders(items, tableID);
    }

    @PutMapping("/lock")
    void lockTable(@RequestParam int tableID, @RequestParam boolean lock) {
        log.info("Table lock by the tableID {} has changed to {}", tableID, lock);
        tableService.setTableLock(tableID, lock);
    }

    @GetMapping("/get")
    TableDTO getTable(@RequestParam int tableID) {
        log.info("Get table by the tableID {}", tableID);
        return tableService.tableDTOById(tableID);
    }

    @GetMapping("/getEmpty")
    List<TableDTO> getEmptyTables() {
        log.info("Get empty tables");
        return tableService.findEmptyTables();
    }

    @GetMapping("/getFull")
    List<TableDTO> getFullTables() {
        log.info("Get full tables");
        return tableService.findNonEmpty();
    }

    @GetMapping("/getAll")
    List<TableDTO> getTables() {
        log.info("Get all tables");
        return tableService.findAllTables();
    }


}
