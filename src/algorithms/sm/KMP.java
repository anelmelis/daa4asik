package algorithms.sm;
import java.util.ArrayList;
import java.util.List;
public class KMP {

    private static int[] buildLPS(String pattern) {

        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
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
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                results.add(i - j);
                j = lps[j - 1];
            }

            else if (i < n && text.charAt(i) != pattern.charAt(j)) {
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

        System.out.println(search("ABABCABABC", "ABC"));
        System.out.println(search("HELLO", "LL"));
        System.out.println(search("AAAAA", "AA"));
        System.out.println(search("ABCDE", "XYZ"));
        System.out.println(search("ABC", "ABCDE"));
        System.out.println(search("ABC", ""));
    }
}
