package com.example.algoritmos;

import java.util.concurrent.TimeUnit;

import com.example.interfaces.SortingAlgorithm;

public class InsertionSort implements SortingAlgorithm{
    private long comparisons;
    private long swaps;
    private long executionTime;

    @Override
    public void sort(int[] array){
        if (array == null){
            throw new IllegalArgumentException("O vetor não pode estar vazio!");
        }

        comparisons = 0;
        swaps = 0;
        long inicio =  System.nanoTime();

        for (int i = 1; i < array.length; i++){
            int aux = array[i];
            int j = i - 1;

            while (j >= 0){
                comparisons++;
                //compara array[j] com a chave:
                if (array[j] > aux){
                    //desloca para a direita:
                    array[j + 1] = array[j];
                    swaps++;
                    j--;
                } else {
                    //se achar a posição, para de comparar:
                    break;
                }
            }
            //insere a chave na posição certa:
            array[j + 1] = aux;
        }

        executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
    }

    @Override
    public long getComparisons(){
        return comparisons;
    }

    @Override
    public long getSwaps(){
        return swaps;
    }

    @Override
    public long getExecutionTime(){
        return executionTime;
    }

    @Override
    public boolean isSorted(int[] array){
        if (array == null) return false;
        for (int i = 0; i < array.length - 1; i++){
            if (array[i] > array[i + 1]){
                return false;
            }
        }
        return true;
    }
}
