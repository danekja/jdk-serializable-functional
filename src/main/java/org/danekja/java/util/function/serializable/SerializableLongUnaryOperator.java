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

package org.danekja.java.util.function.serializable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.LongUnaryOperator;

/**
 * Serializable version of {@link LongUnaryOperator}
 *
 * @author Jakub Danek (www.danekja.org)
 */
@FunctionalInterface
public interface SerializableLongUnaryOperator extends LongUnaryOperator, Serializable {
	/**
	 * Returns a composed operator that first applies the {@code before}
	 * operator to its input, and then applies this operator to the result.
	 * If evaluation of either operator throws an exception, it is relayed to
	 * the caller of the composed operator.
	 *
	 * @param before the operator to apply before this operator is applied
	 * @return a composed operator that first applies the {@code before}
	 * operator and then applies this operator
	 * @throws NullPointerException if before is null
	 *
	 * @see #andThen(LongUnaryOperator)
	 */
	default SerializableLongUnaryOperator compose(SerializableLongUnaryOperator before) {
		Objects.requireNonNull(before);
		return (long v) -> applyAsLong(before.applyAsLong(v));
	}

	/**
	 * Returns a composed operator that first applies this operator to
	 * its input, and then applies the {@code after} operator to the result.
	 * If evaluation of either operator throws an exception, it is relayed to
	 * the caller of the composed operator.
	 *
	 * @param after the operator to apply after this operator is applied
	 * @return a composed operator that first applies this operator and then
	 * applies the {@code after} operator
	 * @throws NullPointerException if after is null
	 *
	 * @see #compose(LongUnaryOperator)
	 */
	default SerializableLongUnaryOperator andThen(SerializableLongUnaryOperator after) {
		Objects.requireNonNull(after);
		return (long t) -> after.applyAsLong(applyAsLong(t));
	}

	/**
	 * Returns a unary operator that always returns its input argument.
	 *
	 * @return a unary operator that always returns its input argument
	 */
	static SerializableLongUnaryOperator identity() {
		return t -> t;
	}
}
