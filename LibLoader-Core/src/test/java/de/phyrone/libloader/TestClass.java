package de.phyrone.libloader;

import java.util.Arrays;

public class TestClass {
    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);
    }
}
