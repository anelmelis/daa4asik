package algorithms.sm;
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007;

    private static long calculateHash(String str, int length) {
        long hash = 0;

        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + str.charAt(i)) % MOD;
        }
        return hash;
    }

    private static boolean verifyMatch(String text, String pattern, int start) {
        for (int i = 0; i < pattern.length(); i++) {
            if (text.charAt(start + i) != pattern.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
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
            if (patternHash == textHash) {
                if (verifyMatch(text, pattern, i)) {
                    results.add(i);
                }
            }
            if (i < n - m) {
                long outgoing = (text.charAt(i) * highestPower) % MOD;
                textHash = (textHash - outgoing + MOD) % MOD;
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
            }
        }
        return results;
    }

    public static void main(String[] args) {

        System.out.println(search("ABABCABABC", "ABC"));
        System.out.println(search("HELLO", "LL"));
        System.out.println(search("AAAAA", "AA"));
        System.out.println(search("ABCDE", "XYZ"));
        System.out.println(search("ABC", "ABCDE"));
        System.out.println(search("ABC", ""));
    }
}
