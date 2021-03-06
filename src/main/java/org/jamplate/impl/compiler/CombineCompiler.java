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
package org.jamplate.impl.compiler;

import org.jamplate.function.Compiler;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.Compilation;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A compiler that compiles using a pre-specified list of other compilers and then
 * combines the compiled instructions with a {@link Block} instruction.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.31
 */
public class CombineCompiler implements Compiler {
	/**
	 * The compilers used by this.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final List<Compiler> compilers;

	/**
	 * Construct a new compiler that compiles using the given {@code compilers} then
	 * combine the results with a {@link Block} instruction.
	 *
	 * @param compilers the compilers to be used by the constructed compiler.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.2.0 ~2021.05.31
	 */
	public CombineCompiler(@Nullable Compiler @NotNull ... compilers) {
		Objects.requireNonNull(compilers, "compilers");
		this.compilers = Arrays
				.stream(compilers)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compilers} then
	 * combine the results with a {@link Block} instruction.
	 *
	 * @param compilers the compilers to be used by the constructed compiler.
	 * @throws NullPointerException if the given {@code compiler} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	public CombineCompiler(@NotNull List<Compiler> compilers) {
		Objects.requireNonNull(compilers, "compilers");
		this.compilers = new ArrayList<>();
		for (Compiler compiler : compilers)
			if (compiler != null)
				this.compilers.add(compiler);
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compilers} and
	 * combine the answers in with a {@link Block}.
	 *
	 * @param compilers the compilers to be used.
	 * @return a compiler that combines the results of the given {@code compilers}.
	 * @throws NullPointerException if the given {@code compilers} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static CombineCompiler combine(@Nullable Compiler @NotNull ... compilers) {
		return new CombineCompiler(compilers);
	}

	/**
	 * Construct a new compiler that compiles using the given {@code compilers} and
	 * combine the answers in with a {@link Block}.
	 *
	 * @param compilers the compilers to be used.
	 * @return a compiler that combines the results of the given {@code compilers}.
	 * @throws NullPointerException if the given {@code compilers} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static CombineCompiler combine(@NotNull List<Compiler> compilers) {
		return new CombineCompiler(compilers);
	}

	@Nullable
	@Override
	public Instruction compile(@NotNull Compiler compiler, @NotNull Compilation compilation, @NotNull Tree tree) {
		Objects.requireNonNull(compiler, "compiler");
		Objects.requireNonNull(compilation, "compilation");
		Objects.requireNonNull(tree, "tree");
		List<Instruction> instructions = this.compilers
				.stream()
				.map(c -> c.compile(compiler, compilation, tree))
				.collect(Collectors.toList());

		return new Block(
				tree,
				instructions
		);
	}

	@NotNull
	@Override
	public Iterator<Compiler> iterator() {
		return Collections.unmodifiableList(this.compilers).iterator();
	}
}
