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
package org.jamplate.impl.environment;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.impl.diagnostic.DiagnosticImpl;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Environment}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class EnvironmentImpl implements Environment {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1644390742511931321L;

	/**
	 * The compilations in this environment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Map<Document, Compilation> compilations = new HashMap<>();
	/**
	 * The diagnostic manager in this enlivenment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Diagnostic diagnostic = new DiagnosticImpl();

	@Nullable
	@Override
	public Compilation getCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.get(document);
	}

	@Nullable
	@Override
	public Compilation getCompilation(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		return this.compilations
				.entrySet()
				.parallelStream()
				.filter(entry ->
						new File(String.valueOf(entry.getKey()))
								.equals(new File(name))
				)
				.map(Map.Entry::getValue)
				.findAny()
				.orElse(null);
	}

	@NotNull
	@Override
	public Diagnostic getDiagnostic() {
		return this.diagnostic;
	}

	@NotNull
	@Override
	public Iterator<Compilation> iterator() {
		return this.compilations.values().iterator();
	}

	@Override
	public void setCompilation(@NotNull Document document, @NotNull Compilation compilation) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(compilation, "compilation");
		this.compilations.put(document, compilation);
	}
}
