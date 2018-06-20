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
package org.eclipse.vorto.repository.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelInfo extends AbstractModel {
	
	protected String author;
	protected Date creationDate;
	protected boolean hasImage = false;
	protected String state;
		
	protected List<ModelId> referencedBy = new ArrayList<ModelId>();
	
	protected Map<String,ModelId> platformMappings = new HashMap<>();
	
	public ModelInfo(ModelId modelId,ModelType modelType) {
		super(modelId,modelType);
	}
	
	public ModelInfo(ModelId modelId, String type) {
		this(modelId,ModelType.valueOf(type));
	}
	
	public ModelInfo() {
	}
	
	public List<ModelId> getReferencedBy() {
		return referencedBy;
	}

	public void setReferencedBy(List<ModelId> referencedBy) {
		this.referencedBy = referencedBy;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Map<String,ModelId> getPlatformMappings() {
		return this.platformMappings;
	}

	public void setPlatformMappings(Map<String,ModelId> platformMappings) {
		this.platformMappings = platformMappings;
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "ModelInfo [ id =" + id + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelInfo other = (ModelInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		return (type == other.type);
	}

	public void addPlatformMapping(String targetPlatform,ModelId mappingId) {
		if (targetPlatform != null && !targetPlatform.equals("")) {
			this.platformMappings.put(targetPlatform,mappingId);
		}
	}

	public void addReferencedBy(ModelId id) {
		this.referencedBy.add(id);
	}

}