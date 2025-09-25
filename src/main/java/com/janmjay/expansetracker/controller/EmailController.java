package com.janmjay.expansetracker.controller;

import com.janmjay.expansetracker.entity.ProfileEntity;
import com.janmjay.expansetracker.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;
    private final ExcelService excelService;

    @GetMapping("/income-excel")
    public ResponseEntity<Void> EmailIncomeExcel() throws IOException {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(baos, incomeService.getCurrentMonthIncomeForCurrentUser());
        emailService.sendExcelAttachment(profile.getEmail(),
                "Your Income Excel Report",
                "Please find attached income report",
                baos.toByteArray(),
                "incomes.xlsx");
        return ResponseEntity.ok().build();
    }


    @GetMapping("/expense-excel")
    public ResponseEntity<Void> EmailExpenseExcel() throws IOException , MessagingException {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(baos, expenseService.getCurrentMonthExpenseForCurrentUser());
        emailService.sendExcelAttachment(profile.getEmail(),
                "Your Income Excel Report",
                "Please find attached income report",
                baos.toByteArray(),
                "incomes.xlsx");
        return ResponseEntity.ok().build();
    }
}
