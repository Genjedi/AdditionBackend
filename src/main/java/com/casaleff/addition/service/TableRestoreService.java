package com.casaleff.addition.service;

import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.model.MyTable;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.List;

@Slf4j
public class TableRestoreService {

    private static final Map<Integer, RestoredData> cache = new HashMap<>();

    public static void getCache(MyTable myTable) {
        log.info("getCache hit");
        RestoredData restoredData = cache.get(myTable.getId());

        if (restoredData == null) {
            restoredData = new RestoredData();
            restoredData.orders = myTable.getOrders() != null ? myTable.getOrders() : new ArrayList<>();
            restoredData.paidItems = myTable.getPaidItems() != null ? myTable.getPaidItems() : new ArrayList<>();
            restoredData.locked = false;
        }

        myTable.setOrders(restoredData.orders);
        myTable.setPaidItems(restoredData.paidItems);
        myTable.setLocked(restoredData.locked);
        cache.put(myTable.getId(), restoredData);
    }

    public static class RestoredData {
        public List<MenuItem> orders;
        public List<MenuItem> paidItems;
        public boolean locked;
    }
}
