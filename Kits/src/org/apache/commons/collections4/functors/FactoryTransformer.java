/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.functors;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;

import java.io.Serializable;

/**
 * Transformer implementation that calls a Factory and returns the result.
 *
 * @version $Id: FactoryTransformer.java 1686855 2015-06-22 13:00:27Z tn $
 * @since 3.0
 */
public class FactoryTransformer<I, O> implements Transformer<I, O>, Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -6817674502475353160L;

	/**
	 * The factory to wrap
	 */
	private final Factory<? extends O> iFactory;

	/**
	 * Constructor that performs no validation.
	 * Use <code>factoryTransformer</code> if you want that.
	 *
	 * @param factory the factory to call, not null
	 */
	public FactoryTransformer(final Factory<? extends O> factory) {
		super();
		iFactory = factory;
	}

	/**
	 * Factory method that performs validation.
	 *
	 * @param <I>     the input type
	 * @param <O>     the output type
	 * @param factory the factory to call, not null
	 * @return the <code>factory</code> transformer
	 * @throws NullPointerException if the factory is null
	 */
	public static <I, O> Transformer<I, O> factoryTransformer(final Factory<? extends O> factory) {
		if (factory == null) {
			throw new NullPointerException("Factory must not be null");
		}
		return new FactoryTransformer<I, O>(factory);
	}

	/**
	 * Transforms the input by ignoring the input and returning the result of
	 * calling the decorated factory.
	 *
	 * @param input the input object to transform
	 * @return the transformed result
	 */
	public O transform(final I input) {
		return iFactory.create();
	}

	/**
	 * Gets the factory.
	 *
	 * @return the factory
	 * @since 3.1
	 */
	public Factory<? extends O> getFactory() {
		return iFactory;
	}

}
