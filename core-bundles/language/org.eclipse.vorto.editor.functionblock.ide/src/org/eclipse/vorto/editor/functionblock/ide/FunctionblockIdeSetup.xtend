/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
/*
 * generated by Xtext
 */
package org.eclipse.vorto.editor.functionblock.ide

import com.google.inject.Guice
import org.eclipse.vorto.editor.functionblock.FunctionblockRuntimeModule
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class FunctionblockIdeSetup extends FunctionblockStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new FunctionblockRuntimeModule, new FunctionblockIdeModule))
	}
	
}
