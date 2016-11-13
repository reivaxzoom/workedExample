package edu.elte.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Primes {

    public static void main(String[] args) throws InterruptedException {

        List odd = new ArrayList();
        List m3 = new ArrayList();
        List m5 = new ArrayList();
        Set sec = IntStream.rangeClosed(0, 100).boxed().collect(Collectors.toSet());;
        
        
        
        IntStream.iterate(0, i -> i + 2).limit(100).forEach(x -> odd.add(x));
        IntStream.iterate(0, i -> i + 3).limit(100).forEach(y -> m3.add(y));
        IntStream.iterate(0, i -> i + 5).limit(100).forEach(z -> m5.add(z));
        IntStream.iterate(0, i -> i + 1).limit(100).forEach(s -> sec.add(s));

        Runnable rodd = () -> {
            IntStream.iterate(0, i -> i * 2).limit(100).forEach(x -> odd.add(x));
//            odd.stream().forEach(System.out::println);
        };

        Runnable rm3 = () -> {
            IntStream.iterate(0, i -> i * 3).limit(100).forEach(y -> m3.add(y));
//            m3.stream().forEach(System.out::println);
        };
        
        Runnable rm5 = () -> {
            IntStream.iterate(0, i -> i * 3).limit(100).forEach(y -> m3.add(y));
//            m3.stream().forEach(System.out::println);
        };
        
        Runnable rsec = () -> {
            IntStream.iterate(0, i -> i * 3).limit(100).forEach(y -> m3.add(y));
//            m3.stream().forEach(System.out::println);
        };
        
        
        Thread todd = new Thread(rodd);
        todd.start();
        Thread tm3 = new Thread(rodd);
        tm3.start();
        Thread tm5 = new Thread(rodd);
        tm5.start();
        Thread tsec = new Thread(rodd);
        tsec.start();
        
        todd.join();
        tm3.join();
        tm5.join();
        tsec.join();
        
        sec.removeAll(m5);
        sec.removeAll(m3);
        sec.removeAll(odd);
        
        sec.stream().forEach(System.out::println);
        
        System.out.println("Done!");
    }

}
