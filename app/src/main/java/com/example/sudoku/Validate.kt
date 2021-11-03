package com.example.sudoku

fun Check() : Boolean {
   return false
}

//for(int i = 0 ; i < 9 ; i++) {
//    if(i == r)
//        continue;
//    if(arr[r][c] == arr[i][c])
//        return false;
//}
//
//for(int i = 0 ; i < 9 ; i++) {
//    if(i == c)
//        continue;
//    if(arr[r][c] == arr[r][i])
//        return false;
//}
//int r1 =(r/3)*3;
//int c1 =(c/3)*3;
//for(int i = 0 ; i <  3; i++) {
//    for(int j = 0 ; j < 3 ; j++) {
//        if(i+r1 == r and j+c1 == c)
//            continue;
//        if(arr[r][c] == arr[i+r1][j+c1])
//            return false;
//    }
//}
//return true;