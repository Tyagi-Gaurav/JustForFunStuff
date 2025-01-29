package com.jffs.trade.domain;

import java.util.List;

public record Correlation(List<String> direct, List<String> inverse) {
}