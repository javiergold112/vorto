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
package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GrantCollaboratorAccessPolicy implements IWorkflowFunction {

  private IModelRepositoryFactory repositoryFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(GrantCollaboratorAccessPolicy.class);


  public GrantCollaboratorAccessPolicy(IModelRepositoryFactory repositoryFactory) {
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public void execute(ModelInfo model, IUserContext user,Map<String,Object> context) {
    LOGGER.info("Restricting permission of model " + model.getId() + " to collaborators within the tenant");
    repositoryFactory.getPolicyManager(user.getTenant(), user.getAuthentication()).addPolicyEntry(
        model.getId(),
        PolicyEntry.of("USER", PrincipalType.Role, Permission.READ),
        PolicyEntry.of("model_viewer", PrincipalType.Role, Permission.READ),
        PolicyEntry.of("model_creator", PrincipalType.Role, Permission.FULL_ACCESS),
        PolicyEntry.of("model_promoter", PrincipalType.Role, Permission.FULL_ACCESS),
        PolicyEntry.of(RepositoryRole.SYS_ADMIN.getName(), PrincipalType.Role, Permission.FULL_ACCESS),
        PolicyEntry.of("ROLE_USER", PrincipalType.Role, Permission.READ),
        PolicyEntry.of("ROLE_model_viewer", PrincipalType.Role, Permission.READ),
        PolicyEntry.of("ROLE_model_creator", PrincipalType.Role, Permission.FULL_ACCESS),
        PolicyEntry.of("ROLE_model_promoter", PrincipalType.Role, Permission.FULL_ACCESS),
        PolicyEntry.of("ROLE_" + RepositoryRole.SYS_ADMIN.getName(), PrincipalType.Role, Permission.FULL_ACCESS));
  }
}
