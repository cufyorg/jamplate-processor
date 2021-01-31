/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.model.reference;

import org.jamplate.model.document.Document;

/**
 * An implementation of the interface {@link Reference} that is a slice of another
 * reference.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class SubReference extends AbstractReference {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 8737902111302332073L;

	/**
	 * Construct a new sub-reference from the given {@code parent} reference. The
	 * constructed reference will have the same {@link #document()} as the given {@code
	 * parent} reference. It will have its {@link #content()} lazily initialized and
	 * equals to the {@link String#substring(int, int)} of the {@link
	 * Document#readContent()} of the document of the given {@code parent} reference.
	 * Also, the constructed reference will have its {@link #position()} equals to the sum
	 * of the given {@code position} and the {@link #position()} of the given {@code
	 * parent} reference. Finally, its obvious that the constructed reference will have
	 * the given {@code parent} reference as its {@link #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the
	 * given {@code parent} reference.
	 *
	 * @param parent   the parent source reference.
	 * @param position the sub-position to get from the given {@code parent} reference.
	 * @param length   the length to get from the given {@code parent} reference.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length} is more than the
	 *                                   length of the given {@code parent}.
	 * @throws IllegalStateException     if the given {@code parent} or is a deserialized
	 *                                   reference.
	 * @since 0.2.0 ~2021.01.17
	 */
	public SubReference(Reference parent, int position, int length) {
		super(
				parent,
				position,
				length
		);
	}

	@Override
	public Reference subReference(int position) {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Source");
		return new SubReference(
				this,
				position,
				this.length - position
		);
	}

	@Override
	public Reference subReference(int position, int length) {
		if (!this.constructed)
			throw new IllegalStateException("Deserialized Source");
		return new SubReference(
				this,
				position,
				length
		);
	}
}