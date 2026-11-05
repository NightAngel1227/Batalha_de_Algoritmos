package com.example.algoritmos;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.example.interfaces.SortingAlgorithm;

public class QuickSort implements SortingAlgorithm {

 private long comparisons;
 private long swaps;
 private long executionTime;  // em milissegundos

 @Override
 public void sort(int[] array) {
  if (array == null) {
   throw new IllegalArgumentException("O vetor não pode ser nulo!");
  }

  comparisons = 0;
  swaps = 0;

  long inicioTempo = System.nanoTime();

  int n = array.length;

  if (n > 1) {
   // gerador de números aleatórios para escolher o pivô (semente fixa: resultados repetíveis)
   Random aleatorio = new Random(42);

   // pilha de trechos ainda não ordenados: guarda pares (inicio, fim)
   // 128 posições = 64 trechos, mais que suficiente (ver explicação abaixo)
   int[] pilha = new int[128];
   int topo = 0;

   pilha[topo++] = 0;       // inicio do vetor inteiro
   pilha[topo++] = n - 1;   // fim do vetor inteiro

   while (topo > 0) {
    // tira um trecho da pilha
    int fim = pilha[--topo];
    int inicio = pilha[--topo];

    // ---- particiona o trecho [inicio..fim] ----
    int posicaoPivo = inicio + aleatorio.nextInt(fim - inicio + 1); // índice sorteado em [inicio..fim]
    int pivo = array[posicaoPivo];  // valor do pivô
    int i = inicio;                 // anda para a direita
    int j = fim;                    // anda para a esquerda

    while (i <= j) {
     // procura, da esquerda, um elemento que NÃO seja menor que o pivô
     comparisons++;
     while (array[i] < pivo) {
      i++;
      comparisons++;
     }

     // procura, da direita, um elemento que NÃO seja maior que o pivô
     comparisons++;
     while (array[j] > pivo) {
      j--;
      comparisons++;
     }

     // os dois estão do lado errado: troca
     if (i <= j) {
      if (i < j) {
       int aux = array[i];
       array[i] = array[j];
       array[j] = aux;
       swaps++;
      }
      i++;
      j--;
     }
    }
    // agora: [inicio..j] tem valores <= pivô e [i..fim] tem valores >= pivô

    int tamanhoEsq = j - inicio + 1;
    int tamanhoDir = fim - i + 1;

    // empilha o trecho MAIOR primeiro e o MENOR depois,
    // assim o menor é o próximo a sair (mantém a pilha pequena).
    // Trechos com menos de 2 elementos já estão ordenados e não entram.
    if (tamanhoEsq > tamanhoDir) {
     if (tamanhoEsq >= 2) {
      pilha[topo++] = inicio;
      pilha[topo++] = j;
     }
     if (tamanhoDir >= 2) {
      pilha[topo++] = i;
      pilha[topo++] = fim;
     }
    } else {
     if (tamanhoDir >= 2) {
      pilha[topo++] = i;
      pilha[topo++] = fim;
     }
     if (tamanhoEsq >= 2) {
      pilha[topo++] = inicio;
      pilha[topo++] = j;
     }
    }
   }
  }

  executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicioTempo);
 }

 @Override
 public long getComparisons() { return comparisons; }

 @Override
 public long getSwaps() { return swaps; }

 @Override
 public long getExecutionTime() { return executionTime; }

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