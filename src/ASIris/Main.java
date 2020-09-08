package ASIris;

import java.util.Arrays;

public class Main {

    static final int SIZE = 10_000_000;
    static final int HALF = SIZE/2;

    public static void main(String[] args) {
        float[] arr = new float[SIZE];
        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];

        //работа методов из разбора ДЗ
        new MyArrayCalc().doOne();
        new MyArrayCalc().doTwo();

        //заполняем исходный массив
        Arrays.fill(arr, 1);

////////////////// проход в один поток
        //отсчет времени
        long timeStart = System.currentTimeMillis();
        //метод обработки массива
        fillArray(arr, 0);
        //вывод затраченного времени
        long timeEnd = System.currentTimeMillis() - timeStart;
        System.out.println("Время вычисления при проходе одним потоком по всему массиву (мс): " +timeEnd);

        //сброс массива
        Arrays.fill(arr, 1);

////////////////проход в два потока
        //новая отсечка времени
        //делим массив на части
        long timeStart2 = System.currentTimeMillis();
        System.arraycopy(arr,0,arr1,0, HALF);
        System.arraycopy(arr,HALF,arr2,0, HALF);

        //создаем новые потоки, в которых переопределяем метод ран статическим методом из мэйн
        Thread thread1 = new Thread(() -> fillArray(arr1, 0));
        Thread thread2 = new Thread(() -> fillArray(arr2, HALF));

        //запускаем потоки
        thread1.start();
        thread2.start();
        //ждем их окончания, так как слияние массивов будет в мэйне, иначе можем получить не полные данные
        try {
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //склейка половинок в исходный массив
        System.arraycopy(arr1,0,arr,0,HALF);
        System.arraycopy(arr2, 0, arr, HALF, HALF);
        //вывод затраченного времени
        timeEnd = System.currentTimeMillis() - timeStart2;
        System.out.println("Время вычисления в двух потоках (мс): " +timeEnd);
    }

    //статический метод для уменьшения количества повторяющегося кода
    static void fillArray (float[] array, int k) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + (i+k) / 5) * Math.cos(0.2f + (i+k) / 5) *
                    Math.cos(0.4f + (i+k) / 2));
        }
    }
}
