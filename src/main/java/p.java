import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class p {
//    public static long maxValue(int[] duration, int[] rating, int limit){
//        return helper2(duration,rating,limit,0,0,0,Integer.MAX_VALUE);
//    }
//    public static int helper2(int[] duration, int[] rating, int limit, int index, int total, int sum, int rate){
//        if(index==duration.length || limit==0){
//            return total;
//        }
//
//        int i = helper2(duration, rating, limit-1, index+1,
//                Math.max(total,(sum + duration[index])*Math.min(rate,rating[index])),
//                sum + duration[index],
//                Math.min(rate,rating[index]));
//
//        int j = helper2(duration, rating, limit, index+1,
//                Math.max(total,(sum*rate)),
//                sum ,
//                rate);
//
//        return Math.max(i,j);
//    }

    public static long maxValue(int[] duration, int[] rating, int limit) {

        int n = duration.length;

        long[][] movies = new long[n][2];

        for (int i = 0; i < n; i++) {
            movies[i][0] = rating[i];
            movies[i][1] = duration[i];
        }

        // Highest rating first
        Arrays.sort(movies, (a, b) -> Long.compare(b[0], a[0]));

        // Keep the largest 'limit' durations
        PriorityQueue<Long> pq = new PriorityQueue<>();

        long sum = 0;
        long answer = 0;

        for (int i = 0; i < n; i++) {

            long rate = movies[i][0];
            long dur = movies[i][1];

            pq.offer(dur);
            sum += dur;

            if (pq.size() > limit) {
                sum -= pq.poll();
            }

            answer = Math.max(answer, sum * rate);
        }

        return answer;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Enter number of movies
        System.out.print("Enter number of movies: ");
        int n = sc.nextInt();

        int[] duration = new int[n];
        int[] rating = new int[n];

        // Enter durations
        System.out.println("Enter durations:");
        for (int i = 0; i < n; i++) {
            duration[i] = sc.nextInt();
        }

        // Enter ratings
        System.out.println("Enter ratings:");
        for (int i = 0; i < n; i++) {
            rating[i] = sc.nextInt();
        }

        // Enter limit
        System.out.print("Enter limit: ");
        int limit = sc.nextInt();

        long answer = maxValue(duration, rating, limit);

        System.out.println("Maximum value = " + answer);

        sc.close();
    }

}
