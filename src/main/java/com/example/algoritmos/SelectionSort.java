package com.example.algoritmos;
import com.example.interfaces.SortingAlgorithm;
import java.util.concurrent.TimeUnit;

public class SelectionSort implements SortingAlgorithm {
 private long swaps;
 private long comparisons;
 private long executionTime;

 @Override
 public void sort(int[] array) {
  if (array == null) {
   throw new IllegalArgumentException("O vetor não pode ser vazio!");
  }

  swaps = 0;
  comparisons = 0;

  long inicio = System.nanoTime();
  for (int i = 0; i < array.length - 1; i++) {
   int menorIndice = i;
   for (int j = i + 1; j < array.length; j++) {
    comparisons = comparisons + 1;
    if (array[j] < array[menorIndice]) {
     menorIndice = j;
    }
   }
   if (menorIndice != i) {
    int aux = array[i];
    array[i] = array[menorIndice];
    array[menorIndice] = aux;
    swaps = swaps + 1;
   }
  }
  executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
 }

 @Override
 public long getComparisons() {
  return comparisons;
 }

 @Override
 public long getSwaps() {
  return swaps;
 }

 @Override
 public long getExecutionTime() {
  return executionTime;
 }

 @Override
 public boolean isSorted(int[] array) {
  if (array == null)
   return false;
  for (int i = 0; i < array.length - 1; i++) {
   if (array[i] > array[i + 1])
    return false;
  }
  return true;
 }
}
