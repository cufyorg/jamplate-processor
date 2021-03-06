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
package org.jamplate.glucose.spec.command.hashcapture;

import org.jamplate.unit.Spec;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.model.CompileException;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.parser.GroupParser.group;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Functions.parser;
import static org.jamplate.util.Query.is;

/**
 * The specification of the command {@code #endcapture}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.03
 */
public class HashEndcaptureSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final HashEndcaptureSpec INSTANCE = new HashEndcaptureSpec();

	/**
	 * The kind of the {@code #endcapture} command.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final String KIND = "command:endcapture";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.03
	 */
	@NotNull
	public static final String NAME = HashEndcaptureSpec.class.getSimpleName();

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target endcapture commands that has not been compiled by a context
				c -> filter(c, is(HashEndcaptureSpec.KIND)),
				//throw
				c -> (compiler, compilation, tree) -> {
					throw new CompileException(
							"#Endcapture without #Capture",
							tree
					);
				}
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return parser(
				p -> group(
						//the pattern
						"[\t ]*(#)((?i)endcapture)\\s*",
						//constructor (whole match)
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(HashEndcaptureSpec.KIND),
								CommandSpec.WEIGHT
						),
						//anchor constructor (1st group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_OPEN)
								 .setKind(AnchorSpec.KIND_OPEN)
						)),
						//type constructor (2st group)
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(CommandSpec.KEY_TYPE)
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return HashEndcaptureSpec.NAME;
	}
}
