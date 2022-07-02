/**
 * Notes taken from learning Dynamic Programming through freecodecamp.org
 * the lecture can be found here
 * https://www.youtube.com/watch?v=oBt53YbR9Kk&list=WL&index=14
 * 
 * this document is created by Parker Qi
 * anyone is free to reuse this document
 */
import java.util.HashMap; // import the HashMap class
import java.util.ArrayList; // import the ArrayList class

public class DynamicPrograming{
    // example 1 
    // fibonacci sequence finder
    /**
     * normal fibonacci sequence finder
     * @param num the x th fibonacci number
     * @return the fibonacci number
     */
    public static long fib (int num) {
        if (num <= 2) return 1;
        return fib(num-1)+fib(num-2);
    }

    /**
     * fibonacci sequence finder using dynamic programming (tabulation)
     * using the property of fibonacci sequence, not recursive
     * results saved in a array
     * 
     * when tabulating visualize the problem as a table
     * size the table base on inputs
     * initialize table with some value & put basecase value to the table
     * iterate through the table
     * 
     * @param num the x th fibonacci number
     * @return the fibonacci number
     */
    public static long tabFib (int num) {
        // uses array
        long[] numLine = new long[num + 1];
        numLine[0] = 0;
        numLine[1] = 1;
        for (int i = 2; i <= num; i++) {
            numLine[i] = numLine[i - 1] + numLine[i - 2];
        }
        return numLine[num];
    }

    /**
     * fibonacci sequence finder using dynamic programming (memorization)
     * a generic finder, results saved in a hashmap
     * this method is usefull when the new result can be calculated using the past result
     * visualizing the problem as a tree would help
     * @param num the x th fibonacci number
     * @param stor hashmap to store the past results
     * @return the fibonacci number
     */
    public static long dyFib (int num, HashMap<Integer, Long> stor) {
        // shorthand using the stored answer
        if (stor.containsKey(num)) return stor.get(num);

        // state the base solution
        if (num <= 2) return 1;

        // store the past solutions
        stor.put(num, dyFib(num-1, stor) + dyFib(num-2, stor));

        // return answer
        return stor.get(num);
    }
    
    // example 2 
    // summation finder

    /**
     * find one way of summing number to reach target number
     * not dynamic programming approach 
     * @param target the target number to find
     * @param numArr the pool of number to add up
     * @param ans the answer number in ArrayList
     * @return the answer in ArrayList
     */
    public static ArrayList<Integer> howSum(int target, int[] numArr, ArrayList<Integer> ans) {
        if (target < 0) return null;
        if (target == 0) return ans;

        for (int curr: numArr) {
            ArrayList<Integer> temp = howSum(target - curr, numArr, ans);
            if (temp != null) {
                temp.add(curr);
                return temp;
            }
        }
        return null;
    }

    /**
     * find one way of summing number to reach target number
     * using dynamic programming approach
     * 
     * consider the algorithm as a tree
     * if target is 7, numArr is [2, 4]
     * 
     *                7 
     *           /         \
     *          5            3
     *        /   \        /   \
     *      3      1      1     -1
     *     / \    / \    / \
     *    1  -1  -1 -3  -1 -3
     *   / \
     *  -1 -3
     * 
     * When recurse normally, all leafs are reached. 
     * However, we can memorize the past result thus,
     * answer of node 1, 3 are memorized, therefore saves time.
     *  
     * @param target the target number to find
     * @param numArr the pool of number to add up
     * @param ans the answer number in ArrayList
     * @param stor store the past answer
     * @return the answer in ArrayList
     */
    public static ArrayList<Integer> howSumDy(int target, int[] numArr, ArrayList<Integer> ans, HashMap<Integer, ArrayList<Integer>> stor) {
        // shorthand using the stored answer
        if (stor.containsKey(target)) return stor.get(target);

        // state base case
        if (target < 0) return null;
        if (target == 0) return ans;

        // iterate
        for (int curr: numArr) {
            ArrayList<Integer> temp = howSumDy(target - curr, numArr, ans, stor);
            if (temp != null) {
                temp.add(curr);
                // store solutions before return
                stor.put(target, temp);
                return temp;
            }
        }
        // store solutions before return
        stor.put(target, null);
        return null;
    }

    /**
     * howSum also can be coded though tabulation. For instance, 
     * target: 8 
     * numArr: [3, 2]
     * 1. create an table lenght 9
     *       0  1  2  3  4  5  6  7  8
     *      [x, x, x, x, x, x, x, x, x]
     * 2. we know that number 0, 2, 3 can be summed through the given number, thus
     *       0  1  2  3  4  5  6  7  8 
     *     [[], x, 2, 3, x, x, x, x, x]
     * 3. given that 0, 2, 3 can be summed, we know that 5=(3+2), 4=(2+2), 6=(3+3) can also be summed, thus
     *       0, 1  2  3  4  5  6  7  8
     *     [[], x, 2, 3, 2, 3, 3, x, x]
     *                   2  2  3
     * 4. given that 5, 4, 6 can be summed, we know that 7(3+2+2), 8(2+2+2+2) can also be summed, thus
     *       0, 1  2  3  4  5  6  7  8
     *     [[], x, 2, 3, 2, 3, 2, 3, 2]
     *                   2  2  2  2  2
     *                         2  2  2
     *                               2
     * 5. return array at index 8 [2,2,2,2]
     * 
     * @param target the target number to find
     * @param numArr the pool of number to add up
     * @param ans the answer number in ArrayList
     * @param stor store the past answer
     * @return the answer in ArrayList
     */
    public static ArrayList<Integer> howSumTab(int target, int[] numArr, ArrayList<Integer> ans) {
        // array of ArrayLists
        ArrayList[] table = new ArrayList[target + 1];
        for (int i = 0; i <= target; i++) {
            table[i] = new ArrayList<Integer>();
        }
        // loop through each array index
        for (int i = 0; i <= target; i++) {
            if (table[i].size() != 0 || i == 0) {
                for (int curr : numArr) {
                    if (curr + i <= target) {
                        // assign a create a copy of current ArrayList to the target
                        // add the new number to target
                        table[curr + i] = (ArrayList)table[i].clone();
                        table[curr + i].add(curr);
                    }
                }
            }
        }
        return table[target]; 
    }


    public static void main(String []args){
        // example 1
        /*
        System.out.println(fib(5));
        System.out.println(tabFib(50));
        HashMap<Integer, Long> storage = new HashMap<Integer, Long>();
        System.out.println(dyFib(50, storage));
        */
        // example 2
        ArrayList<Integer> ans = new ArrayList<Integer>();
        int[] numArr = {7, 14};
        String answer;

        // run howSum
        ans = howSum(28, numArr, ans);
        answer = (ans != null) ? ans.toString() : null;
        System.out.println(answer);

        // run howSumDy
        HashMap<Integer, ArrayList<Integer>> stor = new HashMap<Integer, ArrayList<Integer>>();
        ans = howSumDy(300, numArr, ans, stor);
        answer = (ans != null) ? ans.toString() : null;
        System.out.println(answer);

        // run howSumTab
        ans = howSumTab(300, numArr, ans);
        answer = (ans.size() != 0) ? ans.toString() : null;
        System.out.println(answer);
    }
}