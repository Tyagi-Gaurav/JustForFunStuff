package com.jffs.trade.strategy;

import com.jffs.trade.domain.DataFrame;

public interface Action {
    void execute(DataFrame dataFrame);
}
