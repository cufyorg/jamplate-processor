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
import org.jetbrains.annotations.Nullable;

/**
 * An exception indicating that an illegal tree was provided.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.14
 */
public class IllegalTreeException extends IllegalArgumentException {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5393956633280978236L;

	/**
	 * The illegal tree.
	 *
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	protected Tree illegal;

	/**
	 * The primary tree.
	 *
	 * @since 0.2.0 ~2021.05.30
	 */
	@Nullable
	protected Tree primary;

	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to {@link
	 * #initCause}.
	 *
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException() {
	}

	/**
	 * Constructs a new exception with the specified detail message.  The cause is not
	 * initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later retrieval
	 *                by the {@link #getMessage()} method.
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.  <p>Note
	 * that the detail message associated with {@code cause} is <i>not</i> automatically
	 * incorporated in this exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the {@link
	 *                #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the {@link
	 *                #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *                indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the
	 * class and detail message of <tt>cause</tt>). This constructor is useful for
	 * exceptions that are little more than wrappers for other throwables (for example,
	 * {@link java.security.PrivilegedActionException}).
	 *
	 * @param cause the cause (which is saved for later retrieval by the {@link
	 *              #getCause()} method).  (A <tt>null</tt> value is permitted, and
	 *              indicates that the cause is nonexistent or unknown.)
	 * @since 0.2.0 ~2021.05.14
	 */
	public IllegalTreeException(@Nullable Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree.
	 *
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable Tree illegal) {
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code message}.
	 *
	 * @param message the message of the exception.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Tree illegal) {
		this(message);
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code message} and {@code cause}.
	 *
	 * @param message the message of the exception.
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Throwable cause, @Nullable Tree illegal) {
		this(message, cause);
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code cause}.
	 *
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable Throwable cause, @Nullable Tree illegal) {
		this(cause);
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree.
	 *
	 * @param primary the primary tree.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable Tree primary, @Nullable Tree illegal) {
		this.primary = primary;
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code message}.
	 *
	 * @param message the message of the exception.
	 * @param primary the primary tree.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Tree primary, @Nullable Tree illegal) {
		this(message);
		this.primary = primary;
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code message} and {@code cause}.
	 *
	 * @param message the message of the exception.
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param primary the primary tree.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable String message, @Nullable Throwable cause, @Nullable Tree primary, @Nullable Tree illegal) {
		this(message, cause);
		this.primary = primary;
		this.illegal = illegal;
	}

	/**
	 * Construct a new illegal tree exception with the given {@code illegal} tree and the
	 * given {@code cause}.
	 *
	 * @param cause   the throwable that caused to the construction of this exception.
	 * @param primary the primary tree.
	 * @param illegal the illegal tree.
	 * @since 0.2.0 ~2021.05.15
	 */
	public IllegalTreeException(@Nullable Throwable cause, @Nullable Tree primary, @Nullable Tree illegal) {
		this(cause);
		this.primary = primary;
		this.illegal = illegal;
	}

	/**
	 * Return the illegal tree that caused this exception to be thrown.
	 *
	 * @return the illegal tree. Or {@code null} if unknown or non-existing.
	 * @since 0.2.0 ~2021.05.15
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getIllegalTree() {
		return this.illegal;
	}

	/**
	 * Return the primary tree where the illegal tree was detected.
	 *
	 * @return the primary tree. Or {@code null} if unknown or non-existing.
	 * @since 0.2.0 ~2021.05.30
	 */
	@Nullable
	@Contract(pure = true)
	public Tree getPrimaryTree() {
		return this.primary;
	}
}
