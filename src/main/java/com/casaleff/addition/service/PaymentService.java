package com.casaleff.addition.service;

import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.model.MenuItem;
import com.casaleff.addition.model.MyTable;
import com.casaleff.addition.model.PaidItem;
import com.casaleff.addition.model.Payment;
import com.casaleff.addition.repository.PaidItemRepository;
import com.casaleff.addition.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaidItemRepository paidItemRepository;

    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new BaseException(ErrorType.ENTITY_NOT_FOUND, "Payment not found"));
    }

    public Payment createPayment(MyTable table){
        log.info("Creating Payment for table {}", table);

        Payment payment = new Payment();

        payment.setDate(LocalDate.now());
        payment.setTableNumber(table.getNumber());

        paymentRepository.save(payment);

        List<MenuItem> items = table.getPaidItems();

        for(MenuItem item : items){
            PaidItem paidItem = new PaidItem();

            paidItem.setPayment(payment);
            paidItem.setName(item.getName());
            paidItem.setPrice(item.getPrice());
            paidItem.setMenuId(item.getId());

            paidItemRepository.save(paidItem);
        }

        return payment;
    }
}
