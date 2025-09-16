package com.casaleff.addition.controller;

import com.casaleff.addition.model.Menu;
import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/getMainMenu")
    public Menu getMainMenu() {
        //The menu that contains all other menus must be called "MainMenu"
        return menuService.findMainMenu();
    }

    @GetMapping("/get")
    public Menu getMenuByName(@RequestParam String name) {
        //Request Menu by name
        return menuService.findByName(name);
    }

    @GetMapping("/list")
    public List<String> listMenu() {
        return menuService.menuList();
    }

    @PostMapping("/add")
    public Menu addMenu(@RequestParam String name, @RequestParam(required = false) String parentMenu) {
       return menuService.addMenu(name, parentMenu);
    }

    @PostMapping("/addItems")
    public Menu addItems(@RequestBody List<MenuItem> items, @RequestParam String menu) {
        return menuService.addItems(items, menu);
    }

    @DeleteMapping("/remove")
    public Menu removeMenu(@RequestParam String menuName) {
        return menuService.removeMenu(menuName);
    }

    @DeleteMapping("/removeItem")
    public ResponseEntity<String> removeItem(@RequestParam Integer itemID) {
        menuService.removeItem(itemID);
        return new  ResponseEntity<>("Item Deleted", HttpStatus.OK);
    }
}
