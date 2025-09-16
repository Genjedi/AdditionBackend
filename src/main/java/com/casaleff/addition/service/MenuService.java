package com.casaleff.addition.service;

import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.model.Menu;
import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.repository.MenuItemRepository;
import com.casaleff.addition.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public Menu addMenu(String name, String parentMenuName){
        Menu menu = new Menu();
        menu.setName(name);
        if(parentMenuName != null){
           Menu parent = findByName(parentMenuName);
           parent.getSubMenus().add(menu);
        }
        return menuRepository.save(menu);
    }

    public Menu findByName(String name){
        Optional<Menu> menu = menuRepository.findByName(name);
        return menu.orElseThrow(() -> new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such menu"));
    }

    public Menu findMainMenu(){
        Optional<Menu> mainMenu = menuRepository.findByName("MainMenu");
        return mainMenu.orElseThrow(() -> new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such menu"));
    }

    public List<String> menuList(){
        List<Menu> menuList = menuRepository.findAll();
        List<String> menuNameList = new ArrayList<>();
        for(Menu menu : menuList){
            StringBuilder menus = new StringBuilder(menu.getName());
            List<Menu> subMenus = menu.getSubMenus();
           if(!subMenus.isEmpty()){
              for(Menu subMenu : menu.getSubMenus()){
                  menus.append(", ").append(subMenu.getName());
              }
           }
           menuNameList.add(menus.toString());
        }
        return menuNameList;
    }

    public Menu addItems(List<MenuItem> items, String menuName){
        Menu menu = findByName(menuName);

        for (MenuItem item : items) {
            menuItemRepository.save(item);
            menu.getItems().add(item);
        }
        return menuRepository.save(menu);
    }

    public void removeItem(Integer itemID){
        MenuItem item = menuItemRepository.findById(itemID).orElseThrow(() -> new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such item"));

        menuItemRepository.delete(item);
    }

    public Menu removeMenu(String name){
        var menu = findByName(name);
        menuRepository.delete(menu);
        return menu;
    }
}
