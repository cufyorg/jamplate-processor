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
package org.jamplate.model.source;

/**
 * An implementation of the interface {@link Source} that takes a whole {@link Document}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
public class SourceRoot extends AbstractSource {
	/**
	 * Construct a new source standard implementation instance for the given {@code
	 * document}.
	 *
	 * @param document the document.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @see AbstractSource#AbstractSource(Document)
	 * @since 0.2.0 ~2021.01.13
	 */
	public SourceRoot(Document document) {
		super(document);
	}

	@Override
	public Source slice(int position) {
		return new SourceSlice(
				this,
				position,
				this.length() - position
		);
	}

	@Override
	public Source slice(int position, int length) {
		return new SourceSlice(
				this,
				position,
				length
		);
	}
}