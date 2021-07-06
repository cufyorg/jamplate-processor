package org.jamplate.glucose.instruction.flow;

import org.jamplate.glucose.instruction.memory.console.Print;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.impl.instruction.Block;
import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.memory.Memory;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Values.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaptureTest {
	@Test
	public void manualAssembly() {
		Environment environment = new EnvironmentImpl();
		Memory memory = new Memory();

		Block instruction = new Block(
				new PushConst(text("FirstValue")),
				Print.INSTANCE,
				new Capture(new Block(
						new PushConst(text("SecondValue")),
						Print.INSTANCE
				)),
				new PushConst(text("ThirdValue")),
				Print.INSTANCE
		);

		instruction.exec(environment, memory);

		String console = memory.getConsole().read();
		String stack = memory.pop().eval(memory);

		assertEquals(
				"FirstValueThirdValue",
				console,
				"Unexpected value printed to the console"
		);
		assertEquals(
				"SecondValue",
				stack,
				"Unexpected value at the stack"
		);
	}
}
