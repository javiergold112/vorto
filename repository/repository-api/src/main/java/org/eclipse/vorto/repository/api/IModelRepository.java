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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.repository.api.content.EntityModel;
import org.eclipse.vorto.repository.api.content.EnumModel;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelContent;

/**
 * Model repository lets you find and retrieve Vorto information models from the Vorto Repository
 *
 */
public interface IModelRepository {

	/**
	 * Searches the repository by a query expression. Use {@link IModelRepository#newQuery()} as a helper to formulate your query
	 * @param query expression containing the criteria for the search
	 * @return a list of model info objects, never null
	 */
	CompletableFuture<Collection<ModelInfo>> search(ModelQuery query);
	
	/**
	 * Finds a model by the given model id. 
	 * @param modelId
	 * @return model info that was found in the repository or null if a model does not exist with the given id
	 */
	CompletableFuture<ModelInfo> getById(ModelId modelId);
	
	/**
	 * Gets the actual information model content for a given model id.
	 * @param modelId model id to get its content for
	 * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel}, {@link EntityModel} or {@link EnumModel}
	 * @return model content
	 */
	CompletableFuture<ModelContent> getContent(ModelId modelId);
	
	/**
	 * Gets the actual model content for a given model id including the meta data for the given target platform.
	 * @param modelId model id to get its content for
	 * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel}, {@link EntityModel} or {@link EnumModel}
	 * @param key of the target platform
	 * @return model content
	 */
	CompletableFuture<ModelContent> getContent(ModelId modelId, String targetPlatformKey);
	
	/**
	 * Gets the actual model content for a given model id including the meta data for the given mapping model ID
	 * 
	 * @param modelId model id to get its content for
	 * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel}, {@link EntityModel} or {@link EnumModel}
	 * @param mappingModelId model id of the mapping to look up
	 * @return model content
	 */
	CompletableFuture<ModelContent> getContent(ModelId modelId, ModelId mappingModelId);

	/**
	 * Get a list of file attachments for a model
	 * 
	 * @param modelId model id to get attachments for
	 * @return attachments List of attachment
	 */
	CompletableFuture<List<Attachment>> getAttachments(ModelId modelId);
	
	/**
	 * Get the contents of an attached file to a model
	 * 
	 * @param modelId the model where the file is attached
	 * @param filename the filename of the attached file
	 * @return content The contents of the file
	 */
	CompletableFuture<byte[]> getAttachment(ModelId modelId, String filename);
	
	/**
	 * Creates a new model query builder as a helper for searching models via {@link IModelRepository#search(ModelQuery)} 
	 * @return
	 */
	static ModelQueryBuilder newQuery() {
		return new ModelQueryBuilder();
	}
}
