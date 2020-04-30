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
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * Use Plugin SDK API instead!
 */
@Deprecated
class JavaInformationModelInterfaceTemplate implements ITemplate<InformationModel>{
	
	var String classPackage
	var String[] imports
	var String interfacePrefix
	var ITemplate<FunctionblockProperty> getterDeclarationTemplate
	var ITemplate<FunctionblockProperty> setterDeclarationTemplate
	
	new(String classPackage, 
		String interfacePrefix,
		String[] imports,
		ITemplate<FunctionblockProperty> getterTemplate, 
		ITemplate<FunctionblockProperty> setterTemplate) {
			this.classPackage=classPackage
			this.interfacePrefix = interfacePrefix
			this.imports = imports
			this.getterDeclarationTemplate = getterTemplate
			this.setterDeclarationTemplate = setterTemplate
	}
	
	override getContent(InformationModel im,InvocationContext invocationContext) {
		'''
			/*
			*****************************************************************************************
			* The present code has been generated by the Eclipse Vorto Java Code Generator.
			*
			* The basis for the generation was the Information Model which is uniquely identified by:
			* Name:			«im.name»
			* Namespace:	«im.namespace»
			* Version:		«im.version»
			*****************************************************************************************
			*/
			
			package «classPackage»;
			
			«FOR imprt :imports»
				import «imprt».*;
			«ENDFOR»
			
			/**
			* «im.description»
			*/
			public interface «interfacePrefix»«im.name.toFirstUpper» {

				«FOR fbProperty : im.properties»
					«getterDeclarationTemplate.getContent(fbProperty,invocationContext)»
					
					«setterDeclarationTemplate.getContent(fbProperty,invocationContext)»
										
				«ENDFOR»
			}
		'''
	}
}