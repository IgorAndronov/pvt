package com.pvt.util;


import lombok.Getter;

import java.util.Optional;

@Getter
public class Tuple<F, S> {

    private F first;

    private Optional<S> second;

    public static <F, S> Tuple<F, S> create(F first, S second) {
        return new Tuple<>(first, second);
    }

    public static <F, S> Tuple<F, S> create(F first) {
        return new Tuple<>(first, null);
    }


    private Tuple(F first, S second) {
        this.first = first;
        this.second = Optional.ofNullable(second);
    }
}
