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
package org.jamplate.function;

import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * A function that takes a compilation and a sketch and parses new sketches from them.
 * <br><br>
 * <strong>Members</strong>
 * <ul>
 *     <li>{@link Parser}[]</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
@FunctionalInterface
public interface Parser extends Iterable<Parser> {
	/**
	 * A parser that parses nothing.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	Parser IDLE = new Parser() {
		@NotNull
		@Override
		public Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree) {
			return Collections.emptySet();
		}

		@Override
		public String toString() {
			return "Parser.IDLE";
		}
	};

	/**
	 * Return an immutable iterator iterating over sub-parsers of this parser.
	 *
	 * @return an iterator iterating over the sub-parsers of this parser.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Override
	default Iterator<Parser> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Parse the given {@code sketch} with respect to the given {@code compilation}.
	 *
	 * @param compilation the compilation taking the given {@code sketch}.
	 * @param tree        the sketch to be parsed.
	 * @return the parsed sketches.
	 * @throws NullPointerException if the given {@code compilation} or {@code node} is
	 *                              null.
	 * @throws IOError              if any I/O error occurs.
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	Set<Tree> parse(@NotNull Compilation compilation, @NotNull Tree tree);
}
