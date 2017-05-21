package org.berendeev.roma.rssreader;

import org.junit.Test;

public class FizzBuzz {

    @Test
    public void fizz_buzz(){
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0 || i % 5 == 0){
                if(i % 3 == 0){
                    System.out.print("Fizz");
                }
                if (i % 5 == 0){
                    System.out.print("Buzz");
                }
                System.out.println();
            }else {
                System.out.println(i);
            }
        }
    }
}
