package com.example.algoritmos;
import java.util.concurrent.TimeUnit;
import com.example.interfaces.SortingAlgorithm;

public class MergeSort implements SortingAlgorithm {

 private long comparisons;
 private long swaps; // aqui conta movimentos
 private long executionTime; // em milissegundos

 @Override
 public void sort(int[] array) {
  if (array == null) {
   throw new IllegalArgumentException("O vetor não pode ser nulo!");
  }

  comparisons = 0;
  swaps = 0;
  long inicioTempo = System.nanoTime();

  int n = array.length;
  int[] aux = new int[n]; // vetor auxiliar

  // tamanho = tamanho de cada bloco já ordenado: 1, 2, 4, 8, etc
  for (int tamanho = 1; tamanho < n; tamanho = tamanho * 2) {
   // percorre o vetor de dois em dois blocos
   for (int inicio = 0; inicio < n - tamanho; inicio = inicio + 2 * tamanho) {
    int meio = inicio + tamanho - 1; // fim do bloco esquerdo
    int fim = Math.min(inicio + 2 * tamanho - 1, n - 1); // fim do bloco direito

    //intercala [inicio..meio] com [meio+1..fim]
    int i = inicio; // percorre o bloco esquerdo
    int j = meio + 1; // percorre o bloco direito
    int k = inicio; // posição de escrita em aux

    // enquanto os dois blocos têm elementos, pega o menor
    while (i <= meio && j <= fim) {
     comparisons++;
     if (array[i] <= array[j]) {
      aux[k] = array[i];
      i++;
     } else {
      aux[k] = array[j];
      j++;
     }
     k++;
    }

    // sobrou algo no bloco esquerdo
    while (i <= meio) {
     aux[k] = array[i];
     i++;
     k++;
    }

    // sobrou algo no bloco direito
    while (j <= fim) {
     aux[k] = array[j];
     j++;
     k++;
    }

    //devolve o trecho intercalado para o vetor original
    k = inicio;
    while (k <= fim) {
     array[k] = aux[k];
     swaps++;
     k++;
    }
   }
  }

  executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicioTempo);
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
  if (array == null) {
   return false;
  }
  for (int i = 0; i < array.length - 1; i++) {
   if (array[i] > array[i + 1]) {
    return false;
   }
  }
  return true;
 }
}