package algorithms.dp;
public class EditDistance {

    public static int calculateDistance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insertion = dp[i][j - 1];
                    int deletion = dp[i - 1][j];
                    int substitution = dp[i - 1][j - 1];

                    dp[i][j] = 1 + Math.min(
                            insertion,
                            Math.min(deletion, substitution)
                    );
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {

        System.out.println(calculateDistance("CAT", "CUT"));
        System.out.println(calculateDistance("kitten", "sitting"));
        System.out.println(calculateDistance("ABC", "ABC"));
        System.out.println(calculateDistance("", "ABC"));
    }
}

