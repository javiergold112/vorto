/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
/*
 * generated by Xtext
 */
package org.eclipse.vorto.editor.infomodel.web

import com.google.inject.Provider
import java.util.List
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.web.servlet.XtextServlet
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup

/**
 * Deploy this class into a servlet container to enable DSL-specific services.
 */
@WebServlet(name = 'XtextServices', urlPatterns = '/xtext-service/*')
class InformationModelServlet extends XtextServlet {
	
	val List<ExecutorService> executorServices = newArrayList
	
	override init() {
		super.init()
		val Provider<ExecutorService> executorServiceProvider = [Executors.newCachedThreadPool => [executorServices += it]]
		InformationModelPackageImpl.init
		FunctionblockStandaloneSetup.doSetup
		new InformationModelWebSetup(executorServiceProvider).createInjectorAndDoEMFRegistration()		
	}
	
	override destroy() {
		executorServices.forEach[shutdown()]
		executorServices.clear()
		super.destroy()
	}
	
}
