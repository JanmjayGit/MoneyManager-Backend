package com.janmjay.expansetracker.controller;

import com.janmjay.expansetracker.dto.ExpenseDTO;
import com.janmjay.expansetracker.dto.IncomeDTO;
import com.janmjay.expansetracker.service.EmailService;
import com.janmjay.expansetracker.service.IncomeService;
import com.janmjay.expansetracker.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final ProfileService profileService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO dto){
        IncomeDTO saved = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncomes(){
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomeForCurrentUser();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable("id") Long id){
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/excel/download/income")
    public ResponseEntity<byte[]> downloadIncomesExcel() {
        byte[] excelData = incomeService.exportIncomesToExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=incomes.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
    }

    @GetMapping("/excel/email/income")
    public ResponseEntity<String> emailIncomesExcel() {
        byte[] excelData = incomeService.exportIncomesToExcel();
        // Get current user's email (adjust as per your profile/auth logic)
        String to = profileService.getCurrentProfile().getEmail();
        emailService.sendExcelAttachment(
                to,
                "Your Income Excel Sheet",
                "Please find attached your income Excel report.",
                excelData,
                "incomes.xlsx"
        );
        return ResponseEntity.ok("Income Excel sent to your email!");
    }

}

