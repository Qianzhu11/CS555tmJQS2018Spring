package Project08;
import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public boolean splitArraySameAverage(int[] A) {
        int sum = 0;
        int n = A.length;
        for (int i : A) sum += i;
        double res = sum / n;
        for (int i = n / 2; i >= 1; i--) {
            List<Integer> list = new ArrayList<Integer>();
            dfs(A, i, 0, list, 0);
            for (int num : list)
                if (num / i == res) return true;
        }
        return false;
    }
    
    public void dfs(int[] A, int remain, int cur, List<Integer> list, int index) {
        if (remain == 0) {
            list.add(cur);
            return;
        }
        for (int i = index; i < A.length; i++) {
            dfs(A, remain - 1, cur + A[i], list, i);
        }
    }
	public static void main(String[] args) {
		Test t = new Test();
		System.out.println(t.splitArraySameAverage(new int[] {33,86,88,78,21,76,19,20,88,76,10,25,37,97,58,89,65,59,98,57,50,30}));
	}
}	
