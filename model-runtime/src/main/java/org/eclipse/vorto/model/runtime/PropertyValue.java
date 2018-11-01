/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public class PropertyValue {
	private ModelProperty meta;
	private Object value;
	
	public PropertyValue(ModelProperty meta, Object value) {
		this.meta = meta;
		this.value = value;
	}
	
	public ModelProperty getMeta() {
		return meta;
	}
	public void setMeta(ModelProperty meta) {
		this.meta = meta;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PropertyValue [meta=" + meta + ", value=" + value + "]";
	}
	
	
	
	
}
