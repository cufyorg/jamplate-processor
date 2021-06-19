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

import org.jamplate.model.function.Analyzer;
import org.jamplate.model.function.Compiler;
import org.jamplate.model.function.Parser;
import org.jamplate.model.function.Processor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A spec is a unit containing functions necessary to apply a syntax/runtime
 * specification.
 * <br>
 * Every spec can have sub-specs that it enclosing it. Every spec must specify how the
 * sub-specs are treated inside it. Otherwise, the spec must not support sub-specs.
 * <br>
 * Other than registering sub-specs, the user of a spec must not query nor modify the
 * sub-specs since the spec already delegates to the sub-specs its own way.
 * <br>
 * For a spec to support sub-specs, it must implement the methods {@link #iterator()},
 * {@link #removeSpec(Spec)} and {@link #hasSpec(Spec)}.
 * <br>
 * For a spec to support variable sub-specs, it must implement the methods {@link
 * #addSpec(Spec)}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.16
 */
public interface Spec extends Iterable<Spec> {
	/**
	 * Return an iterator iterating over the subspecs in this spec.
	 *
	 * @return an iterator iterating over the subspecs in this spec.
	 * @implSpec this implementation returns an empty iterator.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	default Iterator<Spec> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Add the given {@code spec} as a subspec to this spec.
	 *
	 * @param spec the spec to be added.
	 * @return true, if the spec was not already a subspec in this spec.
	 * @throws NullPointerException          if the given {@code spec} is null.
	 * @throws UnsupportedOperationException if this spec does not allow sub-specs.
	 * @implSpec this implementation will throw {@link UnsupportedOperationException}
	 * 		immediately.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean addSpec(@NotNull Spec spec) {
		throw new UnsupportedOperationException("addSpec");
	}

	/**
	 * Return an (possibly unmodifiable view) set of the processors that must be executed
	 * before the analyzing stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the pre-analyzing processors to apply this
	 * 		spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Processor> getAnalyzeProcessors() {
		return Collections.emptySet();
	}

	/**
	 * Return an (possibly unmodifiable view) set of the analyzers that must be executed
	 * at the analyzing stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the analyzers to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Analyzer> getAnalyzers() {
		return Collections.emptySet();
	}

	/**
	 * Return an (possibly unmodifiable view) set of the processors that must be executed
	 * before the compiling stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the pre-compiling processors to apply this
	 * 		spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Processor> getCompileProcessors() {
		return Collections.emptySet();
	}

	/**
	 * Return an (possibly unmodifiable view) set of the compilers that must be executed
	 * at the compiling stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the compilers to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Compiler> getCompilers() {
		return Collections.emptySet();
	}

	/**
	 * Return an (possibly unmodifiable view) set of the processors that must be executed
	 * before the parsing stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the pre-parsing processors to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Processor> getParseProcessors() {
		return Collections.emptySet();
	}

	/**
	 * Return an (possibly unmodifiable view) set of the parsers that must be executed at
	 * the parsing stage to apply this specification.
	 *
	 * @return an unmodifiable view set of the parsers to apply this spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<Parser> getParsers() {
		return Collections.emptySet();
	}

	/**
	 * Return {@code true} if this spec has the given subspec {@code spec}.
	 *
	 * @param spec the subspec to check if in this spec.
	 * @return true, if this spec has the given {@code spec} as a subspec.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @implSpec this implementation iterate using {@link #iterator()} and checks by
	 * 		identity.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(pure = true)
	default boolean hasSpec(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		for (Spec value : this)
			if (value == spec)
				return true;
		return false;
	}

	/**
	 * Remove the given subspec {@code spec} from this spec.
	 *
	 * @param spec the subspec to be removed.
	 * @return true, if the given {@code spec} was a subspec in this spec.
	 * @throws NullPointerException if the given {@code spec} is null.
	 * @implSpec this implementation uses {@link #iterator()} to remove the spec.
	 * @since 0.3.0 ~2021.06.19
	 */
	@Contract(mutates = "this")
	default boolean removeSpec(@NotNull Spec spec) {
		Objects.requireNonNull(spec, "spec");
		Iterator<Spec> iterator = this.iterator();
		while (iterator.hasNext())
			if (iterator.next() == spec) {
				iterator.remove();
				return true;
			}
		return false;
	}
}