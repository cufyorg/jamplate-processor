/*
 *	Copyright 2020 Cufy
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
package org.jamplate.scope;

import java.util.Objects;

/**
 * An abstract for the interface {@link BranchScope}.
 * <p>
 * <ul>
 *     <li>Previous: {@link ForkScope}</li>
 *     <li>Fork: {@link Scope}</li>
 *     <li>Branch: {@link BranchScope}</li>
 *     <li>Next: null</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.17
 */
public abstract class AbstractBranchScope extends AbstractForkScope implements BranchScope {
	@Override
	public boolean tryAttach(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return false;
	}

	@Override
	public boolean tryAttachTo(Scope scope) {
		Objects.requireNonNull(scope, "scope");
		return scope instanceof ForkScope &&
			   super.tryAttachTo(scope);
	}
}
