package com.example.sudoku.Data

interface SudokuAPI {
    suspend fun getSudoku(
        difficulty : String = "easy",
        
    )
}