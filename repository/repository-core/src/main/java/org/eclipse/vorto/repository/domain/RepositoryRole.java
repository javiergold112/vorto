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
package org.eclipse.vorto.repository.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Contrary to {@link NamespaceRole}, this role applies instead to the whole repository.<br/>
 * A role is made of the sum of a number of {@link Privilege}s, expressed as a 64-bit integer value.
 * <br/>
 * Inferring which privileges a role has can be done through bitwise {@code &}.<br/>
 * At the time of writing, this and the user-association table are only employed to express which
 * users are sysadmins of the repository.
 */
@Entity
@Table(name = "repository_roles")
public class RepositoryRole implements Serializable, IRole {

  private static final long serialVersionUID = -6221938330735374556L;

  @Id
  private long role;

  private String name;

  private long privileges;

  public long getRole() {
    return role;
  }

  public void setRole(long role) {
    this.role = role;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getPrivileges() {
    return privileges;
  }

  public void setPrivileges(long privileges) {
    this.privileges = privileges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RepositoryRole that = (RepositoryRole) o;
    return role == that.role &&
        privileges == that.privileges &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, name, privileges);
  }
}
