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
package org.eclipse.vorto.repository.workflow.impl.conditions;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;

import java.util.Objects;

public class HasRoleCondition extends AbstractWorkflowCondition {

  private IUserAccountService userRepository;
  private IRole role;

  public HasRoleCondition(IUserAccountService userRepository, IRole role, NamespaceService namespaceService, UserNamespaceRoleService userNamespaceRoleService) {
    super(namespaceService, userNamespaceRoleService);
    this.userRepository = userRepository;
    this.role = role;
  }

  @Override
  public boolean passesCondition(ModelInfo model, IUserContext user) {
    User foundUser = userRepository.getUser(user.getUsername());
    return Objects.nonNull(foundUser) && hasRole(user, foundUser, role);
  }


}
