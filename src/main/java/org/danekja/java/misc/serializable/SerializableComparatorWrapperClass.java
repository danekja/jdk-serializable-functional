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

import java.text.Collator;
import java.util.Comparator;

import org.danekja.java.util.function.serializable.SerializableSupplier;

/**
 * Wrapper for a non-serializable subclass of {@link Comparator}, such as {@link Collator}.
 * This way you can still use such comparators in a serializable way.
 * 
 * This wrapper calls the given {@link SerializableSupplier} to retrieve a delegate {@link Comparator} which it
 * uses for all calls to its {@link #compare(Object, Object)}-method. It caches the retrieved {@link Comparator}
 * in a transient field for efficiency. 
 * 
 * Usage example:
 * 
 * <blockquote><pre>
 * SerializableComparator&lt;Object&gt; collator = new SerializableComparatorWrapper&lt;&gt;(() -&gt; Collator.getInstance(Locale.UK));
 * SerializableComparator&lt;Object&gt; objectComparator = SerializableComparator.comparing(Object::toString, collator);
 * </pre></blockquote>
 * 
 * (Note that Collator is an instance of Comparator typed with Object, not with a generic type variable.)
 * 
 * @author haster
 *
 * @param <T> comparable type
 */
public class SerializableComparatorWrapperClass<T> implements SerializableComparator<T>
{
	private static final long serialVersionUID = 1L;

	private SerializableSupplier<Comparator<T>> comparatorSupplier;

	private transient Comparator<T> delegate;

	public SerializableComparatorWrapperClass(SerializableSupplier<Comparator<T>> comparatorSupplier)
	{
		this.comparatorSupplier = comparatorSupplier;
	}

	@Override
	public int compare(T o1, T o2)
	{
		if (delegate == null)
			delegate = comparatorSupplier.get();
		return delegate.compare(o1, o2);
	}
}

