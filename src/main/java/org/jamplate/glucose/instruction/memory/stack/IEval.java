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
package org.jamplate.glucose.instruction.memory.stack;

import org.jamplate.glucose.value.VQuote;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Values.unquote;
import static org.jamplate.glucose.internal.util.Values.value;

/**
 * An instruction that pops the top value at the stack and pushes the result of evaluating
 * it.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value]
 *     [..., result:text]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class IEval implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final IEval INSTANCE = new IEval();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 9194981107095992939L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the top value at the stack and pushes the
	 * result of evaluating it.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	public IEval() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the top value at the stack and pushes the
	 * result of evaluating it.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.24
	 */
	public IEval(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//pop the last value
		Value value0 = memory.pop();

		//result
		String text1 = value0.eval(memory);
		Value value1 = value(text1);
		Value value2 = value1 instanceof VQuote ?
					   unquote(value1) :
					   value1;

		//push the result
		memory.push(value2);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? IEval.INSTANCE : new IEval(new Tree(this.tree));
	}
}
