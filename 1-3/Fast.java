import java.util.Arrays;

public class Fast {
    public static void main(String[] args)
    {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] p = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            p[i] = new Point(x, y);
            p[i].draw();
        }

        Point[] refP = new Point[N];
        Point[] backupP = new Point[N];
        System.arraycopy(p, 0, refP, 0, refP.length);

        if (N == 1) {
            StdOut.print(p[0]);
            return;
        }
        for (int i = 0; i < refP.length; i++) {
            //Arrays.sort(p);
            Arrays.sort(p, 0, p.length, refP[i].SLOPE_ORDER);
            int low = 0, high = 1;

            double originSlope = p[low].slopeTo(p[high]);

            //StdOut.printf("high: %d\n", high);
            for (int j = high + 1; j < p.length; j++) {
                double currentSlope = p[low].slopeTo(p[j]);
                /*
                if (i == 11) {StdOut.printf("low: %d high: %d\n", low, high);
                        StdOut.printf("low: %d high: %d\n", low, high);
                        for (int k = 0; k < N; k++) {
                            StdOut.print(p[k] + " ");
                        }

                        StdOut.println();
                        
                        for (int k = 0; k < N; k++) {
                            StdOut.print(p[k] + " ");
                        }
                        StdOut.println();
                        StdOut.println();
            }
            */


                if (originSlope == currentSlope) {
                    high++;
                } else {
                    if ((high - low) >= 3) {
                        
                        System.arraycopy(p, 0, backupP, 0, backupP.length);
                        Arrays.sort(p, low, high + 1);
                        
                        if (refP[i].compareTo(p[low]) == 0) {
                            StdOut.print(p[low]);
                            for (int k = low + 1; k <= high; k++) {
                                StdOut.print(" -> " + p[k]);
                            }
                            StdOut.println();

                            p[low].drawTo(p[high]);
                        } else {
                            System.arraycopy(backupP, 0, p, 0, backupP.length);
                        }
                    }

                    Point temp = p[low];
                    p[low] = p[high];
                    p[high] = temp;

                    low = high;
                    high = j;
                    originSlope = currentSlope;
                }
            }
            //StdOut.println();
            if ((high - low) >= 3) {
                        System.arraycopy(p, 0, backupP, 0, backupP.length);
                        Arrays.sort(p, low, high + 1);
                        
                        if (refP[i].compareTo(p[low]) == 0) {
                            StdOut.print(p[low]);
                            for (int k = low + 1; k <= high; k++) {
                                StdOut.print(" -> " + p[k]);
                            }
                            StdOut.println();

                            p[low].drawTo(p[high]);
                        } else {
                            System.arraycopy(backupP, 0, p, 0, backupP.length);
                        }
            }
        }

        StdDraw.show(0);
    }
}
