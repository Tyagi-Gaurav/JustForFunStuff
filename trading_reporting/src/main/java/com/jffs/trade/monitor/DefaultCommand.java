package com.jffs.trade.monitor;

import java.util.function.Function;

public class DefaultCommand<T, R> implements Command<T, R> {

    private final Function<T, R> function;

    public DefaultCommand(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public R execute(T t) {
        return function.apply(t);
    }
}
