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
package org.jamplate.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;

/**
 * An interface that abstracts the functionality required to deal with source-code files.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public interface Document extends Serializable {
	/**
	 * Open a new input-stream that reads the content at the given {@code reference} in
	 * this document.
	 *
	 * @param reference the range to be read.
	 * @return a new input-stream that reads the content of this document.
	 * @throws IOException               if any I/O exception occurs.
	 * @throws DocumentNotFoundException if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	default InputStream openInputStream(@NotNull Reference reference) throws IOException {
		Objects.requireNonNull(reference, "reference");
		//noinspection ALL
		return new InputStream() {
			/**
			 * The position where to start the stream.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final int start = reference.position();
			/**
			 * The position where to stop the stream.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final int end = this.start + reference.length();
			/**
			 * The original stream.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final InputStream stream = Document.this.openInputStream();
			/**
			 * The number of bytes read.
			 *
			 * @since 0.2.0 ~2021.02.15
			 */
			private volatile long i = this.stream.skip(this.start);

			@Override
			public int available() throws IOException {
				//the minimist of either the reference remaining or actual remaining
				//noinspection NumericCastThatLosesPrecision
				return (int) Math.min(
						this.end - Math.max(this.i, this.start),
						Math.min(0, this.stream.available() - Math.max(0,
								this.start - this.i))
				);
			}

			@Override
			public void close() throws IOException {
				this.stream.close();
			}

			@Override
			public synchronized void mark(int limit) {
				//add the number of the remaining bytes until the start. (if any)
				//noinspection NumericCastThatLosesPrecision
				this.stream.mark(Math.max(0, (int) (this.start - this.i)) + limit);
			}

			@Override
			public boolean markSupported() {
				return this.stream.markSupported();
			}

			@Override
			public synchronized int read(byte[] buffer, int off, int len) throws IOException {
				Objects.requireNonNull(buffer, "buffer");
				if (off < 0 || len < 0 || off + len > buffer.length)
					//noinspection NewExceptionWithoutArguments
					throw new IndexOutOfBoundsException();

				//can skip the whole operation? (optional)
				if (len == 0)
					return 0;

				//skip if skip needed (optional)
				long skip = this.start - this.i;
				if (skip > 0)
					this.i += this.stream.skip(skip);

				//reached the start?
				if (this.i >= this.start) {
					//read, but stop before the end
					int read = this.stream.read(
							buffer,
							off,
							Math.min((int) (this.end - this.i), len)
					);

					//something has been read?
					if (read > 0)
						this.i += read;

					return read;
				}

				//still has not reached the start :P
				return 0;
			}

			@Override
			public synchronized int read() throws IOException {
				while (true) {
					//reached the end?
					if (this.i >= this.end)
						return -1;

					//skip if skip needed (optional)
					long skip = this.start - this.i;
					if (skip > 0)
						this.i += this.stream.skip(skip);

					//go ahead, still has not reached the reference limit
					int r = this.stream.read();

					//reached the actual end?
					if (r == -1)
						return -1;

					//reached the start?
					if (++this.i > this.start)
						//yup, lessgo
						return r;
				}
			}

			@Override
			public synchronized void reset() throws IOException {
				this.stream.reset();
			}

			@Override
			public synchronized long skip(long n) throws IOException {
				//limit the amount to the remaining bytes until the end
				long skipped = this.stream.skip(Math.min(this.end - this.i, n));

				if (skipped > 0)
					this.i += skipped;

				return skipped;
			}
		};
	}

	/**
	 * Open a new reader that reads the content at the given {@code reference} in this
	 * document.
	 *
	 * @param reference the range to be read.
	 * @return a new reader that reads the content of this document.
	 * @throws IOException               if any I/O exception occurs. (optional)
	 * @throws DocumentNotFoundException if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	default Reader openReader(@NotNull Reference reference) throws IOException {
		Objects.requireNonNull(reference, "reference");
		//noinspection ALL
		return new Reader() {
			/**
			 * The original reader.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final Reader reader = Document.this.openReader();
			/**
			 * The position to start.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final int start = reference.position();
			/**
			 * The position to stop.
			 *
			 * @since 0.2.0 ~2021.02.17
			 */
			private final int end = this.start + reference.length();
			/**
			 * The number of bytes read.
			 *
			 * @since 0.2.0 ~2021.02.15
			 */
			private volatile long i = this.reader.skip(this.start);

			@Override
			public void close() throws IOException {
				this.reader.close();
			}

			@Override
			public void mark(int limit) throws IOException {
				//add the number of the remaining bytes until the start. (if any)
				//noinspection NumericCastThatLosesPrecision
				this.reader.mark(Math.max(0, (int) (this.start - this.i)) + limit);
			}

			@Override
			public boolean markSupported() {
				return this.reader.markSupported();
			}

			@Override
			public synchronized int read() throws IOException {
				while (true) {
					//reached the end?
					if (this.i >= this.end)
						return -1;

					//skip if skip needed (optional)
					long skip = this.start - this.i;
					if (skip > 0)
						this.i += this.reader.skip(skip);

					//go ahead, still has not reached the reference limit
					int r = this.reader.read();

					//reached the actual end?
					if (r == -1)
						return -1;

					//reached the start?
					if (++this.i > this.start)
						//yup, lessgo
						return r;
				}
			}

			@Override
			public synchronized int read(char[] buffer, int off, int len) throws IOException {
				Objects.requireNonNull(buffer, "buffer");
				if (off < 0 || len < 0 || off + len > buffer.length)
					//noinspection NewExceptionWithoutArguments
					throw new IndexOutOfBoundsException();

				//can skip the whole operation? (optional)
				if (len == 0)
					return 0;

				//skip if skip needed (optional)
				long skip = this.start - this.i;
				if (skip > 0)
					this.i += this.reader.skip(skip);

				//reached the start?
				if (this.i >= this.start) {
					//read, but stop before the end
					int read = this.reader.read(
							buffer,
							off,
							Math.min((int) (this.end - this.i), len)
					);

					//something has been read?
					if (read > 0)
						this.i += read;

					return read;
				}

				//still has not reached the start :P
				return 0;
			}

			@Override
			public boolean ready() throws IOException {
				return this.reader.ready();
			}

			@Override
			public void reset() throws IOException {
				this.reader.reset();
			}

			@Override
			public synchronized long skip(long n) throws IOException {
				//limit the amount to the remaining bytes until the end
				long skipped = this.reader.skip(Math.min(this.end - this.i, n));

				if (skipped > 0)
					this.i += skipped;

				return skipped;
			}
		};
	}

	/**
	 * Read the content at the given {@code reference} in this document.
	 *
	 * @param reference the range to be read.
	 * @return the content of this document. (unmodifiable view)
	 * @throws IOError               if any I/O exception occurs. (optional)
	 * @throws DocumentNotFoundError if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	default CharSequence read(@NotNull Reference reference) {
		Objects.requireNonNull(reference, "reference");
		int p = reference.position();
		int t = p + reference.length();
		return this.read()
				   .subSequence(p, t);
	}

	/**
	 * Determines if the given {@code object} equals this document or not. An object
	 * equals a document when that object is pointing to the same source as this document.
	 * (regardless of its content, assuming the user is honest and does not provide two
	 * documents with same source but from different origins or have different content)
	 * <pre>
	 *     equals = object instanceof Document &&
	 *     			this.toString().equals(object.toString())
	 * </pre>
	 *
	 * @param object the object to be matched.
	 * @return true, if the given {@code object} is a document and equals this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Contract(value = "null->false", pure = true)
	@Override
	boolean equals(@Nullable Object object);

	/**
	 * Calculate the hash code of this document. The hash code of a document is the hashes
	 * of the variables related to its source (like drive, path, name, module, etc..). The
	 * implementer is totally free to implement its own algorithm. As long as it is a
	 * result from calculating uniq variables about its source.
	 * <pre>
	 *     hashCode = &lt;SourceUniqIdentifiers&gt;
	 * </pre>
	 *
	 * @return the hash code of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@Contract(pure = true)
	@Override
	int hashCode();

	/**
	 * Returns a string representation of this document. The string representation of a
	 * document is the name of its source.
	 * <pre>
	 *     toString = &lt;SourceUniqName&gt;
	 * </pre>
	 *
	 * @return a string representation of this document.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Open a new input-stream that reads the content of this document.
	 *
	 * @return a new input-stream that reads the content of this document.
	 * @throws IOException               if any I/O exception occurs.
	 * @throws DocumentNotFoundException if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	InputStream openInputStream() throws IOException;

	/**
	 * Open a new reader that reads the content of this document.
	 *
	 * @return a new reader that reads the content of this document.
	 * @throws IOException               if any I/O exception occurs. (optional)
	 * @throws DocumentNotFoundException if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	Reader openReader() throws IOException;

	/**
	 * Read the content of this document.
	 *
	 * @return the content of this document. (unmodifiable view)
	 * @throws IOError               if any I/O exception occurs. (optional)
	 * @throws DocumentNotFoundError if this document is not available for reading.
	 * @since 0.2.0 ~2021.01.13
	 */
	@NotNull
	@Contract(pure = true)
	CharSequence read();
}
