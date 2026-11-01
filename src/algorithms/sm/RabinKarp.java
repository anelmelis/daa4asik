package algorithms.sm;
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007;

    private static long characterComparisons = 0;
    private static long hashComparisons = 0;

    public static long getCharacterComparisons() {
        return characterComparisons;
    }

    public static long getHashComparisons() {
        return hashComparisons;
    }

    private static long calculateHash(String str, int length) {

        long hash = 0;

        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + str.charAt(i)) % MOD;
        }

        return hash;
    }

    private static boolean verifyMatch(
            String text, String pattern, int start) {

        for (int i = 0; i < pattern.length(); i++) {

            if (countingEnabled) {
                characterComparisons++;
            }

            if (text.charAt(start + i) != pattern.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public static List<Integer> search(String text, String pattern) {

        List<Integer> results = new ArrayList<>();

        characterComparisons = 0;
        hashComparisons = 0;

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

        long highestPower = 1;

        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % MOD;
        }

        long patternHash = calculateHash(pattern, m);
        long textHash = calculateHash(text, m);

        for (int i = 0; i <= n - m; i++) {

            // Count hash comparisons
            if (countingEnabled) {
                hashComparisons++;
            }

            if (patternHash == textHash) {

                if (verifyMatch(text, pattern, i)) {
                    results.add(i);
                }
            }

            // Rolling hash
            if (i < n - m) {

                long outgoing =
                        (text.charAt(i) * highestPower) % MOD;

                textHash = (textHash - outgoing + MOD) % MOD;

                textHash =
                        (textHash * BASE + text.charAt(i + m)) % MOD;
            }
        }

        return results;
    }

    public static void main(String[] args) {

        String text = "ABABCABABC";
        String pattern = "ABC";

        System.out.println(search(text, pattern));

        System.out.println("Hash comparisons: "
                + getHashComparisons());

        System.out.println("Character comparisons: "
                + getCharacterComparisons());
    }

    private static boolean countingEnabled = true;

    public static void setCountingEnabled(boolean enabled) {
        countingEnabled = enabled;
    }
}


