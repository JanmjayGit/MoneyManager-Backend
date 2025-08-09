package com.janmjay.expansetracker.service;

import com.janmjay.expansetracker.dto.ExpenseDTO;
import com.janmjay.expansetracker.dto.IncomeDTO;
import com.janmjay.expansetracker.dto.RecentTransactionDTO;
import com.janmjay.expansetracker.entity.ProfileEntity;
import com.janmjay.expansetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> data = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> transactions =  concat(latestIncomes.stream().map(income ->
                RecentTransactionDTO.builder()   // The recent transaction adding all the amount including incomes and expenses
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()),
                latestExpenses.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()))
                .sorted((a,b) -> {
                    int compare = b.getDate().compareTo(a.getDate());
                    if(compare == 0 && a.getCreatedAt()!= null && b.getCreatedAt() != null){
                        return a.getCreatedAt().compareTo(b.getCreatedAt());
                    }
                    return compare;
                }).collect(Collectors.toList());

        data.put("totalBalance",
                incomeService.getTotalIncomesForCurrentUser()
                        .subtract(expenseService.getTotalExpenseForCurrentUser()));
        data.put("totalIncomes", incomeService.getTotalIncomesForCurrentUser());
        data.put("totalExpenses", expenseService.getTotalExpenseForCurrentUser());
        data.put("recentTransactions", transactions);
        data.put("recent5Incomes", latestIncomes);
        data.put("recent5Expenses", latestExpenses);
        return data;

    }
}
