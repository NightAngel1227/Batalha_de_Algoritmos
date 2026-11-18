package com.example.interfaces;

public interface SortingAlgorithm {
    void sort ( int [] array ) ;
    long getComparisons () ;
    long getSwaps () ;
    long getExecutionTime () ;
    boolean isSorted (int [] array ) ;
}
