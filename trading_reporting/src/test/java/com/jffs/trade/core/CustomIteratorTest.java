package com.jffs.trade.core;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomIteratorTest {

    @Test
    void iterator() {
        String[] arrayOfStrings = new String[] {"oslo", "madam", "car", "deed", "wow", "test"};
        final var customIterator = new CustomIterator<>(arrayOfStrings);

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("oslo");

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("madam");

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("car");
    }

    @Test
    void iteratorWithIndex() {
        String[] arrayOfStrings = new String[] {"oslo", "madam", "car", "deed", "wow", "test"};
        final var customIterator = new CustomIterator<>(arrayOfStrings, 2, 3);

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("car");

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("deed");

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("wow");

        assertThat(customIterator.hasNext()).isFalse();
    }

    @Test
    void throwsExceptionWhenNextAfterEndOfIterator() {
        String[] arrayOfStrings = new String[] {"oslo"};
        final var customIterator = new CustomIterator<>(arrayOfStrings);

        assertThat(customIterator.hasNext()).isTrue();
        assertThat(customIterator.next()).isEqualTo("oslo");

        assertThat(customIterator.hasNext()).isFalse();
        assertThrows(NoSuchElementException.class, customIterator::next);
    }
}