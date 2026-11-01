package algorithms.sm;
import java.util.ArrayList;
import java.util.List;

public class KMP {

    private static long preprocessingComparisons = 0;
    private static long searchComparisons = 0;

    public static long getPreprocessingComparisons() {
        return preprocessingComparisons;
    }

    public static long getSearchComparisons() {
        return searchComparisons;
    }

    private static int[] buildLPS(String pattern) {

        int m = pattern.length();
        int[] lps = new int[m];

        int len = 0;
        int i = 1;

        while (i < m) {

            if (countingEnabled) {
                preprocessingComparisons++;
            }

            if (pattern.charAt(i) == pattern.charAt(len)) {

                len++;
                lps[i] = len;
                i++;

            } else {

                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    public static List<Integer> search(String text, String pattern) {

        List<Integer> results = new ArrayList<>();

        preprocessingComparisons = 0;
        searchComparisons = 0;

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                results.add(i);
            }
            return results;
        }

        if (m > n) {
            return results;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {

            if (countingEnabled) {
                searchComparisons++;
            }

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == m) {
                    results.add(i - j);
                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return results;
    }

    public static void main(String[] args) {

        String text = "ABABCABABC";
        String pattern = "ABC";

        System.out.println(search(text, pattern));

        System.out.println(
                "Preprocessing comparisons: "
                        + getPreprocessingComparisons()
        );

        System.out.println(
                "Search comparisons: "
                        + getSearchComparisons()
        );
    }
    private static boolean countingEnabled = true;

    public static void setCountingEnabled(boolean enabled) {
        countingEnabled = enabled;
    }
}

