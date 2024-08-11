package com.jffs.e2e.tests;

import java.util.List;

public record TestPaginatedWords(List<TestWord> words,
                                 int totalWords,
                                 int totalPages,
                                 int nextPage,
                                 int previousPage,
                                 int currentPage) {
}