package ASIris;

import java.util.Arrays;

public class MyArrayCalc {
    static final int SIZE = 10_000_000;
    static final int HALF = SIZE/2;
    float[] arr = new float[SIZE];

    public void doOne() {
        long timeStart = System.currentTimeMillis();
        Arrays.fill(arr, 1);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + (i) / 5) * Math.cos(0.2f + (i) / 5) *
                    Math.cos(0.4f + (i) / 2));
        }
        long timeEnd = System.currentTimeMillis() - timeStart;
        System.out.println(timeEnd);
    }

    public void doTwo(){
        long timeStart = System.currentTimeMillis();
        Arrays.fill(arr, 1);

        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];

        System.arraycopy(arr,0,a1,0, HALF);
        System.arraycopy(arr,HALF,a2,0, HALF);

        System.out.println(a1.length);
        System.out.println(a2.length);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a1) {
                    System.out.println("Start 1");
                    for (int i = 0; i < a1.length; i++) {
                        a1[i] = (float) (a1[i] * Math.sin(0.2f + (i) / 5) * Math.cos(0.2f + (i) / 5) *
                                Math.cos(0.4f + (i) / 2));
                    }
                    System.out.println("End 1");
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a2) {
                    System.out.println("Start 2");
                    for (int i = 0; i < a2.length; i++) {
                        a2[i] = (float) (a2[i] * Math.sin(0.2f + (i) / 5) * Math.cos(0.2f + (i) / 5) *
                                Math.cos(0.4f + (i) / 2));
                    }
                    System.out.println("End 2");
                }
            }
        });

        t1.start();
        t2.start();

//        try {
//            t1.join();
//            t2.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        synchronized (a1) {
            synchronized (a2) {
                System.out.println("Final");
                long timeEnd = System.currentTimeMillis() - timeStart;
                System.out.println(timeEnd);
            }
        }
    }
}
