/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Jakub Danek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *  Please visit https://github.com/danekja/jdk-function-serializable if you need additional information or have any
 *  questions.
 *
 */

package org.danekja.java.misc.serializable;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableToDoubleFunction;
import org.danekja.java.util.function.serializable.SerializableToIntFunction;
import org.danekja.java.util.function.serializable.SerializableToLongFunction;

/**
 * Serializable version of {@link Comparator}.
 *
 * @author Emond Papegaaij
 */
@FunctionalInterface
public interface SerializableComparator<T> extends Comparator<T>, Serializable {
	default SerializableComparator<T> reversed() {
        return Collections.reverseOrder(this)::compare;
    }

    default SerializableComparator<T> thenComparing(SerializableComparator<? super T> other) {
        Objects.requireNonNull(other);
        return (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }

    default <U> SerializableComparator<T> thenComparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor,
            SerializableComparator<? super U> keyComparator)
    {
        return thenComparing(comparing(keyExtractor, keyComparator));
    }

    default <U extends Comparable<? super U>> SerializableComparator<T> thenComparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor)
    {
        return thenComparing(comparing(keyExtractor));
    }

    default SerializableComparator<T> thenComparingInt(
    		SerializableToIntFunction<? super T> keyExtractor) {
        return thenComparing(comparingInt(keyExtractor));
    }

    default SerializableComparator<T> thenComparingLong(
    		SerializableToLongFunction<? super T> keyExtractor) {
        return thenComparing(comparingLong(keyExtractor));
    }

    default SerializableComparator<T> thenComparingDouble(
    		SerializableToDoubleFunction<? super T> keyExtractor) {
        return thenComparing(comparingDouble(keyExtractor));
    }

    public static <T extends Comparable<? super T>> SerializableComparator<T> reverseOrder() {
        return Collections.reverseOrder()::compare;
    }

    public static <T extends Comparable<? super T>> SerializableComparator<T> naturalOrder() {
        return Comparator.<T>naturalOrder()::compare;
    }

    public static <T> SerializableComparator<T> nullsFirst(
    		SerializableComparator<? super T> comparator) {
        return Comparator.nullsFirst(comparator)::compare;
    }

    public static <T> SerializableComparator<T> nullsLast(
    		SerializableComparator<? super T> comparator) {
        return Comparator.nullsLast(comparator)::compare;
    }

    public static <T, U> SerializableComparator<T> comparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor,
            SerializableComparator<? super U> keyComparator)
    {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return 
            (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1),
                                              keyExtractor.apply(c2));
    }

    public static <T, U extends Comparable<? super U>> SerializableComparator<T> comparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }

    public static <T> SerializableComparator<T> comparingInt(
    		SerializableToIntFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
    }

    public static <T> SerializableComparator<T> comparingLong(
    		SerializableToLongFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));
    }

    public static<T> SerializableComparator<T> comparingDouble(
    		SerializableToDoubleFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));
    }
}
