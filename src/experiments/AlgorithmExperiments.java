package experiments;

import algorithms.dp.LCS;
import algorithms.dp.EditDistance;
import algorithms.sm.NaiveStringMatching;
import algorithms.sm.KMP;
import algorithms.sm.RabinKarp;
import algorithms.aca.Kasai;

import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AlgorithmExperiments {

    private static final int RUNS = 5;
    private static final int WARMUP_RUNS = 3;

    private static final long SEED = 20261031L;

    private static final String ALPHABET = "ABCD";

    private static final int PATTERN_LENGTH = 10;

    private static volatile int intSink;
    private static volatile Object objectSink;

    private static final List<String> csvRows = new ArrayList<>();

    private static final List<Result> results = new ArrayList<>();

    // Store one experiment result
    private static class Result {

        String algorithm;
        int size;
        String scenario;

        double time;

        long preprocessing;
        long hashComparisons;
        long characterComparisons;

        Result(
                String algorithm,
                int size,
                String scenario,
                double time,
                long preprocessing,
                long hashComparisons,
                long characterComparisons) {

            this.algorithm = algorithm;
            this.size = size;
            this.scenario = scenario;
            this.time = time;
            this.preprocessing = preprocessing;
            this.hashComparisons = hashComparisons;
            this.characterComparisons = characterComparisons;
        }

        long totalComparisons() {

            return preprocessing
                    + hashComparisons
                    + characterComparisons;
        }
    }

    // Generate random strings
    private static String randomString(
            int length, Random random) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {

            result.append(
                    ALPHABET.charAt(
                            random.nextInt(ALPHABET.length())
                    )
            );
        }

        return result.toString();
    }

    // Measure average execution time
    private static double averageTime(Runnable task) {

        // JVM warm-up
        for (int i = 0; i < WARMUP_RUNS; i++) {
            task.run();
        }

        long totalTime = 0;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            totalTime += end - start;
        }

        return totalTime / (double) RUNS / 1_000_000.0;
    }

    // Record experiment
    private static void record(
            String algorithm,
            int size,
            String scenario,
            double time,
            long preprocessing,
            long hashComparisons,
            long characterComparisons) {

        Result result = new Result(
                algorithm,
                size,
                scenario,
                time,
                preprocessing,
                hashComparisons,
                characterComparisons
        );

        results.add(result);

        String row = String.format(
                Locale.US,
                "%s,%d,%s,%.6f,%d,%d,%d,%d",
                algorithm,
                size,
                scenario,
                time,
                preprocessing,
                hashComparisons,
                characterComparisons,
                result.totalComparisons()
        );

        csvRows.add(row);

        System.out.println(row);
    }

    // PART A: Dynamic Programming
    private static void runDynamicProgramming(Random random) {

        System.out.println("\n=== DYNAMIC PROGRAMMING ===");

        int[] sizes = {100, 500, 1000};

        for (int size : sizes) {

            String first = randomString(size, random);
            String second = randomString(size, random);

            double lcsTime = averageTime(
                    () -> intSink = LCS.findLCS(first, second)
            );

            record(
                    "LCS", size, "N/A",
                    lcsTime, 0, 0, 0
            );

            double editTime = averageTime(
                    () -> intSink =
                            EditDistance.calculateDistance(first, second)
            );

            record(
                    "EditDistance", size, "N/A",
                    editTime, 0, 0, 0
            );
        }
    }

    // Generate pattern absent from text
    private static String generateAbsentPattern(
            String text, Random random) {

        while (true) {

            String candidate =
                    randomString(PATTERN_LENGTH, random);

            if (!text.contains(candidate)) {
                return candidate;
            }
        }
    }

    @FunctionalInterface
    interface SearchFunction {

        List<Integer> search(String text, String pattern);
    }

    // Benchmark string matching algorithm
    private static void benchmarkSearch(
            String algorithm,
            SearchFunction function,
            String text,
            String pattern,
            int size,
            String scenario) {

        double time = averageTime(
                () -> objectSink = function.search(text, pattern)
        );

        // Separate operation-counting pass.
        // This pass is NOT included in execution time.

        function.search(text, pattern);

        long preprocessing = 0;
        long hashComparisons = 0;
        long characterComparisons = 0;

        switch (algorithm) {

            case "Naive":

                characterComparisons =
                        NaiveStringMatching.getCharacterComparisons();

                break;

            case "KMP":

                preprocessing =
                        KMP.getPreprocessingComparisons();

                characterComparisons =
                        KMP.getSearchComparisons();

                break;

            case "RabinKarp":

                hashComparisons =
                        RabinKarp.getHashComparisons();

                characterComparisons =
                        RabinKarp.getCharacterComparisons();

                break;
        }

        record(
                algorithm,
                size,
                scenario,
                time,
                preprocessing,
                hashComparisons,
                characterComparisons
        );
    }

    // PART B: String Matching
    private static void runStringMatching(Random random) {

        System.out.println("\n=== STRING MATCHING ===");

        int[] sizes = {10000, 50000, 100000};

        for (int size : sizes) {

            String text = randomString(size, random);

            // Pattern present in text
            int position = random.nextInt(
                    size - PATTERN_LENGTH + 1
            );

            String presentPattern = text.substring(
                    position,
                    position + PATTERN_LENGTH
            );

            // Pattern absent from text
            String absentPattern =
                    generateAbsentPattern(text, random);

            System.out.println("\nText length: " + size);

            // PRESENT
            benchmarkSearch(
                    "Naive",
                    NaiveStringMatching::search,
                    text, presentPattern, size, "Present"
            );

            benchmarkSearch(
                    "KMP",
                    KMP::search,
                    text, presentPattern, size, "Present"
            );

            benchmarkSearch(
                    "RabinKarp",
                    RabinKarp::search,
                    text, presentPattern, size, "Present"
            );

            // ABSENT
            benchmarkSearch(
                    "Naive",
                    NaiveStringMatching::search,
                    text, absentPattern, size, "Absent"
            );

            benchmarkSearch(
                    "KMP",
                    KMP::search,
                    text, absentPattern, size, "Absent"
            );

            benchmarkSearch(
                    "RabinKarp",
                    RabinKarp::search,
                    text, absentPattern, size, "Absent"
            );
        }
    }

    // Build suffix array for Kasai input
    // This construction is outside the timed region
    private static int[] buildSuffixArray(String text) {

        int n = text.length();

        Integer[] indices = new Integer[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (a, b) -> {

            int i = a;
            int j = b;

            while (i < n && j < n) {

                int comparison = Character.compare(
                        text.charAt(i),
                        text.charAt(j)
                );

                if (comparison != 0) {
                    return comparison;
                }

                i++;
                j++;
            }

            return Integer.compare(n - i, n - j);
        });

        int[] suffixArray = new int[n];

        for (int i = 0; i < n; i++) {
            suffixArray[i] = indices[i];
        }

        return suffixArray;
    }

    // PART C: Kasai
    private static void runKasai(Random random) {

        System.out.println("\n=== KASAI ALGORITHM ===");

        int[] sizes = {1000, 5000, 10000};

        for (int size : sizes) {

            String text = randomString(size, random);

            int[] suffixArray = buildSuffixArray(text);

            double time = averageTime(
                    () -> objectSink =
                            Kasai.buildLCP(text, suffixArray)
            );

            record(
                    "Kasai", size, "N/A",
                    time, 0, 0, 0
            );
        }
    }

    // Find measured execution time for chart
    private static double findTime(
            String algorithm,
            int size,
            String scenario) {

        for (Result result : results) {

            if (result.algorithm.equals(algorithm)
                    && result.size == size
                    && result.scenario.equals(scenario)) {

                return result.time;
            }
        }

        throw new IllegalStateException(
                "Missing chart data: " + algorithm
        );
    }

    // Create PNG line chart
    private static void createLineChart(
            String title,
            String xAxisLabel,
            String yAxisLabel,
            int[] xValues,
            String[] seriesNames,
            double[][] values,
            Path outputPath) throws IOException {

        int width = 1100;
        int height = 700;

        BufferedImage image = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int left = 120;
        int right = width - 60;
        int top = 130;
        int bottom = height - 115;

        // Calculate maximum Y value
        double maxY = 0;

        for (double[] series : values) {
            for (double value : series) {
                maxY = Math.max(maxY, value);
            }
        }

        maxY = maxY * 1.2;

        if (maxY == 0) {
            maxY = 1;
        }

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 25));

        g.drawString(title, left, 45);

        // Grid and Y-axis values
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));

        for (int step = 0; step <= 5; step++) {

            int y = bottom - (bottom - top) * step / 5;

            double value = maxY * step / 5.0;

            g.setColor(new Color(220, 220, 220));
            g.drawLine(left, y, right, y);

            g.setColor(Color.DARK_GRAY);

            g.drawString(
                    String.format(Locale.US, "%.3f", value),
                    65,
                    y + 5
            );
        }

        int minX = xValues[0];
        int maxX = xValues[xValues.length - 1];

        // X-axis values
        for (int xValue : xValues) {

            int x = left + (int) (
                    (xValue - minX) * (right - left)
                            / (double) (maxX - minX)
            );

            g.setColor(new Color(220, 220, 220));
            g.drawLine(x, top, x, bottom);

            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(xValue), x - 25, bottom + 25);
        }

        // Axes
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));

        g.drawLine(left, top, left, bottom);
        g.drawLine(left, bottom, right, bottom);

        // Axis labels
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        g.drawString(
                xAxisLabel,
                (left + right) / 2 - 100,
                height - 35
        );

        g.rotate(-Math.PI / 2);

        g.drawString(
                yAxisLabel,
                -(top + bottom) / 2 - 80,
                30
        );

        g.rotate(Math.PI / 2);

        // Series colors
        Color[] colors = {
                new Color(30, 100, 200),
                new Color(220, 80, 60),
                new Color(30, 150, 90)
        };

        // Draw lines and points
        for (int s = 0; s < seriesNames.length; s++) {

            g.setColor(colors[s]);
            g.setStroke(new BasicStroke(3));

            for (int i = 0; i < xValues.length; i++) {

                int x = left + (int) (
                        (xValues[i] - minX)
                                * (right - left)
                                / (double) (maxX - minX)
                );

                int y = bottom - (int) (
                        values[s][i] / maxY * (bottom - top)
                );

                if (i > 0) {

                    int previousX = left + (int) (
                            (xValues[i - 1] - minX)
                                    * (right - left)
                                    / (double) (maxX - minX)
                    );

                    int previousY = bottom - (int) (
                            values[s][i - 1]
                                    / maxY * (bottom - top)
                    );

                    g.drawLine(previousX, previousY, x, y);
                }

                g.fillOval(x - 6, y - 6, 12, 12);
            }
        }

        // Legend
        int legendX = left + 15;
        int legendY = top - 35;

        g.setFont(new Font("SansSerif", Font.BOLD, 14));

        for (int s = 0; s < seriesNames.length; s++) {

            g.setColor(colors[s]);

            g.drawLine(
                    legendX,
                    legendY,
                    legendX + 30,
                    legendY
            );

            g.fillOval(
                    legendX + 10,
                    legendY - 5,
                    10,
                    10
            );

            g.setColor(Color.BLACK);

            g.drawString(
                    seriesNames[s],
                    legendX + 40,
                    legendY + 5
            );

            legendX += 190;
        }

        g.dispose();

        ImageIO.write(image, "png", outputPath.toFile());
    }

    // Generate all required plots
    private static void generateCharts() throws IOException {

        System.out.println("\n=== GENERATING GRAPHS ===");

        Path folder = Path.of("results");

        int[] dpSizes = {100, 500, 1000};

        double[][] dpValues = {
                {
                        findTime("LCS", 100, "N/A"),
                        findTime("LCS", 500, "N/A"),
                        findTime("LCS", 1000, "N/A")
                },
                {
                        findTime("EditDistance", 100, "N/A"),
                        findTime("EditDistance", 500, "N/A"),
                        findTime("EditDistance", 1000, "N/A")
                }
        };

        createLineChart(
                "Dynamic Programming: LCS vs Edit Distance",
                "Input size (characters)",
                "Average execution time (ms)",
                dpSizes,
                new String[]{"LCS", "Edit Distance"},
                dpValues,
                folder.resolve("dp_comparison.png")
        );

        int[] matchingSizes = {10000, 50000, 100000};

        String[] matchingAlgorithms = {
                "Naive", "KMP", "RabinKarp"
        };

        double[][] presentValues = new double[3][3];

        double[][] absentValues = new double[3][3];

        for (int a = 0; a < matchingAlgorithms.length; a++) {

            for (int i = 0; i < matchingSizes.length; i++) {

                presentValues[a][i] = findTime(
                        matchingAlgorithms[a],
                        matchingSizes[i],
                        "Present"
                );

                absentValues[a][i] = findTime(
                        matchingAlgorithms[a],
                        matchingSizes[i],
                        "Absent"
                );
            }
        }

        createLineChart(
                "String Matching: Pattern Present",
                "Text length (characters)",
                "Average execution time (ms)",
                matchingSizes,
                matchingAlgorithms,
                presentValues,
                folder.resolve("string_matching_present.png")
        );

        createLineChart(
                "String Matching: Pattern Absent",
                "Text length (characters)",
                "Average execution time (ms)",
                matchingSizes,
                matchingAlgorithms,
                absentValues,
                folder.resolve("string_matching_absent.png")
        );

        System.out.println("Created: dp_comparison.png");
        System.out.println("Created: string_matching_present.png");
        System.out.println("Created: string_matching_absent.png");
    }

    // MAIN
    public static void main(String[] args) {

        Random random = new Random(SEED);

        csvRows.add(
                "Algorithm,InputSize,Scenario,AverageTimeMs,"
                        + "PreprocessingComparisons,HashComparisons,"
                        + "CharacterComparisons,TotalComparisons"
        );

        try {

            Files.createDirectories(Path.of("results"));

            runDynamicProgramming(random);

            runStringMatching(random);

            runKasai(random);

            // Generate graphs
            generateCharts();

            // Save CSV
            Path output = Path.of(
                    "results",
                    "benchmark_results.csv"
            );

            Files.write(
                    output,
                    csvRows,
                    StandardCharsets.UTF_8
            );

            System.out.println("\n============================");
            System.out.println("EXPERIMENTS COMPLETED");
            System.out.println("============================");

            System.out.println("Runs per experiment: " + RUNS);
            System.out.println("Warm-up runs: " + WARMUP_RUNS);
            System.out.println("Random seed: " + SEED);
            System.out.println("Alphabet: " + ALPHABET);

            System.out.println("CSV: " + output);

            System.out.println("All graphs saved in results/");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Experiment failed", e
            );
        }
    }
}

