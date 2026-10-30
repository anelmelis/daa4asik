package algorithms.sm;
import java.util.ArrayList;
import java.util.List;

public class NaiveStringMatching {
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

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                results.add(i);
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
