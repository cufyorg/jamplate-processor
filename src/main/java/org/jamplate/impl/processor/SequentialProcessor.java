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
package org.jamplate.impl.processor;

import org.jamplate.function.Processor;
import org.jamplate.model.Compilation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A processor that sequentially execute other processors in a pre-specified order when
 * called.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public class SequentialProcessor implements Processor {
	/**
	 * The processors in order.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	protected final List<Processor> processors;

	/**
	 * Construct a new processor that processes using the given processors in order.
	 * <br>
	 * Null processors in the array will be ignored.
	 *
	 * @param processors the processors to be executed when the constructed processor gets
	 *                   executed.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	public SequentialProcessor(@Nullable Processor @NotNull ... processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = Arrays.stream(processors)
								.filter(Objects::nonNull)
								.collect(Collectors.toList());
	}

	/**
	 * Construct a new processor that processes using the given processors in order.
	 * <br>
	 * Null processors in the list will be ignored.
	 *
	 * @param processors the processors to be executed when the constructed processor gets
	 *                   executed.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.2.0 ~2021.05.20
	 */
	public SequentialProcessor(@NotNull List<Processor> processors) {
		Objects.requireNonNull(processors, "processors");
		this.processors = new ArrayList<>();
		for (Processor processor : processors)
			if (processor != null)
				this.processors.add(processor);
	}

	/**
	 * Construct a new processor that processes using the given processors in order.
	 * <br>
	 * Null processors in the array will be ignored.
	 *
	 * @param processors the processors to be executed when the constructed processor gets
	 *                   executed.
	 * @return a new sequential processor that executes the given {@code processors} in
	 * 		order.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialProcessor sequential(@Nullable Processor @NotNull ... processors) {
		return new SequentialProcessor(processors);
	}

	/**
	 * Construct a new processor that processes using the given processors in order.
	 * <br>
	 * Null processors in the list will be ignored.
	 *
	 * @param processors the processors to be executed when the constructed processor gets
	 *                   executed.
	 * @return a new sequential processor that executes the given {@code processors} in
	 * 		order.
	 * @throws NullPointerException if the given {@code processors} is null.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static SequentialProcessor sequential(@NotNull List<Processor> processors) {
		return new SequentialProcessor(processors);
	}

	@NotNull
	@Override
	public Iterator<Processor> iterator() {
		return Collections.unmodifiableList(this.processors).iterator();
	}

	@Override
	public boolean process(@NotNull Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		boolean modified = false;

		for (Processor processor : this.processors)
			while (processor.process(compilation))
				modified = true;

		return modified;
	}
}
