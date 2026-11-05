package com.example;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import com.example.algoritmos.BubbleSort;
import com.example.algoritmos.InsertionSort;
import com.example.algoritmos.MergeSort;
import com.example.algoritmos.QuickSort;
import com.example.algoritmos.SelectionSort;
import com.example.interfaces.SortingAlgorithm;

/* Programa principal do projeto: roda os 5 algoritmos de ordenação com as mesmas entradas
e grava os resultados em um arquivo CSV. */
public class Main {

    // tamanhos e tipos de dados
    static int[] tamanhos = {100, 1000, 5000, 10000, 50000, 100000};
    static String[] tipos = {"aleatorio", "ordenado", "invertido"};
    static String[] nomes = {"BubbleSort", "InsertionSort", "SelectionSort", "MergeSort", "QuickSort"};

    // os 3 primeiros (posições 0, 1 e 2) são O(n²) e não rodam acima desse tamanho
    static final int LIMITE_QUADRATICOS = 50000;

    // 5 execuções por tamanho, a primeira é descartada
    static final int EXECUCOES = 5;

    // usado para medir a memória (os bytes alocados durante o sort)
    static com.sun.management.ThreadMXBean medidor = null;

    public static void main(String[] args) throws Exception {

        // tenta pegar o medidor de memória
        java.lang.management.ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (bean instanceof com.sun.management.ThreadMXBean) {
            medidor = (com.sun.management.ThreadMXBean) bean;
        }

        PrintWriter csv = new PrintWriter("resultados.csv");
        csv.println("algoritmo,entrada,n,comparacoes,trocas,tempo_ms,tempo_ns,memoria_bytes,ordenado");

        System.out.printf(Locale.US, "%-14s %-10s %7s %16s %14s %10s %14s %12s %s%n",
                "algoritmo", "entrada", "n", "comparacoes", "trocas", "tempo_ms", "tempo_ns", "memoria(B)", "ok");

        int falhas = 0;

        for (int t = 0; t < tamanhos.length; t++) {
            int n = tamanhos[t];

            for (int k = 0; k < tipos.length; k++) {
                String tipo = tipos[k];

                // somas para calcular a média depois (uma posição para cada algoritmo)
                double[] somaComparacoes = new double[nomes.length];
                double[] somaTrocas = new double[nomes.length];
                double[] somaMs = new double[nomes.length];
                double[] somaNs = new double[nomes.length];
                double[] somaMemoria = new double[nomes.length];
                boolean[] ordenou = new boolean[nomes.length];
                Arrays.fill(ordenou, true);

                for (int execucao = 1; execucao <= EXECUCOES; execucao++) {

                    // todos os algoritmos recebem a mesma entrada
                    int[] original = gerarVetor(tipo, n, execucao);

                    // vetor de referência, ordenado pelo Arrays.sort
                    int[] esperado = original.clone();
                    Arrays.sort(esperado);

                    for (int a = 0; a < nomes.length; a++) {

                        if (a < 3 && n > LIMITE_QUADRATICOS) {
                            continue; 
                            // O(n²) não roda em n muito grande
                        }

                        int[] copia = original.clone();
                        SortingAlgorithm alg = criarAlgoritmo(a);

                        long memoriaAntes = bytesAlocados();
                        long inicio = System.nanoTime();
                        alg.sort(copia);
                        long tempoNs = System.nanoTime() - inicio;
                        long memoriaDepois = bytesAlocados();

                        // confere com o isSorted e com o Arrays.sort
                        boolean ok = alg.isSorted(copia) && Arrays.equals(copia, esperado);
                        if (!ok) {
                            ordenou[a] = false;
                            falhas++;
                        }

                        // a execução 1 é o warm-up, então não entra na média
                        if (execucao > 1) {
                            somaComparacoes[a] += alg.getComparisons();
                            somaTrocas[a] += alg.getSwaps();
                            somaMs[a] += alg.getExecutionTime();
                            somaNs[a] += tempoNs;
                            if (medidor != null) {
                                somaMemoria[a] += memoriaDepois - memoriaAntes;
                            } else {
                                somaMemoria[a] += -1;
                            }
                        }
                    }
                }

                // calcula as médias de 4 execuções
                int usadas = EXECUCOES - 1;
                for (int a = 0; a < nomes.length; a++) {
                    if (a < 3 && n > LIMITE_QUADRATICOS) {
                        continue;
                    }

                    double comparacoes = somaComparacoes[a] / usadas;
                    double trocas = somaTrocas[a] / usadas;
                    double ms = somaMs[a] / usadas;
                    double ns = somaNs[a] / usadas;
                    double memoria = somaMemoria[a] / usadas;

                    
                    csv.println(nomes[a] + "," + tipo + "," + n + ","
                            + String.format(Locale.US, "%.2f", comparacoes) + ","
                            + String.format(Locale.US, "%.2f", trocas) + ","
                            + String.format(Locale.US, "%.2f", ms) + ","
                            + String.format(Locale.US, "%.2f", ns) + ","
                            + String.format(Locale.US, "%.2f", memoria) + ","
                            + ordenou[a]);

                    System.out.printf(Locale.US, "%-14s %-10s %7d %16.1f %14.1f %10.2f %14.0f %12.0f %s%n",
                            nomes[a], tipo, n, comparacoes, trocas, ms, ns, memoria,
                            ordenou[a] ? "ok" : "FALHOU");
                }
            }
        }

        csv.close();

        System.out.println();
        if (falhas == 0) {
            System.out.println("Todos os vetores ficaram ordenados (conferido com Arrays.sort).");
        } else {
            System.out.println("ATENÇÃO: " + falhas + " execução(ões) com vetor incorreto!");
        }
        System.out.println("Resultados gravados em resultados.csv");
    }

    // cria o algoritmo da posição indicada (uma instância nova a cada execução, para zerar os contadores)
    static SortingAlgorithm criarAlgoritmo(int posicao) {
        if (posicao == 0) {
            return new BubbleSort();
        }
        if (posicao == 1) {
            return new InsertionSort();
        }
        if (posicao == 2) {
            return new SelectionSort();
        }
        if (posicao == 3) {
            return new MergeSort();
        }
        return new QuickSort();
    }

    // gera o vetor de entrada: aleatório (muda a cada execução), ordenado ou invertido
    static int[] gerarVetor(String tipo, int n, int execucao) {
        int[] v = new int[n];

        if (tipo.equals("aleatorio")) {
            Random random = new Random(2026 + n * 1000L + execucao);
            for (int i = 0; i < n; i++) {
                v[i] = random.nextInt();
            }
        } else if (tipo.equals("ordenado")) {
            for (int i = 0; i < n; i++) {
                v[i] = i;
            }
        } else {
            for (int i = 0; i < n; i++) {
                v[i] = n - i;
            }
        }

        return v;
    }

    /* total de bytes que a thread atual já alocou; a diferença antes e depois do sort
    mostra quanta memória extra o algoritmo usou */
    static long bytesAlocados() {
        if (medidor == null) {
            return -1;
        }
        return medidor.getThreadAllocatedBytes(Thread.currentThread().threadId());
    }
}
