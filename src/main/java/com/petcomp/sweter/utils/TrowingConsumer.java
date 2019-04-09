package com.petcomp.sweter.utils;

import java.util.function.Consumer;

@FunctionalInterface
public interface TrowingConsumer<T, E extends Throwable> {

    void accept(T t) throws E;

    static <T, E extends Throwable> Consumer<T> unchecked(TrowingConsumer<T, E> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
