/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ditto.schema

import java.util.HashSet
import java.util.List
import java.util.Set
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.ConstraintTemplate
import org.eclipse.vorto.plugin.generator.InvocationContext

class Utils {
	var static ConstraintTemplate constraintTemplate = new ConstraintTemplate()
	
	static def getConstraintsContent(ConstraintRule constraintRule,InvocationContext invocationContext){
		'''
		«IF constraintRule !== null»
			«FOR constraint : constraintRule.constraints BEFORE ',\n' SEPARATOR ','»
				«constraintTemplate.getContent(constraint, invocationContext)»
			«ENDFOR»
		«ENDIF»
		'''
	}

		static def EList<FunctionblockModel> getReferencedFunctionBlocks(InformationModel infomodel) {
    		var fbs = new BasicEList<FunctionblockModel>();
    		for (FunctionblockProperty property : infomodel.properties) {
    			if (fbs.filter[EcoreUtil.equals(it, property.type)].empty) {
    				fbs.add(property.type);
    			}
    		}
    		return fbs;
    	}


    	static def EList<FunctionblockModel> getFunctionBlockHierarchy(FunctionblockModel model) {
        	var fbs = new BasicEList<FunctionblockModel>();
        	    if(model.superType == null) {
        	        fbs.add(model);
                 } else {
                    fbs.add(model);
        	        fbs.addAll(getFunctionBlockHierarchy(model.superType));
        	     }
         	return fbs;
        }

    	static def Set<Entity> getReferencedEntities(InformationModel infomodel) {
    		var entities = new HashSet<Entity>();
    		for (FunctionblockProperty property : infomodel.properties) {
    			for (Entity entity : org.eclipse.vorto.plugin.utils.Utils.getReferencedEntities(
    				property.type.functionblock)) {
    				if (entities.filter[createModelId(it).equals(createModelId(entity))].empty) {
    					entities.add(entity);
    				}
    			}
    		}
    		return entities;
    	}

    	static def ModelId createModelId(Model model) {
    		return new ModelId(model.name, model.namespace, model.version)
    	}

    	static def EList<Enum> getReferencedEnums(InformationModel infomodel) {
    		var enums = new BasicEList<Enum>();
    		for (FunctionblockProperty property : infomodel.properties) {
    			for (Enum enumeration : org.eclipse.vorto.plugin.utils.Utils.getReferencedEnums(
    				property.type.functionblock)) {
    				if (enums.filter[EcoreUtil.equals(it, enumeration)].empty) {
    					enums.add(enumeration);
    				}
    			}
    		}
    		return enums;
    	}

    	static def CharSequence calculateRequired(
    		List<Property> properties) '''«IF !properties.filter[presence !== null && presence.mandatory].isEmpty»required: [«FOR property : properties.filter[presence !== null && presence.mandatory] SEPARATOR ','»«property.name»«ENDFOR»]«ENDIF»'''
}
