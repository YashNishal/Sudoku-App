package com.example.sudoku


fun Check() : Boolean {

   val used_rows = Array(9) { IntArray(10) }
   val used_cols = Array(9) { IntArray(10) }
   val used_submatrix = Array(9) { IntArray(10) }

   for(i in 0..8) {
      for(j in 0..8) {
         if(matrix[i][j] != 0 ) {
            val num = matrix[i][j]
            val k = (i/3)*3 + j/3
            if(used_rows[i][num] == 1 || used_cols[j][num] ==1 || used_submatrix[k][num] == 1) {
               return false
            }
            used_rows[i][num] = 1
            used_cols[j][num] = 1
            used_submatrix[k][num] = 1
         }
      }
   }
   return true
}



fun getSolution() {

}