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
import org.jamplate.glucose.value.VQuote;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Environment;
import org.jamplate.model.Instruction;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.jamplate.glucose.internal.util.Structs.put;
import static org.jamplate.glucose.internal.util.Structs.set;
import static org.jamplate.glucose.internal.util.Values.*;

/**
 * An instruction that pops the top three values in the stack and put the first popped
 * value in the third popped value at the key resultant from evaluating the second popped
 * value.
 * <br><br>
 * Memory Visualization:
 * <pre>
 *     [..., param:value*|array*|object*, key:value*|number*, value:value*]
 *     [..., result:array*|object*]
 * </pre>
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class IPut implements Instruction {
	/**
	 * An instance of this instruction.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final IPut INSTANCE = new IPut();

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4102904984181017768L;

	/**
	 * A reference of this instruction in the source code.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@Nullable
	protected final Tree tree;

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the heap at the result of evaluating the second
	 * popped value.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	public IPut() {
		this.tree = null;
	}

	/**
	 * Construct a new instruction that pops the last two values in the stack and
	 * allocates the first popped value to the heap at the result of evaluating the second
	 * popped value.
	 *
	 * @param tree a reference for the constructed instruction in the source code.
	 * @throws NullPointerException if the given {@code tree} is null.
	 * @since 0.3.0 ~2021.06.15
	 */
	public IPut(@NotNull Tree tree) {
		Objects.requireNonNull(tree, "tree");
		this.tree = tree;
	}

	@Override
	public void exec(@NotNull Environment environment, @NotNull Memory memory) {
		Objects.requireNonNull(environment, "environment");
		Objects.requireNonNull(memory, "memory");

		//value
		Value value0 = memory.pop();
		//key
		Value value1 = memory.pop();
		//struct
		Value value2 = memory.pop();

		//value
		VQuote quote0 = quote(value0);

		if (!(value2 instanceof VObject) && value1 instanceof VNumber) {
			//key
			VNumber number1 = (VNumber) value1;
			//struct
			VArray array2 = array(value2);

			//result
			VArray array3 = set(
					array2,
					number1,
					quote0
			);

			memory.push(array3);
		} else {
			//key
			VQuote quote1 = quote(value1);
			//struct
			VObject object2 = object(value2);

			//result
			VObject object3 = put(
					object2,
					quote1,
					quote0
			);

			memory.push(object3);
		}
	}

	@Nullable
	@Override
	public Tree getTree() {
		return this.tree;
	}

	@NotNull
	@Override
	public Instruction optimize(int mode) {
		return mode < 0 ? IPut.INSTANCE : new IPut(new Tree(this.tree));
	}
}
