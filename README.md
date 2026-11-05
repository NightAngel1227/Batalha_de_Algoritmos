# Batalha dos Algoritmos de Ordenação

Projeto Final de **Projeto e Análise de Algoritmos**, professor Alexandre Barbosa de Souza.

Implementação em Java e comparação experimental de cinco algoritmos de ordenação: **Bubble Sort, Insertion Sort, Selection Sort, Merge Sort e Quick Sort**. Para cada algoritmo medimos comparações, trocas (ou movimentos), tempo de execução e memória alocada, em entradas aleatórias, ordenadas e invertidas, e comparamos os resultados com a análise teórica.

O relatório completo (introdução, fundamentação teórica, metodologia, resultados com gráficos log-log, discussão e conclusão) está em [`src/dados/Relatorio_Batalha_dos_Algoritmos.pdf`](src/dados/Relatorio_Batalha_dos_Algoritmos.pdf).

## Integrantes

- Carlos Eduardo Pisa Meireles
- Deivid Ramos dos Reis Silva
- Diana Carina Frederich Briere
- Eduardo Augusto Rosario Maciel
- Ruan Victor de Santana Batista

## Estrutura do repositório

```
batalha_algoritmos/
├── pom.xml                                  # projeto Maven (Java 17 ou superior)
└── src/
    ├── main/java/com/example/
    │   ├── interfaces/
    │   │   └── SortingAlgorithm.java        # interface comum
    │   ├── algoritmos/
    │   │   ├── BubbleSort.java
    │   │   ├── InsertionSort.java
    │   │   ├── SelectionSort.java
    │   │   ├── MergeSort.java               # iterativo (bottom-up), tudo dentro de sort()
    │   │   └── QuickSort.java               # pivô aleatório, pilha explícita, tudo dentro de sort()
    │   └── Main.java                        # executa os testes e grava o CSV
    └── dados/
        ├── resultados.csv                   # resultados brutos (81 linhas)
        └── Relatorio_Batalha_dos_Algoritmos.pdf
```

## A interface

Todos os algoritmos implementam `SortingAlgorithm`:

```java
public interface SortingAlgorithm {
    void sort(int[] array);
    long getComparisons();
    long getSwaps();
    long getExecutionTime();      // em milissegundos
    boolean isSorted(int[] array);
}
```

O que cada algoritmo conta como "troca":

| Algoritmo | Contagem de `getSwaps()` |
|---|---|
| Bubble | troca de elementos vizinhos |
| Insertion | deslocamento (movimento) de elemento |
| Selection | troca, apenas quando o índice do menor difere do atual |
| Merge | escrita de volta no vetor original |
| Quick | troca na partição, quando `i < j` |

## Como executar

Requisitos: **JDK 17 ou superior** (testado com JDK 21.0.12.1). O `Main` usa `ManagementFactory` e `com.sun.management.ThreadMXBean`, disponíveis em JDKs baseados no HotSpot/OpenJDK.

### Com Maven

```bash
mvn compile
java -cp target/classes com.example.projeto_algoritmos.Main
```

### Sem Maven (apenas JDK)

```bash
# Linux / macOS
javac -d out $(find src/main/java -name "*.java")
java -cp out com.example.projeto_algoritmos.Main
```

```powershell
# Windows (PowerShell)
javac -d out (Get-ChildItem -Recurse src\main\java -Filter *.java).FullName
java -cp out com.example.projeto_algoritmos.Main
```

### No IntelliJ

Abra a pasta do projeto como projeto Maven e execute a classe `Main`.

A execução completa leva alguns minutos, principalmente por causa do Bubble Sort e do Selection Sort em n = 50.000. Os resultados aparecem no console e são gravados em `resultados.csv` **na pasta em que o comando foi executado**; para atualizar os dados do repositório, copie o arquivo para `src/dados/`.

> Observação: os pacotes declarados nos arquivos (`com.example.projeto_algoritmos` para `Main` e `algoritmos`, `com.example.interfaces` para a interface) não coincidem exatamente com a estrutura de pastas. O Maven e o `javac` compilam normalmente, mas o IntelliJ pode sugerir mover as classes; é só um aviso.

### Parâmetros do experimento (em `Main.java`)

| Constante | Valor | Significado |
|---|---|---|
| `tamanhos` | 100, 1.000, 5.000, 10.000, 50.000, 100.000 | tamanhos de entrada |
| `LIMITE_QUADRATICOS` | 50.000 | maior n executado pelos algoritmos quadráticos |
| `EXECUCOES` | 5 | execuções por combinação; a primeira é descartada (aquecimento do JIT) e os resultados são a média das 4 seguintes |

Tipos de entrada: `aleatorio`, `ordenado` (crescente) e `invertido` (decrescente). Após cada ordenação, o resultado é validado com `isSorted` e comparado com `Arrays.sort`.

## Formato do CSV

`resultados.csv` tem uma linha por combinação (algoritmo × entrada × n), 81 no total:

| Coluna | Descrição |
|---|---|
| `algoritmo` | nome da classe |
| `entrada` | `aleatorio`, `ordenado` ou `invertido` |
| `n` | tamanho do vetor |
| `comparacoes` | média de comparações |
| `trocas` | média de trocas / movimentos |
| `tempo_ms` | média de `getExecutionTime()` (resolução de 1 ms) |
| `tempo_ns` | média do tempo medido em nanossegundos (usado nos gráficos) |
| `memoria_bytes` | bytes alocados durante a ordenação |
| `ordenado` | `true` se o vetor ficou ordenado |

Os valores têm casas decimais porque são médias de execuções com vetores diferentes.

## Resultados em resumo

Tempo médio em n = 50.000, entrada aleatória (ambiente abaixo):

| Algoritmo | Comparações | Tempo (ms) | Memória extra |
|---|---:|---:|---:|
| Bubble | 1.249.950.380 | 5.594 | 0 B |
| Insertion | 623.612.969 | 577 | 0 B |
| Selection | 1.249.975.000 | 2.553 | 0 B |
| Merge | 733.203 | 5,1 | 200.016 B |
| Quick | 1.079.195 | 4,3 | 552 B |

As contagens de comparações seguem as fórmulas teóricas, com inclinações log-log iguais às esperadas até a segunda casa decimal. As diferenças entre teoria e prática (previsão de desvios, JIT, resolução do relógio) estão discutidas na seção 5 do relatório.

## Ambiente de teste

| | |
|---|---|
| Equipamento | ASUS Vivobook Go 15 (E1504FA) |
| Processador | AMD Ryzen 5 7520U with Radeon Graphics, 2,80 GHz |
| Memória RAM | 16 GB (15,3 GB utilizáveis) |
| Sistema operacional | Windows 11, 64 bits |
| Java | JDK 21.0.12.1 |

Os tempos absolutos dependem da máquina; o que deve se repetir em outro computador são as contagens de comparações e as ordens de crescimento.
