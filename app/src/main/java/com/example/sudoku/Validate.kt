package com.example.sudoku


fun check() : Boolean {

   val usedRows = Array(9) { IntArray(10) }
   val usedCols = Array(9) { IntArray(10) }
   val usedSubmatrix = Array(9) { IntArray(10) }

   for(i in 0..8) {
      for(j in 0..8) {
         if(matrix[i][j] != 0 ) {
            val num = matrix[i][j]
            val k = (i/3)*3 + j/3
            if(usedRows[i][num] == 1 || usedCols[j][num] ==1 || usedSubmatrix[k][num] == 1) {
               return false
            }
            usedRows[i][num] = 1
            usedCols[j][num] = 1
            usedSubmatrix[k][num] = 1
         }
      }
   }
   return true
}



fun getSolution() : Boolean {
   return solve(0,0)
}

// utility function for getSolution
private fun solve(r : Int , c : Int) : Boolean {
   if(r == 9)
      return true
   var r1 : Int = r
   var c1 : Int = c
   if(c == 8) {
      r1++
      c1 = 0
   } else {
      c1++
   }

   if(original[r][c] != 0)
      return solve(r1,c1)
   else {
      for(i in 1..9) {
         if(isValid(r,c,i)) {
            original[r][c] = i
            if(solve(r1,c1))
               return true
            original[r][c] = 0
         }
      }
   }
   return false
}

// utility function for getSolution
private fun isValid(r : Int,c : Int,value : Int) : Boolean {

   //checking row and column
   for(i in 0..8) {
      if(value == original[i][c] || value == original[r][i])
         return false
   }

   val r1 = (r/3)*3
   val c1 = (c/3)*3

   for(i in 0..2) {
      for(j in 0..2) {
         if(value == original[r1+i][c1+j])
            return false
      }
   }
   return true
}