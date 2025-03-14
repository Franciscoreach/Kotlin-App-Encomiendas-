package org.example.project.presentacion

import moe.tlaster.precompose.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double= 0.0
)

class ExpensesViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState = _uiState.asStateFlow()
    private val allExpense = repo.getAllExpenses()

    init{
        getAllExpenses()
    }

    private fun getAllExpenses(){
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(expenses = allExpense, total = allExpense.sumOf { it.amount })
            }
        }
    }

    fun addExpense(expense: Expense){
        viewModelScope.launch {
            repo.addExpense(expense)
            updateState()
        }
    }

    fun editExpense(expense: Expense){
        viewModelScope.launch {
            repo.editExpense(expense)
            updateState()
        }
    }

    fun deleteExpense(expense: Expense){
        viewModelScope.launch {
            repo.deleteExpense(expense)
            updateState()
        }
    }

    private fun updateState(){
        _uiState.update { state ->
            state.copy(expenses = allExpense, total = allExpense.sumOf { it.amount })
        }
    }

    fun getExpenseWithID(id: Long): Expense{
        return allExpense.first{ it.id == id }
    }

    fun getCategories(): List<ExpenseCategory>{
        return repo.getCategories()
    }


}