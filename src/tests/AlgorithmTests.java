package tests;

import algorithms.dp.LCS;
import algorithms.dp.EditDistance;
import algorithms.sm.NaiveStringMatching;
import algorithms.sm.KMP;
import algorithms.sm.RabinKarp;
import algorithms.aca.Kasai;

import java.util.Arrays;
import java.util.List;

public class AlgorithmTests {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String testName, Object expected, Object actual) {

        if (expected.equals(actual)) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
            failed++;
        }
    }

    // PART A: LCS TESTS
    private static void testLCS() {

        System.out.println("\n=== LCS TESTS ===");

        check("Identical strings", 3,
                LCS.findLCS("ABC", "ABC"));

        check("Completely different strings", 0,
                LCS.findLCS("ABC", "XYZ"));

        check("Empty string", 0,
                LCS.findLCS("", "ABC"));

        check("Common subsequence", 3,
                LCS.findLCS("ABCDE", "ACE"));
    }

    // PART A: EDIT DISTANCE TESTS
    private static void testEditDistance() {

        System.out.println("\n=== EDIT DISTANCE TESTS ===");

        check("Identical strings", 0,
                EditDistance.calculateDistance("ABC", "ABC"));

        check("One empty string", 3,
                EditDistance.calculateDistance("", "ABC"));

        check("Substitution", 1,
                EditDistance.calculateDistance("CAT", "CUT"));

        check("Insertion", 1,
                EditDistance.calculateDistance("CAT", "CATS"));

        check("Deletion", 1,
                EditDistance.calculateDistance("CATS", "CAT"));
    }

    // PART B: STRING MATCHING TESTS
    private static void testStringMatching() {

        System.out.println("\n=== STRING MATCHING TESTS ===");

        testAllSearchAlgorithms(
                "Pattern at beginning",
                "ABCXX", "ABC", Arrays.asList(0));

        testAllSearchAlgorithms(
                "Pattern in middle",
                "XXABCXX", "ABC", Arrays.asList(2));

        testAllSearchAlgorithms(
                "Pattern at end",
                "XXABC", "ABC", Arrays.asList(2));

        testAllSearchAlgorithms(
                "Multiple occurrences",
                "ABCXXABCXXABC", "ABC",
                Arrays.asList(0, 5, 10));

        testAllSearchAlgorithms(
                "Pattern absent",
                "ABCDE", "XYZ",
                Arrays.asList());

        testAllSearchAlgorithms(
                "Pattern longer than text",
                "ABC", "ABCDE",
                Arrays.asList());

        testAllSearchAlgorithms(
                "Empty pattern",
                "ABC", "",
                Arrays.asList(0, 1, 2, 3));
    }

    private static void testAllSearchAlgorithms(
            String testName,
            String text,
            String pattern,
            List<Integer> expected) {

        List<Integer> naive =
                NaiveStringMatching.search(text, pattern);

        List<Integer> kmp =
                KMP.search(text, pattern);

        List<Integer> rabinKarp =
                RabinKarp.search(text, pattern);

        check(testName + " - Naive", expected, naive);
        check(testName + " - KMP", expected, kmp);
        check(testName + " - Rabin-Karp", expected, rabinKarp);

        check(testName + " - All algorithms agree",
                naive, kmp);

        check(testName + " - Rabin-Karp agrees",
                naive, rabinKarp);
    }

    // PART C: KASAI TEST
    private static void testKasai() {

        System.out.println("\n=== KASAI TESTS ===");

        String text = "banana";

        int[] suffixArray = {5, 3, 1, 0, 4, 2};

        int[] expectedLCP = {0, 1, 3, 0, 0, 2};

        int[] actualLCP =
                Kasai.buildLCP(text, suffixArray);

        check("Kasai LCP array",
                Arrays.toString(expectedLCP),
                Arrays.toString(actualLCP));
    }

    public static void main(String[] args) {

        testLCS();

        testEditDistance();

        testStringMatching();

        testKasai();

        System.out.println("\n========================");
        System.out.println("TESTING SUMMARY");
        System.out.println("========================");

        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            throw new AssertionError("Some tests failed!");
        }
        System.out.println("\nAll tests passed successfully!");
    }
}
