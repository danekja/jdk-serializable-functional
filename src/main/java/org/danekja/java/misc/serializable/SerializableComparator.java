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

	/**
	 * Returns a comparator that imposes the reverse ordering of this
	 * comparator.
	 *
	 * @return a comparator that imposes the reverse ordering of this
	 *         comparator.
	 * @since 1.8
	 */
	default SerializableComparator<T> reversed() {
        return Collections.reverseOrder(this)::compare;
    }

	/**
	 * Returns a lexicographic-order comparator with another comparator.
	 * If this {@code SerializableComparator} considers two elements equal, i.e.
	 * {@code compare(a, b) == 0}, {@code other} is used to determine the order.
	 *
	 * @apiNote
	 * For example, to sort a collection of {@code String} based on the length
	 * and then case-insensitive natural ordering, the comparator can be
	 * composed using following code,
	 *
	 * <pre>{@code
	 *     SerializableComparator<String> cmp = SerializableComparator.comparingInt(String::length)
	 *             .thenComparing(String.CASE_INSENSITIVE_ORDER);
	 * }</pre>
	 *
	 * @param  other the other comparator to be used when this comparator
	 *         compares two objects that are equal.
	 * @return a lexicographic-order comparator composed of this and then the
	 *         other comparator
	 * @throws NullPointerException if the argument is null.
	 * @since 1.8
	 */
    default SerializableComparator<T> thenComparing(SerializableComparator<? super T> other) {
        Objects.requireNonNull(other);
        return (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }

	/**
	 * Returns a lexicographic-order comparator with a function that
	 * extracts a key to be compared with the given {@code SerializableComparator}.
	 *
	 * @implSpec This default implementation behaves as if {@code
	 *           thenComparing(comparing(keyExtractor, cmp))}.
	 *
	 * @param  <U>  the type of the sort key
	 * @param  keyExtractor the function used to extract the sort key
	 * @param  keyComparator the {@code SerializableComparator} used to compare the sort key
	 * @return a lexicographic-order comparator composed of this comparator
	 *         and then comparing on the key extracted by the keyExtractor function
	 * @throws NullPointerException if either argument is null.
	 * @see #comparing(SerializableFunction, SerializableComparator)
	 * @see #thenComparing(SerializableComparator)
	 * @since 1.8
	 */
    default <U> SerializableComparator<T> thenComparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor,
            SerializableComparator<? super U> keyComparator)
    {
        return thenComparing(comparing(keyExtractor, keyComparator));
    }

	/**
	 * Returns a lexicographic-order comparator with a function that
	 * extracts a {@code Comparable} sort key.
	 *
	 * @implSpec This default implementation behaves as if {@code
	 *           thenComparing(comparing(keyExtractor))}.
	 *
	 * @param  <U>  the type of the {@link Comparable} sort key
	 * @param  keyExtractor the function used to extract the {@link
	 *         Comparable} sort key
	 * @return a lexicographic-order comparator composed of this and then the
	 *         {@link Comparable} sort key.
	 * @throws NullPointerException if the argument is null.
	 * @see #comparing(SerializableFunction)
	 * @see #thenComparing(SerializableComparator)
	 * @since 1.8
	 */
    default <U extends Comparable<? super U>> SerializableComparator<T> thenComparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor)
    {
        return thenComparing(comparing(keyExtractor));
    }

	/**
	 * Returns a lexicographic-order comparator with a function that
	 * extracts an {@code int} sort key.
	 *
	 * @implSpec This default implementation behaves as if {@code
	 *           thenComparing(comparingInt(keyExtractor))}.
	 *
	 * @param  keyExtractor the function used to extract the integer sort key
	 * @return a lexicographic-order comparator composed of this and then the
	 *         {@code int} sort key
	 * @throws NullPointerException if the argument is null.
	 * @see #comparingInt(SerializableToIntFunction)
	 * @see #thenComparing(SerializableComparator)
	 * @since 1.8
	 */
    default SerializableComparator<T> thenComparingInt(
    		SerializableToIntFunction<? super T> keyExtractor) {
        return thenComparing(comparingInt(keyExtractor));
    }

	/**
	 * Returns a lexicographic-order comparator with a function that
	 * extracts a {@code long} sort key.
	 *
	 * @implSpec This default implementation behaves as if {@code
	 *           thenComparing(comparingLong(keyExtractor))}.
	 *
	 * @param  keyExtractor the function used to extract the long sort key
	 * @return a lexicographic-order comparator composed of this and then the
	 *         {@code long} sort key
	 * @throws NullPointerException if the argument is null.
	 * @see #comparingLong(SerializableToLongFunction)
	 * @see #thenComparing(SerializableComparator)
	 * @since 1.8
	 */
    default SerializableComparator<T> thenComparingLong(
    		SerializableToLongFunction<? super T> keyExtractor) {
        return thenComparing(comparingLong(keyExtractor));
    }

	/**
	 * Returns a lexicographic-order comparator with a function that
	 * extracts a {@code double} sort key.
	 *
	 * @implSpec This default implementation behaves as if {@code
	 *           thenComparing(comparingDouble(keyExtractor))}.
	 *
	 * @param  keyExtractor the function used to extract the double sort key
	 * @return a lexicographic-order comparator composed of this and then the
	 *         {@code double} sort key
	 * @throws NullPointerException if the argument is null.
	 * @see #comparingDouble(SerializableToDoubleFunction)
	 * @see #thenComparing(SerializableComparator)
	 * @since 1.8
	 */
    default SerializableComparator<T> thenComparingDouble(
    		SerializableToDoubleFunction<? super T> keyExtractor) {
        return thenComparing(comparingDouble(keyExtractor));
    }

	/**
	 * Returns a comparator that imposes the reverse of the <em>natural
	 * ordering</em>.
	 *
	 * <p>The returned comparator throws {@link NullPointerException} when
	 * comparing {@code null}.
	 *
	 * @param  <T> the {@link Comparable} type of element to be compared
	 * @return a comparator that imposes the reverse of the <i>natural
	 *         ordering</i> on {@code Comparable} objects.
	 * @see Comparable
	 * @since 1.8
	 */
    public static <T extends Comparable<? super T>> SerializableComparator<T> reverseOrder() {
        return Collections.reverseOrder()::compare;
    }

	/**
	 * Returns a comparator that compares {@link Comparable} objects in natural
	 * order.
	 *
	 * <p>The returned comparator throws {@link NullPointerException} when
	 * comparing {@code null}.
	 *
	 * @param  <T> the {@link Comparable} type of element to be compared
	 * @return a comparator that imposes the <i>natural ordering</i> on {@code
	 *         Comparable} objects.
	 * @see Comparable
	 * @since 1.8
	 */
    public static <T extends Comparable<? super T>> SerializableComparator<T> naturalOrder() {
        return Comparator.<T>naturalOrder()::compare;
    }

	/**
	 * Returns a null-friendly comparator that considers {@code null} to be
	 * less than non-null. When both are {@code null}, they are considered
	 * equal. If both are non-null, the specified {@code Comparator} is used
	 * to determine the order. If the specified comparator is {@code null},
	 * then the returned comparator considers all non-null values to be equal.
	 *
	 * @param  <T> the type of the elements to be compared
	 * @param  comparator a {@code SerializableComparator} for comparing non-null values
	 * @return a comparator that considers {@code null} to be less than
	 *         non-null, and compares non-null objects with the supplied
	 *         {@code SerializableComparator}.
	 * @since 1.8
	 */
    public static <T> SerializableComparator<T> nullsFirst(
    		SerializableComparator<? super T> comparator) {
        return Comparator.nullsFirst(comparator)::compare;
    }

	/**
	 * Returns a null-friendly comparator that considers {@code null} to be
	 * greater than non-null. When both are {@code null}, they are considered
	 * equal. If both are non-null, the specified {@code Comparator} is used
	 * to determine the order. If the specified comparator is {@code null},
	 * then the returned comparator considers all non-null values to be equal.
	 *
	 * @param  <T> the type of the elements to be compared
	 * @param  comparator a {@code SerializableComparator} for comparing non-null values
	 * @return a comparator that considers {@code null} to be greater than
	 *         non-null, and compares non-null objects with the supplied
	 *         {@code SerializableComparator}.
	 * @since 1.8
	 */
    public static <T> SerializableComparator<T> nullsLast(
    		SerializableComparator<? super T> comparator) {
        return Comparator.nullsLast(comparator)::compare;
    }

	/**
	 * Accepts a function that extracts a sort key from a type {@code T}, and
	 * returns a {@code SerializableComparator<T>} that compares by that sort
	 * key using the specified {@link SerializableComparator}.
	 *
	 * <p>The returned comparator is serializable.
	 *
	 * @apiNote
	 * For example, to obtain a {@code SerializableComparator} that compares
	 * {@code Person} objects by their last name ignoring case differences,
	 *
	 * <pre>{@code
	 *     SerializableComparator<Person> cmp = SerializableComparator.comparing(
	 *             Person::getLastName,
	 *             String.CASE_INSENSITIVE_ORDER);
	 * }</pre>
	 *
	 * @param  <T> the type of element to be compared
	 * @param  <U> the type of the sort key
	 * @param  keyExtractor the function used to extract the sort key
	 * @param  keyComparator the {@code SerializableComparator} used to compare the sort key
	 * @return a comparator that compares by an extracted key using the
	 *         specified {@code SerializableComparator}
	 * @throws NullPointerException if either argument is null
	 * @since 1.8
	 */
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

	/**
	 * Accepts a function that extracts a {@link java.lang.Comparable
	 * Comparable} sort key from a type {@code T}, and returns a {@code
	 * Comparator<T>} that compares by that sort key.
	 *
	 * @apiNote
	 * For example, to obtain a {@code SerializableComparator} that compares
	 * {@code Person} objects by their last name,
	 *
	 * <pre>{@code
	 *     Comparator<Person> byLastName = Comparator.comparing(Person::getLastName);
	 * }</pre>
	 *
	 * @param  <T> the type of element to be compared
	 * @param  <U> the type of the {@code Comparable} sort key
	 * @param  keyExtractor the function used to extract the {@link
	 *         Comparable} sort key
	 * @return a comparator that compares by an extracted key
	 * @throws NullPointerException if the argument is null
	 * @since 1.8
	 */
    public static <T, U extends Comparable<? super U>> SerializableComparator<T> comparing(
    		SerializableFunction<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }

	/**
	 * Accepts a function that extracts an {@code int} sort key from a type
	 * {@code T}, and returns a {@code Comparator<T>} that compares by that
	 * sort key.
	 *
	 * @param  <T> the type of element to be compared
	 * @param  keyExtractor the function used to extract the integer sort key
	 * @return a comparator that compares by an extracted key
	 * @see #comparing(SerializableFunction)
	 * @throws NullPointerException if the argument is null
	 * @since 1.8
	 */
    public static <T> SerializableComparator<T> comparingInt(
    		SerializableToIntFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
    }

	/**
	 * Accepts a function that extracts a {@code long} sort key from a type
	 * {@code T}, and returns a {@code Comparator<T>} that compares by that
	 * sort key.
	 *
	 * @param  <T> the type of element to be compared
	 * @param  keyExtractor the function used to extract the long sort key
	 * @return a comparator that compares by an extracted key
	 * @see #comparing(SerializableFunction)
	 * @throws NullPointerException if the argument is null
	 * @since 1.8
	 */
    public static <T> SerializableComparator<T> comparingLong(
    		SerializableToLongFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));
    }

	/**
	 * Accepts a function that extracts a {@code double} sort key from a type
	 * {@code T}, and returns a {@code SerializableComparator<T>} that compares
	 * by that sort key.
	 *
	 * @param  <T> the type of element to be compared
	 * @param  keyExtractor the function used to extract the double sort key
	 * @return a comparator that compares by an extracted key
	 * @see #comparing(SerializableFunction)
	 * @throws NullPointerException if the argument is null
	 * @since 1.8
	 */
    public static<T> SerializableComparator<T> comparingDouble(
    		SerializableToDoubleFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));
    }
}
