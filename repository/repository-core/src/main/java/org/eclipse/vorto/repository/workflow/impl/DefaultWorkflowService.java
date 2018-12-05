/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.InvalidInputException;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowModel;
import org.eclipse.vorto.repository.workflow.model.IWorkflowValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultWorkflowService implements IWorkflowService {

  @Autowired
  private IModelRepository modelRepository;

  private IWorkflowModel SIMPLE_WORKFLOW = null;


  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWorkflowService.class);

  public DefaultWorkflowService(@Autowired IModelRepository modelRepository,
      @Autowired IUserAccountService userRepository,
      @Autowired INotificationService notificationService) {
    this.modelRepository = modelRepository;
    this.SIMPLE_WORKFLOW =
        new SimpleWorkflowModel(userRepository, modelRepository, notificationService);
  }

  @Override
  public ModelInfo doAction(ModelId model, IUserContext user, String actionName)
      throws WorkflowException {
    ModelInfo modelInfo = modelRepository.getById(model);
    final Optional<IState> state = SIMPLE_WORKFLOW.getState(modelInfo.getState());
    final Optional<IAction> action = state.get().getAction(actionName);
    if (action.isPresent() && isValidInput(modelInfo, action.get())
        && passesConditions(action.get().getConditions(), modelInfo, user)) {
      final IState newState = action.get().getTo();
      modelInfo.setState(newState.getName());

      ModelInfo updatedInfo = modelRepository.updateMeta(modelInfo);
      action.get().getFunctions().stream().forEach(a -> executeFunction(a, modelInfo, user));

      return updatedInfo;
    } else {
      throw new WorkflowException(modelInfo, "The given action is invalid.");
    }
  }

  private void executeFunction(IWorkflowFunction function, ModelInfo modelInfo, IUserContext user) {
    try {
      function.execute(modelInfo, user);
    } catch (Throwable t) {
      LOGGER.error("Problem executing workflow function " + function.getClass(), t);
    }
  }

  private boolean isValidInput(ModelInfo modelInfo, IAction action) throws InvalidInputException {
    for (IWorkflowValidator validator : action.getValidators()) {
      validator.validate(modelInfo, action);
    }
    return true;
  }

  @Override
  public List<String> getPossibleActions(ModelId model, IUserContext user) {
    ModelInfo modelInfo = modelRepository.getById(model);
    Optional<IState> state = SIMPLE_WORKFLOW.getState(modelInfo.getState());
    return state.get().getActions().stream()
        .filter(action -> passesConditions(action.getConditions(), modelInfo, user))
        .map(t -> t.getName()).collect(Collectors.toList());
  }

  private boolean passesConditions(List<IWorkflowCondition> conditions, ModelInfo model,
      IUserContext user) {
    for (IWorkflowCondition condition : conditions) {
      if (condition.passesCondition(model, user)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<ModelInfo> getModelsByState(String state) {
    return this.modelRepository.search("state:" + state);
  }

  public IModelRepository getModelRepository() {
    return modelRepository;
  }

  public void setModelRepository(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }

  @Override
  public ModelId start(ModelId modelId) {
    IState nextState = SIMPLE_WORKFLOW.getInitialAction().getTo();
    return modelRepository.updateState(modelId, nextState.getName());
  }

  @Override
  public Optional<IState> getStateModel(ModelId model) {
    ModelInfo modelInfo = modelRepository.getById(model);
    return SIMPLE_WORKFLOW.getState(modelInfo.getState());
  }

}
