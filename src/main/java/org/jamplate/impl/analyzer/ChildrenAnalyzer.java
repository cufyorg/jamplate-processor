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
package org.jamplate.impl.analyzer;

import org.jamplate.function.Analyzer;
import org.jamplate.util.Trees;
import org.jamplate.model.Compilation;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * An analyzer that analyzes the direct children of the trees given to it using a
 * pre-specified analyzer.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class ChildrenAnalyzer implements Analyzer {
	/**
	 * The analyzer to be used by this.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	protected final Analyzer analyzer;

	/**
	 * Construct a new analyzer that analyzes the direct children of the trees given to it
	 * using the given {@code analyzer}.
	 *
	 * @param analyzer the analyzer to be used.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.3.0 ~2021.06.27
	 */
	public ChildrenAnalyzer(@NotNull Analyzer analyzer) {
		Objects.requireNonNull(analyzer, "analyzer");
		this.analyzer = analyzer;
	}

	/**
	 * Construct a new analyzer that analyzes the direct children of the trees given to it
	 * using the given {@code analyzer}.
	 *
	 * @param analyzer the analyzer to be used.
	 * @return a new children analyzer that uses the given {@code analyzer}.
	 * @throws NullPointerException if the given {@code analyzer} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ChildrenAnalyzer children(@NotNull Analyzer analyzer) {
		return new ChildrenAnalyzer(analyzer);
	}

	@Override
	public boolean analyze(@NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");

		boolean modified = false;

		for (Tree child : Trees.children(tree))
			modified |= this.analyzer.analyze(compilation, child);

		return modified;
	}

	@NotNull
	@Override
	public Iterator<Analyzer> iterator() {
		return Collections.singleton(this.analyzer).iterator();
	}
}
