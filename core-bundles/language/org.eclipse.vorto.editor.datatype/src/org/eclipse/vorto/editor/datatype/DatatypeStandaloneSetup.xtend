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
package
/*
 * generated by Xtext
 */
org.eclipse.vorto.editor.datatype

/** 
 * Initialization support for running Xtext languages without equinox extension
 * registry
 */
class DatatypeStandaloneSetup extends DatatypeStandaloneSetupGenerated {
	def static void doSetup() {
		new DatatypeStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
