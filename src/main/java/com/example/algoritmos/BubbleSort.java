package com.example.algoritmos;
import java.util.concurrent.TimeUnit;
import com.example.interfaces.SortingAlgorithm;

public class BubbleSort implements SortingAlgorithm{
    private long comparisons;
    private long swaps;
    private long executionTime;

    @Override
    public void sort(int[] array){
        if (array == null){
            throw new IllegalArgumentException("O vetor não pode ser nulo!");
        }

        //zera as métricas a cada execução:
        comparisons = 0;
        swaps = 0;

        //marca o tempo de início:
        long start = System.nanoTime();

        int n = array.length;
        for (int i = 0; i < n - 1; i++){
            boolean swapped = false;

            //a cada passada, os últimos elementos i estão no lugar
            for (int j = 0; j < n - 1 - i; j++){
                comparisons++;
                if (array[j] > array[j + 1]){
                    int aux = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = aux;
                    swaps++;
                    swapped = true;
                }
            }

            //se o vetor já estiver ordenado:
            if(!swapped){
                break;
            }
        }

        executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
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
        if (array == null){
            return false;
        }

        for (int i = 0; i < array.length - 1; i++){
            if (array[i] > array[i + 1]){
                return false;
            }
        }

        return true;
    }
}
