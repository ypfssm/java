package com.ypf.generic;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Typesafe heterogeneous container 应用场景：？？？
 *
 *
 * references：
 *      https://gerardnico.com/code/design_pattern/typesafe_heterogeneous_container#example1
 *      https://www.baeldung.com/gson-json-to-map
 * @author shuaifei
 */
public class TypesafeHeterogeneousContainer {

    static AtomicInteger counter = new AtomicInteger(3);


    public void copyOf() {

    }

    public static void main(String[] args) {
        System.out.println(counter.getAndSet(-1));
        System.out.println(counter.get());

        System.out.println(counter.incrementAndGet());

        try {
           Selector selector =  Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
