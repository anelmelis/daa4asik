# Assignment 4 — Text Analysis: Dynamic Programming and String Algorithms

ANEL MELIS SE-2527

## 1. Project Overview

This project implements and evaluates six algorithms for text analysis using Dynamic Programming, String Matching, and Advanced String Algorithms.

The main objectives are:
- Implement the required algorithms independently in Java.
- Understand and analyze their time and space complexity.
- Test their correctness using different input cases.
- Compare their performance through experimental evaluation.
- Present the results using tables and graphical visualizations.

### Implemented Algorithms

**Part A — Dynamic Programming**
1. Longest Common Subsequence (LCS)
2. Edit Distance (Levenshtein Distance)

**Part B — String Matching**
3. Naive String Matching
4. Knuth–Morris–Pratt (KMP)
5. Rabin–Karp

**Part C — Advanced String Algorithm**
6. Kasai's Algorithm (LCP Array Construction)

## 2. Project Structure

```text
Assignment4/
│
├── src/
│   ├── algorithms/
│   │   ├── dp/
│   │   │   ├── LCS.java
│   │   │   └── EditDistance.java
│   │   │
│   │   ├── sm/
│   │   │   ├── NaiveStringMatching.java
│   │   │   ├── KMP.java
│   │   │   └── RabinKarp.java
│   │   │
│   │   └── aca/
│   │       └── Kasai.java
│   │
│   ├── tests/
│   │   └── AlgorithmTests.java
│   │
│   └── experiments/
│       └── AlgorithmExperiments.java
│
├── results/
│   ├── benchmark_results.csv
│   ├── dp_comparison.png
│   ├── string_matching_present.png
│   └── string_matching_absent.png
│
├── .gitignore
└── README.md
```

## 3. Algorithm Implementations

### 3.1. Longest Common Subsequence (LCS)

The LCS algorithm finds the length of the longest subsequence shared by two strings. Characters must appear in the same relative order, but they do not need to be consecutive.

The implementation uses a two-dimensional Dynamic Programming table.

**Recurrence relation:**

If the current characters match:

`dp[i][j] = dp[i-1][j-1] + 1`

Otherwise:

