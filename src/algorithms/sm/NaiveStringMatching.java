package algorithms.sm;
import java.util.ArrayList;
import java.util.List;

public class NaiveStringMatching {
    private static long characterComparisons = 0;
    public static long getCharacterComparisons() {
        return characterComparisons;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        characterComparisons = 0;

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

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m) {
                characterComparisons++;

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }

            if (j == m) {
                results.add(i);
            }
        }
        return results;
    }

    public static void main(String[] args) {

        String text = "ABABCABABC";
        String pattern = "ABC";
        System.out.println(search(text, pattern));
        System.out.println(
                "Character comparisons: "
                        + getCharacterComparisons()
        );
    }
}

