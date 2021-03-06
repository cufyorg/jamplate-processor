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
package org.jamplate.glucose.spec.command.hashwhile;

import org.jamplate.glucose.instruction.memory.frame.IPopFrame;
import org.jamplate.unit.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.flow.IRepeat;
import org.jamplate.glucose.instruction.memory.frame.IDumpFrame;
import org.jamplate.glucose.instruction.memory.frame.IGlueFrame;
import org.jamplate.glucose.instruction.memory.frame.IPushFrame;
import org.jamplate.glucose.instruction.memory.stack.IPop;
import org.jamplate.glucose.instruction.operator.cast.ICastBoolean;
import org.jamplate.glucose.spec.element.CommandSpec;
import org.jamplate.glucose.spec.element.FlowSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.glucose.internal.analyzer.BinaryFlowAnalyzer.flow;
import static org.jamplate.util.Functions.analyzer;
import static org.jamplate.util.Functions.compiler;
import static org.jamplate.util.Query.*;

/**
 * While flow specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings("OverlyCoupledMethod")
public class FlowWhileSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final FlowWhileSpec INSTANCE = new FlowWhileSpec();

	/**
	 * The kind of a while flow.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final String KIND = "flow:while";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = FlowWhileSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				//analyze the whole hierarchy
				a -> hierarchy(a),
				//target unprocessed valid trees
				a -> filter(a, and(
						//must not have been processed before
						not(FlowWhileSpec.KIND),
						child(or(
								//any #while
								is(HashWhileSpec.KIND),
								//any #endwhile
								is(HashEndwhileSpec.KIND)
						))
				)),
				//analyze
				a -> flow(
						//start command predicate
						is(HashWhileSpec.KIND),
						//end command predicate
						is(HashEndwhileSpec.KIND),
						//flow wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(FlowWhileSpec.KIND),
								FlowSpec.WEIGHT
						),
						//start command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_START,
								t.getSketch()
						),
						//end command constructor
						(w, t) -> w.getSketch().set(
								FlowSpec.KEY_END,
								t.getSketch()
						),
						//body wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.getDocument(),
								r,
								w.getSketch()
								 .get(FlowSpec.KEY_BODY),
								FlowSpec.WEIGHT_BODY
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target while flow
				c -> filter(c, is(FlowWhileSpec.KIND)),
				//compile
				c -> (compiler, compilation, tree) -> {
					//gather component trees
					Tree startT = tree.getSketch().get(FlowSpec.KEY_START).getTree();
					Tree endT = tree.getSketch().get(FlowSpec.KEY_END).getTree();
					Tree bodyT = tree.getSketch().get(FlowSpec.KEY_BODY).getTree();

					//check required component trees
					if (startT == null || endT == null || bodyT == null)
						throw new CompileException(
								"Flow WHILE is missing some components",
								tree
						);

					//gather subcomponent trees
					Tree valueT = startT.getSketch().get(CommandSpec.KEY_VALUE).getTree();

					//check required subcomponent trees
					if (valueT == null)
						throw new CompileException(
								"Command WHILE is missing some components",
								startT
						);

					//compile the value
					Instruction valueI = compiler.compile(
							compiler,
							compilation,
							valueT
					);
					//compile the body
					Instruction bodyI = compiler.compile(
							compiler,
							compilation,
							bodyT
					);

					//validate the compiled value
					if (valueI == null)
						//the value must be compiled
						throw new CompileException(
								"Unrecognized token",
								valueT
						);
					//validate subcomponents
					if (bodyI == null)
						//the body must be compiled
						throw new CompileException(
								"Unrecognized token",
								bodyT
						);

					//value sandbox
					Instruction valueWrapI = new Block(
							tree,
							//push a new frame
							new IPushFrame(valueT),
							//run the condition
							valueI,
							//glue the answer
							new IGlueFrame(tree),
							//cast the answer to boolean
							new ICastBoolean(tree),
							//dump the frame
							new IDumpFrame(tree)
					);

					//compile as a simple REPEAT
					return new Block(
							tree,
							//evaluate the value for the first round
							valueWrapI,
							//repeat while the answer evaluate to true
							new IRepeat(tree, new Block(
									//push body frame
									new IPushFrame(tree),
									//execute the body
									bodyI,
									//pop body frame
									new IPopFrame(tree),
									//evaluate the value for the next round
									valueWrapI
							)),
							//pop the original value
							new IPop(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return FlowWhileSpec.NAME;
	}
}
