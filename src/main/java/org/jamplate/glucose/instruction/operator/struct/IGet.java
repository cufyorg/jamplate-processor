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
package org.jamplate.glucose.instruction.operator.struct;

import org.jamplate.glucose.value.VArray;
import org.jamplate.glucose.value.VNumber;
import org.jamplate.glucose.value.VObject;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Structs.get;
import static org.jamplate.glucose.internal.util.Values.number;

/**
 * An instruction that pops the top two values in the stack and pushes the property in the
 * second popped value at the first popped value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*|array*|object*, key:value*|number*]
 *     [..., result:value*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class IGet implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@NotNull
	public static final IGet INSTANCE = new IGet();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6807005239681352973L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.13
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @since 0.3.0 ~2021.06.12
	 */
	public IGet() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values and pushes the property
	 * in the second popped value at the first popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.13
	 */
	public IGet(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//right
		Value value0 = memory.pop();
		//left
		Value value1 = memory.pop();

		if (value1 instanceof VObject) {
			//left
			VObject object1 = (VObject) value1;

			//result
			Value value3 = get(
					object1,
					value0
			);

			memory.push(value3);
			return;
		}
		if (value1 instanceof VArray) {
			//right
			VNumber number0 = number(value0);
			//left
			VArray array1 = (VArray) value1;

			//result
			Value value3 = get(
					array1,
					number0
			);

			memory.push(value3);
			return;
		}

		memory.push(Value.NULL);
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? IGet.INSTANCE : new IGet(new Tree(this.tree));
	}
}
