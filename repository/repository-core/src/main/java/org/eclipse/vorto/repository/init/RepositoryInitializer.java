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
package org.eclipse.vorto.repository.init;

import java.util.Collection;
import java.util.stream.Stream;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RepositoryInitializer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String STANDARDIZATION = "standardization";

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @Autowired
  private DefaultUserAccountService userAccountService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  @EventListener(ApplicationReadyEvent.class)
  @Profile("!test")
  public void initRepo() {
    for (int id = 0; id < admins.length; id++) {
      // ids 1-indexed
      createAdminUser(admins[id], id + 1);
    }

  }

  private void createAdminUser(String username, long id) {
    if (!userAccountService.exists(username)) {
      logger.info("Creating admin user: {}", username);
      User user = User.create(username, null, null);
      // TODO : set to be configurable from configuration file
      user.setEmailAddress("vorto-dev@bosch-si.com");
      user.setAuthenticationProviderId("GITHUB");
      userAccountService.saveUser(user);
    }
    User user = userRepository.findByUsername(username);
    UserRepositoryRoles roles = userRepositoryRoleRepository.findByUser(user.getId())
    .orElse(
        new UserRepositoryRoles()
    );
    if (roles.getUser() == null) {
      roles.setUser(user);
    }
    if (roles.getId() == null) {
      roles.setId(id);
    }
    roles.setRoles(RepositoryRole.SYS_ADMIN.getRole());

    userRepositoryRoleRepository.save(roles);
  }

}
