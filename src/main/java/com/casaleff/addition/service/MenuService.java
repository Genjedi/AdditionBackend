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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * Adds a new menu, optionally as a submenu to an existing menu.
     */
    public Menu addMenu(String name, String parentMenuName) {
        log.info("Adding menu: '{}' with parent: '{}'", name, parentMenuName);

        Menu menu = new Menu();
        menu.setName(name);

        if (parentMenuName != null) {
            Menu parent = findByName(parentMenuName);
            parent.getSubMenus().add(menu);
            log.debug("Added '{}' as a submenu to '{}'", name, parentMenuName);
        }

        Menu saved = menuRepository.save(menu);
        log.info("Menu '{}' saved with ID: {}", saved.getName(), saved.getId());
        return saved;
    }

    /**
     * Finds a menu by its name.
     */
    public Menu findByName(String name) {
        log.info("Looking for menu with name: '{}'", name);

        return menuRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Menu '{}' not found", name);
                    return new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such menu");
                });
    }

    /**
     * Returns the main menu.
     */
    public Menu findMainMenu() {
        log.info("Retrieving main menu");

        return menuRepository.findByName("MainMenu")
                .orElseThrow(() -> {
                    log.warn("Main menu not found");
                    return new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such menu");
                });
    }

    /**
     * Returns a list of menu names including submenus in a flat string.
     */
    public List<String> menuList() {
        log.info("Generating list of all menus with their submenus");

        List<Menu> menuList = menuRepository.findAll();
        List<String> menuNameList = new ArrayList<>();

        for (Menu menu : menuList) {
            StringBuilder menuLine = new StringBuilder(menu.getName());

            List<Menu> subMenus = menu.getSubMenus();
            if (!subMenus.isEmpty()) {
                for (Menu subMenu : subMenus) {
                    menuLine.append(", ").append(subMenu.getName());
                }
            }

            menuNameList.add(menuLine.toString());
            log.debug("Menu entry: {}", menuLine);
        }

        log.info("Total menus processed: {}", menuNameList.size());
        return menuNameList;
    }

    /**
     * Adds a list of items to a menu.
     */
    public Menu addItems(List<MenuItem> items, String menuName) {
        log.info("Adding {} items to menu '{}'", items.size(), menuName);

        Menu menu = findByName(menuName);

        for (MenuItem item : items) {
            item.setMenu(menu);
            menuItemRepository.save(item);
            menu.getItems().add(item);
            log.debug("Added item '{}' to menu '{}'", item.getName(), menu.getName());
        }

        Menu updated = menuRepository.save(menu);
        log.info("Menu '{}' updated with new items", updated.getName());
        return updated;
    }

    /**
     * Removes a menu item by its ID.
     */
    public void removeItem(Integer itemID) {
        log.info("Attempting to remove item with ID: {}", itemID);

        MenuItem item = menuItemRepository.findById(itemID)
                .orElseThrow(() -> {
                    log.warn("Item with ID {} not found", itemID);
                    return new BaseException(ErrorType.ENTITY_NOT_FOUND, "No such item");
                });

        menuItemRepository.delete(item);
        log.info("Item with ID {} removed", itemID);
    }

    /**
     * Deletes a menu by name.
     */
    public Menu removeMenu(String name) {
        log.info("Removing menu with name: '{}'", name);

        Menu menu = findByName(name);
        menuRepository.delete(menu);

        log.info("Menu '{}' deleted", name);
        return menu;
    }
}
