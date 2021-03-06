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
package org.jamplate.impl.document;

import org.jamplate.model.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A pseudo document that get made programmatically.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class PseudoDocument implements Document {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5906973056576376713L;

	/**
	 * The content of this document.
	 *
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	protected final CharSequence content;
	/**
	 * The identifier of this document.
	 *
	 * @since 0.2.0 ~2021.02.17
	 */
	@NotNull
	protected final String identifier;

	/**
	 * Construct a new pseudo document that has no content neither can be identified.
	 *
	 * @since 0.2.0 ~2021.02.19
	 */
	public PseudoDocument() {
		this("");
	}

	/**
	 * Construct a new pseudo document that have the given {@code content}. The identifier
	 * of the constructed document will be the string in hex of the {@link
	 * System#identityHashCode(Object) identitiy hash code} of the given content.
	 * <br>
	 * Important Note: do not provide modifiable content (such as StringBuilder). For the
	 * sake of performance and compatibility, this class will simply give its content
	 * instance to anyone requests it. So, due to that. The provided content might get
	 * changed in value while it suppose to be immutable.
	 *
	 * @param content the content of the constructed pseudo content.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(@NotNull CharSequence content) {
		Objects.requireNonNull(content, "content");
		this.identifier = Integer.toHexString(System.identityHashCode(content));
		this.content = content;
	}

	/**
	 * Construct a new pseudo document that have the given {@code content} and be
	 * identified with the given {@code identifier}.
	 * <br>
	 * Important Note: do not provide modifiable content (such as StringBuilder). For the
	 * sake of performance and compatibility, this class will simply give its content
	 * instance to anyone requests it. So, due to that. The provided content might get
	 * changed in value while it suppose to be immutable.
	 *
	 * @param identifier the identity of the constructed document.
	 * @param content    the content of the constructed pseudo content.
	 * @throws NullPointerException if the given {@code identifier} or {@code content} is
	 *                              null.
	 * @since 0.2.0 ~2021.01.13
	 */
	public PseudoDocument(@NotNull String identifier, @NotNull CharSequence content) {
		Objects.requireNonNull(identifier, "identifier");
		Objects.requireNonNull(content, "content");
		this.identifier = identifier;
		this.content = content;
	}

	/**
	 * Construct a new pseudo document that have the given {@code lines} and be identified
	 * with the given {@code identifier}.
	 *
	 * @param identifier the identity of the constructed document.
	 * @param lines      the lines of the constructed pseudo content.
	 * @throws NullPointerException if the given {@code identifier} or {@code lines} is
	 *                              null.
	 * @since 0.3.0 ~2021.07.03
	 */
	public PseudoDocument(@NotNull String identifier, @Nullable CharSequence @NotNull ... lines) {
		Objects.requireNonNull(identifier, "identifier");
		Objects.requireNonNull(lines, "lines");
		this.identifier = identifier;
		this.content = Arrays
				.stream(lines)
				.map(line -> Objects.toString(line, ""))
				.collect(Collectors.joining("\n"));
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Document) {
			Document document = (Document) object;

			return Objects.equals(document.toString(), this.identifier);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(this.content.toString().getBytes());
	}

	@NotNull
	@Override
	public Reader openReader() {
		return new StringReader(this.content.toString());
	}

	@NotNull
	@Override
	public CharSequence read() {
		return this.content;
	}

	@NotNull
	@Override
	public String toString() {
		return this.identifier;
	}
}
