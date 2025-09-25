package com.janmjay.expansetracker.controller;

import com.janmjay.expansetracker.service.ExcelService;
import com.janmjay.expansetracker.service.ExpenseService;
import com.janmjay.expansetracker.service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private  final ExpenseService expenseService;

    @GetMapping("/download/income")
    private void downloadIncomesExcel(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=incomes.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        excelService.writeIncomesToExcel(response.getOutputStream(), incomeService.getCurrentMonthIncomeForCurrentUser());
    }

    @GetMapping("/download/expense")
    private void downloadExpensesExcel(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=expenses.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        excelService.writeExpensesToExcel(response.getOutputStream(), expenseService.getCurrentMonthExpenseForCurrentUser());
    }
}
