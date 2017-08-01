/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.internal.service.validation;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.internal.service.InvocationContext;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IModelValidator {
	
	/**
	 * Validates an uploaded model resource
	 * @param modelResource
	 * @param context
	 * @throws ValidationException
	 */
	void validate(ModelInfo modelResource, InvocationContext context) throws ValidationException;
}
