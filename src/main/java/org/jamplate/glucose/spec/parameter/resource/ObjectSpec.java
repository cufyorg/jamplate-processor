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
package org.jamplate.glucose.spec.parameter.resource;

import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.operator.cast.IBuildObject;
import org.jamplate.glucose.instruction.operator.cast.ICastGlue;
import org.jamplate.glucose.instruction.operator.cast.ICastPair;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracesSpec;
import org.jamplate.glucose.spec.syntax.symbol.CommaSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.instruction.Idle;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.analyzer.ChildrenAnalyzer.children;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.impl.compiler.FirstCompileCompiler.first;
import static org.jamplate.glucose.internal.analyzer.SeparatorsAnalyzer.separators;
import static org.jamplate.glucose.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.*;

/**
 * Parameter object specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings("OverlyCoupledMethod")
public class ObjectSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final ObjectSpec INSTANCE = new ObjectSpec();
	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ObjectSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//search in the whole hierarchy
				a -> hierarchy(a),
				//target braces
				a -> filter(a, is(BracesSpec.KIND)),
				//foreach child
				a -> children(a),
				//target the body
				a -> filter(a, and(
						//target the body
						is(AnchorSpec.KIND_BODY),
						//skip if already separated
						child(is(AnchorSpec.KIND_SLOT)).negate()
				)),
				//separate
				a -> separators(
						//separator predicate
						is(CommaSpec.KIND),
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								new Sketch(AnchorSpec.KIND_SLOT),
								AnchorSpec.WEIGHT_SLOT
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target braces
				c -> filter(c, is(BracesSpec.KIND)),
				//compile the whole context
				c -> (compiler, compilation, tree) ->
						//object sandbox
						new Block(
								tree,
								//push a frame to encapsulate the content of the object
								new IPushFrame(tree),
								//execute inner parts
								c.compile(
										compiler,
										compilation,
										tree
								),
								//glue the frame values
								new IGlueFrame(tree),
								//in case single value
								new ICastGlue(tree),
								//build an object from the glued value
								new IBuildObject(tree),
								//dump the frame
								new IDumpFrame(tree)
						),
				//flatten parts
				c -> flatten(c),
				//compile
				c -> first(
						//compile the anchors
						compiler(
								//target open and close anchors
								cc -> filter(cc, or(
										is(AnchorSpec.KIND_OPEN),
										is(AnchorSpec.KIND_CLOSE)
								)),
								//compile
								cc -> (compiler, compilation, tree) ->
										new Idle(tree)
						),
						//compile the body
						compiler(
								//target body
								cc -> filter(cc, is(AnchorSpec.KIND_BODY)),
								//flatten body parts
								cc -> flatten(cc),
								//compile
								cc -> first(
										//compile the commas
										compiler(
												//target commas
												ccc -> filter(ccc, is(CommaSpec.KIND)),
												//compile
												ccc -> (compiler, compilation, tree) ->
														new Idle(tree)
										),
										//compile the slots
										compiler(
												//target slots
												ccc -> filter(ccc, is(AnchorSpec.KIND_SLOT)),
												//compile
												ccc -> (compiler, compilation, tree) ->
														//pair sandbox
														new Block(
																tree,
																//compile the parts
																ccc.compile(
																		compiler,
																		compilation,
																		tree
																),
																//cast to pair
																new ICastPair(tree)
														),
												//flatten slots parts
												ccc -> flatten(ccc),
												//compile
												ccc -> fallback()
										)
								)
						)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ObjectSpec.NAME;
	}
}
