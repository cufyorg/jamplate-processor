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
package org.jamplate.glucose.instruction.operator.cast;

import org.jamplate.glucose.value.VGlue;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.glue;

/**
 * An instruction that pops the last value in the stack and pushes a value that evaluate
 * to the popped value interpreted as a {@link VGlue}.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*]
 *     [..., result:glue*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.06
 */
public class ICastGlue implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@NotNull
	public static final ICastGlue INSTANCE = new ICastGlue();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4663666280068528631L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as a {@link VGlue}.
	 *
	 * @since 0.3.0 ~2021.07.06
	 */
	public ICastGlue() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last value in the stack and pushes a
	 * value that evaluate to the popped value interpreted as a {@link VGlue}.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.07.06
	 */
	public ICastGlue(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		Value value0 = memory.pop();
		Value value1 = glue(value0);

		memory.push(value1);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? ICastGlue.INSTANCE : new ICastGlue(new Tree(this.tree));
	}
}
