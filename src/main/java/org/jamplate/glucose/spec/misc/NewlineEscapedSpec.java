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
package org.jamplate.glucose.spec.misc;

import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.memory.heap.IAlloc;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.internal.memory.Address;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.unit.Spec;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.parser.TermParser.term;
import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.glucose.internal.util.Values.text;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Functions.parser;
import static org.jamplate.util.Query.is;
import static org.jamplate.util.Source.line;

/**
 * Escaped line separators {@code \\\n}, {@code \\\r}, {@code \\\r\n} spec.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.27
 */
public class NewlineEscapedSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.0.0 ~2021.06.27
	 */
	@NotNull
	public static final NewlineEscapedSpec INSTANCE = new NewlineEscapedSpec();

	/**
	 * The kind of a escaped line separator trees.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final String KIND = "newline:escaped";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.27
	 */
	@NotNull
	public static final String NAME = NewlineEscapedSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target escaped newlines
				c -> filter(c, is(NewlineEscapedSpec.KIND)),
				//compile the escaped newlines
				c -> (compiler, compilation, tree) -> {
					//determine the line number of the next line
					int line = line(tree) + 1;

					return new Block(
							//Define __LINE__
							new IPushConst(tree, text(Address.LINE)),
							new IPushConst(tree, number(line)),
							new IAlloc(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		//parse only on the first round
		return parser(
				p -> term(
						"\\\\(?:\r\n|\r|\n)",
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(NewlineEscapedSpec.KIND)
						)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return NewlineEscapedSpec.NAME;
	}
}
