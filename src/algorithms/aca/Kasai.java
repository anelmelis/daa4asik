package algorithms.aca;
import java.util.Arrays;

public class Kasai {
    public static int[] buildLCP(String text, int[] suffixArray) {
        int n = text.length();

        if (suffixArray.length != n) {
            throw new IllegalArgumentException(
                    "Suffix array length must equal text length"
            );
        }

        int[] lcp = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            int suffixIndex = suffixArray[i];
            if (suffixIndex < 0 || suffixIndex >= n) {
                throw new IllegalArgumentException(
                        "Invalid suffix index"
                );
            }
            rank[suffixIndex] = i;
        }

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                k = 0;
                continue;
            }
            int j = suffixArray[rank[i] - 1];

            while (i + k < n && j + k < n
                    && text.charAt(i + k) == text.charAt(j + k)) {

                k++;
            }

            lcp[rank[i]] = k;

            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }

    public static void main(String[] args) {

        String text = "banana";
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        int[] lcp = buildLCP(text, suffixArray);

        System.out.println("Text: " + text);
        System.out.println("Suffix Array: "
                + Arrays.toString(suffixArray));

        System.out.println("LCP Array: "
                + Arrays.toString(lcp));
    }
}

