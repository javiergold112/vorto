/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * Use Plugin SDK API instead!
 */
@Deprecated
class JavaEnumTemplate implements ITemplate<Enum>{
	
	var String enumPackage;
	
	new(String enumPackage) {
		this.enumPackage = enumPackage;
	}
	
	override getContent(Enum en,InvocationContext invocationContext) {
		'''
			/*
			*****************************************************************************************
			* The present code has been generated by the Eclipse Vorto Code Generator.
			*
			* The basis for the generation was the Enum which is uniquely identified by:
			* Name:			«en.name»
			* Namespace:	«en.namespace»
			* Version:		«en.version»
			*****************************************************************************************
			*/
			
			package «enumPackage»;
			
			/**
			* «en.description»
			*/
			public enum «en.name.toFirstUpper» {
				«FOR literal : en.enums SEPARATOR ","»
					/**
					* «literal.description»
					*/
					«literal.name.toUpperCase»
					
				«ENDFOR»
			}
		'''
	}
}