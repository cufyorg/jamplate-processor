package org.jamplate.glucose.spec.parameter.operator;

import org.jamplate.unit.Unit;
import org.jamplate.glucose.instruction.memory.resource.IPushConst;
import org.jamplate.glucose.instruction.operator.cast.ICastBoolean;
import org.jamplate.glucose.instruction.operator.logic.ICompare;
import org.jamplate.glucose.instruction.operator.logic.INegate;
import org.jamplate.glucose.spec.document.LogicSpec;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.parameter.resource.NumberSpec;
import org.jamplate.glucose.spec.syntax.symbol.CloseChevronSpec;
import org.jamplate.glucose.spec.syntax.symbol.MinusSpec;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.tool.DebugSpec;
import org.jamplate.impl.unit.Action;
import org.jamplate.impl.unit.UnitImpl;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.environment.EnvironmentImpl;
import org.jamplate.impl.document.PseudoDocument;
import org.jamplate.memory.Memory;
import org.jamplate.memory.Value;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.util.Specs.listener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MoreThanSpecTest {
	@Test
	public void manualAssembly0() {
		for (int i = -2; i < 3; i++)
			for (int j = -2; j < 3; j++) {
				int left = i;
				int right = j;
				boolean result = left > right;

				Environment environment = new EnvironmentImpl();
				Memory memory = new Memory();
				new Block(
						(env, mem) -> mem.push(number(left)),
						(env, mem) -> mem.push(number(right)),
						//compare the values
						ICompare.INSTANCE,
						//push `1` to compare the comparison result
						new IPushConst(number(1)),
						//compare the comparison result with `1`
						ICompare.INSTANCE,
						//cast the result to boolean
						ICastBoolean.INSTANCE,
						//invert the results
						INegate.INSTANCE
				).exec(environment, memory);

				Value value = memory.pop();
				String text = value.eval(memory);

				assertEquals(
						String.valueOf(result),
						text,
						left + " > " + right + " is expected to be " + result +
						" but got " +
						text
				);
			}
	}

	@Test
	public void test0() {
		for (int i : new int[]{-2, -1, 0, 1, 2})
			for (int j : new int[]{-2, -1, 0, 1, 2}) {
				Document document = new PseudoDocument(i + ">" + j);
				String expected = String.valueOf(i > j);

				Unit unit = new UnitImpl();

				unit.getSpec().add(DebugSpec.INSTANCE);

				unit.getSpec().add(LogicSpec.INSTANCE);
				unit.getSpec().add(new ParameterSpec(
						//syntax
						DigitsSpec.INSTANCE,
						MinusSpec.INSTANCE,
						CloseChevronSpec.INSTANCE,
						//value
						NumberSpec.INSTANCE,
						//operator
						MoreThanSpec.INSTANCE,
						SubtractorSpec.INSTANCE
				));
				unit.getSpec().add(listener(event -> {
					if (event.getAction().equals(Action.POST_EXEC)) {
						Memory memory = event.getMemory();
						String actual = memory.peek().eval(memory);

						assertEquals(
								expected,
								actual,
								"Unexpected result"
						);
					}
				}));

				if (
						!unit.initialize(document) ||
						!unit.parse(document) ||
						!unit.analyze(document) ||
						!unit.compile(document) ||
						!unit.execute(document)
				) {
					unit.diagnostic();
					fail("Uncompleted test invocation");
				}
			}
	}
}