`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**Base cases:**

`dp[i][0] = 0`

`dp[0][j] = 0`

The final answer is stored in `dp[m][n]`.

**Example:**

- String 1: ABCDE
- String 2: ACE
- LCS length: 3

The implementation returns the length of the subsequence rather than reconstructing the actual subsequence.

### 3.2. Edit Distance (Levenshtein Distance)

Edit Distance calculates the minimum number of operations required to transform one string into another.

The algorithm supports three operations:

- Insertion — adding a character.
- Deletion — removing a character.
- Substitution — replacing a character.

**Recurrence relation:**

If the current characters are equal:

`dp[i][j] = dp[i-1][j-1]`

Otherwise:

`dp[i][j] = 1 + min(insertion, deletion, substitution)`

The DP table stores the minimum transformation cost for every pair of prefixes.

**Example:**

- String 1: CAT
- String 2: CUT
- Edit Distance: 1

Only one substitution is required.

### 3.3. Naive String Matching

Naive String Matching is a basic pattern-searching algorithm.

It checks the pattern at every possible starting position in the text.

The algorithm compares characters sequentially. If a mismatch occurs, it shifts the pattern by one position and starts checking again.

The implementation returns all starting indices where the pattern occurs.

**Example:**

- Text: ABABCABABC
- Pattern: ABC
- Result: [2, 7]

This algorithm serves as a baseline for comparing the efficiency of KMP and Rabin–Karp.

### 3.4. Knuth–Morris–Pratt (KMP)

KMP improves pattern searching by avoiding unnecessary repeated character comparisons.

It uses an auxiliary array called LPS (Longest Prefix Suffix).

The LPS table stores the length of the longest proper prefix that is also a suffix for each pattern prefix.

Before searching, KMP preprocesses the pattern and constructs the LPS table.

When a mismatch occurs, the algorithm uses the LPS values to determine how far the pattern can shift without repeating previously completed comparisons.

The implementation separately counts:
- Preprocessing character comparisons.
- Search character comparisons.

**Example:**

Pattern: ABABC

LPS: [0, 0, 1, 2, 0]

### 3.5. Rabin–Karp

Rabin–Karp uses hashing to search for a pattern inside a text.

Instead of comparing every character immediately, it calculates hash values for the pattern and the current text window.

The algorithm uses a rolling hash to efficiently update the hash when the window moves one position forward.

If the hash values match, the algorithm verifies the actual characters to avoid false matches caused by hash collisions.

The implementation uses:
- Base: 256
- Modulus: 1,000,000,007

The implementation tracks two types of operations:
- Hash comparisons.
- Character comparisons during collision verification.

This distinction helps evaluate how hashing reduces direct character comparisons.

### 3.6. Kasai's Algorithm

Kasai's algorithm constructs the Longest Common Prefix (LCP) array using a given string and its suffix array.

A suffix is a substring that starts at a particular position and continues until the end of the original string.

The suffix array contains the starting indices of all suffixes sorted lexicographically.

The LCP array stores the length of the common prefix between each suffix and its previous suffix in sorted order.

The implementation follows these steps:

1. Construct the inverse suffix array (rank array).
2. Iterate through the original string.
3. Compare neighboring suffixes in sorted order.
4. Store the common prefix length.
5. Reuse previous comparisons by decreasing the matching length by one.

The suffix array is provided as input. Its construction is not part of the Kasai implementation.

**Example:**

Text: banana

Suffix Array: [5, 3, 1, 0, 4, 2]

LCP Array: [0, 1, 3, 0, 0, 2]

## 4. Complexity Analysis

The following table summarizes the theoretical complexity of all implemented algorithms.

| Algorithm | Best-case Time | Worst-case Time | Auxiliary Space |
|---|---|---|---|
| LCS | Θ(m × n) | Θ(m × n) | O(m × n) |
| Edit Distance | Θ(m × n) | Θ(m × n) | O(m × n) |
| Naive String Matching | O(n − m + 1) | O((n − m + 1) × m) | O(1) |
| KMP | O(n + m) | O(n + m) | O(m) |
| Rabin–Karp | O(n + m) expected | O(n × m) | O(1) |
| Kasai | Θ(n) | Θ(n) | O(n) |

*The auxiliary space estimates exclude the space required to store returned result lists.*

### Complexity Justification

**LCS:** The algorithm fills an m × n DP table. Every cell is computed once, resulting in Θ(m × n) time and O(m × n) space.

**Edit Distance:** Similar to LCS, the algorithm calculates every cell of a two-dimensional DP table. Each cell requires a constant number of operations. Therefore, its time complexity is Θ(m × n).

**Naive String Matching:** The algorithm examines up to n − m + 1 possible starting positions. In the worst case, it compares all m pattern characters at each position.

**KMP:** The LPS preprocessing takes O(m) time. The searching phase takes O(n) time because the text index never moves backwards. The total complexity is O(n + m).

**Rabin–Karp:** Hash preprocessing and rolling hash updates take O(n + m) expected time. In the worst case, hash collisions may require repeated character verification, resulting in O(n × m) time.

**Kasai:** The rank array is constructed in O(n) time. The algorithm reuses previously computed common-prefix information, ensuring that the total number of character comparisons remains linear.

## 5. Testing Strategy and Results

Correctness was evaluated using different input cases, including edge cases.

### LCS Tests

- Identical strings.
- Completely different strings.
- An empty string.
- A common subsequence.

### Edit Distance Tests

- Identical strings.
- One empty string.
- Substitution.
- Insertion.
- Deletion.

### String Matching Tests

- Pattern at the beginning.
- Pattern in the middle.
- Pattern at the end.
- Multiple occurrences.
- Pattern absent from the text.
- Pattern longer than the text.
- Empty pattern.

For every string-matching test, Naive, KMP, and Rabin–Karp were checked against the same expected list of occurrence indices.

### Kasai Test

A manually verified suffix array and expected LCP array were used to validate the implementation.

### Testing Results

| Category | Number of Checks |
|---|---:|
| LCS | 4 |
| Edit Distance | 5 |
| String Matching | 35 |
| Kasai | 1 |
| **Total** | **45** |

**Final test result:**

- Passed: 45
- Failed: 0
- Total: 45

All implemented algorithms passed the conducted correctness tests.

### Implementation Assumptions

- String matching is case-sensitive.
- An empty pattern is considered to occur at every position from 0 to the text length.
- If the pattern is longer than the text, the result is an empty list.
- The LCS implementation returns the length of the subsequence.
- Kasai receives an already constructed suffix array.

## 6. Experimental Setup

The experimental evaluation compares the execution performance of the implemented algorithms.

### 6.1. Environment

- Programming language: Java
- JDK: Java 25
- IDE: IntelliJ IDEA
- Timing method: `System.nanoTime()`

### 6.2. Input Sizes

The experiments use the fixed input sizes required by the assignment.

| Algorithm | Input Sizes |
|---|---|
| LCS | 100, 500, 1,000 |
| Edit Distance | 100, 500, 1,000 |
| String Matching | 10,000, 50,000, 100,000 |
| Kasai | 1,000, 5,000, 10,000 |

### 6.3. Experimental Methodology

The experiments follow these procedures:

1. Random strings are generated using a fixed seed of 20261031.
2. The alphabet consists of A, B, C, and D.
3. Pattern length is set to 10 for string-matching experiments.
4. Each experiment includes three JVM warm-up runs.
5. Each measured experiment is executed five times.
6. Average execution time is calculated in milliseconds.
7. Input generation and console output are excluded from timed regions.
8. The suffix array is constructed before measuring Kasai's execution time.
9. The same text and pattern are used when comparing Naive, KMP, and Rabin–Karp.

String-matching experiments include two scenarios:

- Present: the pattern occurs in the text.
- Absent: the pattern does not occur in the text.

Operation counts are collected in a separate pass after timing so that the additional counting pass does not affect the measured execution time.

### 6.4. Operation Counts

The following operations are recorded:

| Algorithm | Recorded Operations |
|---|---|
| Naive | Character comparisons |
| KMP | LPS preprocessing comparisons and search comparisons |
| Rabin–Karp | Hash comparisons and character verification comparisons |

These measurements provide additional information about the algorithms beyond execution time.



## 7. Experimental Results

This section presents the measured execution times and operation counts for all implemented algorithms. The results were obtained using the experimental methodology described in Section 6.

### 7.1. Dynamic Programming Results

The following table compares the average execution time of LCS and Edit Distance for three input sizes.

| Input Size | LCS (ms) | Edit Distance (ms) |
|---:|---:|---:|
| 100 | 0.676780 | 0.806820 |
| 500 | 1.042840 | 1.650180 |
| 1,000 | 5.688060 | 4.751360 |

**Graph:**

![Dynamic Programming Comparison](results/dp_comparison.png)

The results show that execution time generally increases with input size for both algorithms. At an input size of 1,000, Edit Distance executes faster than LCS in this experiment. Both algorithms have quadratic time complexity.

### 7.2. String Matching Results

The following tables compare Naive, KMP, and Rabin–Karp using identical text and pattern inputs for each experiment.

**Pattern Present**

| Text Length | Algorithm | Average Time (ms) | Preprocessing Comparisons | Hash Comparisons | Character Comparisons |
|---:|---|---:|---:|---:|---:|
| 10,000 | Naive | 0.565060 | 0 | 0 | 13,331 |
| 10,000 | KMP | 0.569320 | 12 | 0 | 12,279 |
| 10,000 | Rabin–Karp | 0.952000 | 0 | 9,991 | 10 |
| 50,000 | Naive | 0.446940 | 0 | 0 | 66,829 |
| 50,000 | KMP | 0.411600 | 13 | 0 | 62,566 |
| 50,000 | Rabin–Karp | 0.674800 | 0 | 49,991 | 10 |
| 100,000 | Naive | 0.416180 | 0 | 0 | 133,760 |
| 100,000 | KMP | 0.432760 | 13 | 0 | 125,288 |
| 100,000 | Rabin–Karp | 0.852520 | 0 | 99,991 | 10 |

**Graph:**

![String Matching - Pattern Present](results/string_matching_present.png)

**Pattern Absent**

| Text Length | Algorithm | Average Time (ms) | Preprocessing Comparisons | Hash Comparisons | Character Comparisons |
|---:|---|---:|---:|---:|---:|
| 10,000 | Naive | 0.235820 | 0 | 0 | 13,427 |
| 10,000 | KMP | 0.213700 | 11 | 0 | 12,567 |
| 10,000 | Rabin–Karp | 0.157080 | 0 | 9,991 | 0 |
| 50,000 | Naive | 0.212480 | 0 | 0 | 66,710 |
| 50,000 | KMP | 0.208280 | 9 | 0 | 62,529 |
| 50,000 | Rabin–Karp | 0.442220 | 0 | 49,991 | 0 |
| 100,000 | Naive | 0.392080 | 0 | 0 | 132,825 |
| 100,000 | KMP | 0.411780 | 11 | 0 | 124,321 |
| 100,000 | Rabin–Karp | 0.844360 | 0 | 99,991 | 0 |

**Graph:**

![String Matching - Pattern Absent](results/string_matching_absent.png)

The operation counts demonstrate differences between the three algorithms.

Naive String Matching performs a large number of direct character comparisons. KMP reduces repeated comparisons by using its LPS preprocessing table. Rabin–Karp performs hash comparisons for each text window and only verifies characters when the hash values match.

When the pattern is absent, Rabin–Karp performs zero direct character verifications in these experiments because none of the window hashes matched the pattern hash.

### 7.3. Kasai's Algorithm Results

The following table presents the execution time of Kasai's algorithm for different string lengths.

| Input Size | Average Execution Time (ms) |
|---:|---:|
| 1,000 | 0.173140 |
| 5,000 | 0.363080 |
| 10,000 | 0.661540 |

The suffix array was constructed before the timing measurements. Therefore, the reported execution time represents Kasai's LCP construction rather than suffix array construction.

The results show an increase in execution time as the input size grows, which is consistent with the linear time complexity of Kasai's algorithm.

### 7.4. Summary of Experimental Findings

The experiments demonstrate that theoretical complexity and measured execution time are related but not identical.

- LCS and Edit Distance show increasing execution times as input sizes grow.
- KMP reduces direct character comparisons by using the LPS table.
- Naive String Matching provides a useful baseline but performs more direct comparisons.
- Rabin–Karp reduces direct character verification through hashing, although it still processes every text window.
- Kasai demonstrates increasing execution time with larger input strings, consistent with its linear complexity.

The measured times should be interpreted with consideration of JVM behavior, warm-up, memory allocation, and measurement variability.

## 8. Discussion

The theoretical complexity analysis and experimental evaluation provide two different perspectives on algorithm performance.

LCS and Edit Distance both use Dynamic Programming tables. Their execution time increases as the input strings become longer because the number of DP table cells grows with the product of the two string lengths.

Naive String Matching provides a simple baseline. Its performance depends on the number of possible starting positions and the number of character comparisons required at each position.

KMP improves searching by using the LPS table. It avoids repeating unnecessary comparisons and provides a linear-time searching process.

Rabin–Karp uses rolling hashes to compare text windows efficiently. Its performance depends on hash matches and the number of character verifications required. Hash comparisons and character comparisons therefore need to be interpreted separately.

Kasai's algorithm reuses previously calculated common-prefix information, allowing it to construct the LCP array in linear time when the suffix array is already available.

Measured execution times may differ from theoretical expectations because of JVM warm-up, memory allocation, processor behavior, and other implementation-related overhead.

Therefore, theoretical complexity and measured execution time should be interpreted together rather than treated as identical measures of performance.

## 9. Conclusion

This project implemented six algorithms covering Dynamic Programming, String Matching, and Advanced String Algorithms.

The implementations were evaluated through correctness testing, theoretical complexity analysis, and experimental performance measurements.

The testing process produced 45 successful checks with no failures.

The experiments evaluated the algorithms using the input sizes specified in the assignment. Execution times and operation counts were recorded for comparative analysis.

The project demonstrates the differences between Dynamic Programming approaches, direct pattern searching, prefix-based optimization, rolling hashing, and linear-time LCP construction.

Overall, the results provide an opportunity to compare algorithmic efficiency, implementation trade-offs, and the relationship between theoretical complexity and practical execution performance.



