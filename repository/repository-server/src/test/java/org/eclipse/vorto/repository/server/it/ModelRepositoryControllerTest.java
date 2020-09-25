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
package org.eclipse.vorto.repository.server.it;

import com.google.common.collect.Sets;
import java.util.Arrays;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelFullDetailsDTO;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelLink;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelMinimalInfoDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModelRepositoryControllerTest extends IntegrationTestBase {

  @Autowired
  protected AttachmentValidator attachmentValidator;

  @Test
  public void getModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());

    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
  }

  /*
   * Retrieve Model image with empty image attachments
   */
  @Test
  public void getModelImageWithEmptyAttachments() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  /**
   * This originally only tested that consecutive image attachments on a same model responded with
   * HTTP 201. <br/>
   * With the new "display image" tag that is programmatically unique to the last image updated to
   * a model, this test is enriched with a few additional checks. <br/>
   * There is little to test at controller-level, because the images themselves are mocked, and the
   * GET calls actually return the model name as image in the header (so one cannot check that the
   * actual image file name is returned).<br/>
   * Here, we only check that there is indeed an image once it's been added - twice. <br/>
   * More thorough tests are added at repository level.
   *
   * @throws Exception
   */
  @Test
  public void uploadModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
    createImage("model_image.png", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
  }

  /**
   * Contrary to the attachment controller, which is API v.1 and cannot be changed, we can
   * respond with a specific status in the ModelRepositoryController when an attachment is too
   * large.
   *
   * @throws Exception
   */
  @Test
  public void uploadModelImageSizeTooLarge() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin,
        (attachmentValidator.getMaxFileSizeSetting() + 1) * 1024 * 1024)
        .andExpect(status().isPayloadTooLarge());
  }

  @Test
  public void uploadModelImageSizeReasonable() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin,
        (attachmentValidator.getMaxFileSizeSetting() - 1) * 1024 * 1024)
        .andExpect(status().isCreated());
  }

  @Test
  public void createModelWithAPI() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    createNamespaceSuccessfully("com.test", userSysadmin);
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin));
  }

  @Test
  public void createVersionOfModel() throws Exception {
    TestModel testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer, userSysadmin);
    repositoryServer.perform(post("/rest/models/" + testModel.prettyName + "/versions/1.0.1")
        .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer.perform(post("/rest/models/com.test1:Location:1.0.0/versions/1.0.1")
        .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    repositoryServer.perform(delete("/rest/models/" + testModel.prettyName).with(userSysadmin));
  }

  @Test
  public void deleteModelResource() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    String json = createContent("Location.fbmodel");

    // making sure the model is not there
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin))
        .andExpect(status().isNotFound());

    createNamespaceSuccessfully("com.test", userSysadmin);
    Collaborator collaborator = new Collaborator();
    collaborator.setTechnicalUser(false);
    collaborator.setUserId(USER_MODEL_CREATOR_NAME);
    collaborator.setRoles(Sets.newHashSet("model_creator"));
    addCollaboratorToNamespace("com.test", collaborator);
    createModel(userModelCreator, fileName, modelId);
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer.perform(put("/rest/models/" + modelId)
        .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isNotFound());

    // delete non existent model
    repositoryServer.perform(delete("/rest/models/com.test:ASDASD:0.0.1").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getUserModels() throws Exception {
    this.repositoryServer.perform(get("/rest/models/mine/download").with(userSysadmin))
        .andExpect(status().isOk());
  }

  @Test
  public void downloadMappingsForPlatform() throws Exception {
    this.repositoryServer
        .perform(
            get("/rest/models/" + testModel.prettyName + "/download/mappings/test")
                .with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/com.test:Test1:1.0.0/download/mappings/test").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void runDiagnostics() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/diagnostics").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/test:Test123:1.0.0/diagnostics").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getPolicies() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies").with(userModelCreator))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/test:Test123:1.0.0/policies").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getUserPolicy() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelViewer))
        .andExpect(status().isNotFound());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelCreator))
        .andExpect(status().isOk());
  }

  @Test
  public void addValidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelCreator))
        .andExpect(result -> result.getResponse().getContentAsString().contains(
            "{\"principalId\":\"user3\",\"principalType\":\"User\",\"permission\":\"READ\",\"adminPolicy\":false}"));
  }

  @Test
  public void editOwnPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userModelCreator))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void addInvalidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user3\", \"principalType\": \"AUser\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void removePolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete("/rest/models/" + testModel.prettyName + "/policies/user2/User")
                .with(userSysadmin))
        .andExpect(status().isOk());
  }

  /*
   * Only models in Released state can be made public, all other states must return exception
   */
  @Test
  public void makeModelPublicNotReleased() throws Exception {
    this.repositoryServer
        .perform(post("/rest/models/" + testModel.prettyName + "/makePublic").with(userSysadmin))
        .andExpect(status().isForbidden());
  }

  /*
   * Users with SysAdmin access or model publisher role can only make models public, other users should receive Unauthorized
   * exception
   */
  @Test
  public void makeModelPublicNonSysAdmin() throws Exception {
    this.repositoryServer
        .perform(post("/rest/models/" + testModel.prettyName + "/makePublic").with(userModelViewer))
        .andExpect(status().isUnauthorized());
  }

  /**
   * This test performs the following:
   * <ol>
   *   <li>
   *     Creates the {@literal com.test} namespace with a sysadmin user, to hold the models.
   *   </li>
   *   <li>
   *     Adds a non-sysadmin user with {@literal model_creator} role to the {@literal com.test}
   *     namespace.
   *   </li>
   *   <li>
   *     Creates a "Zone" datatype model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates a "Lamp" functionblock model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates an "Address" functionblock model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates a "StreetLamp" model from the corresponding file resource, using the above
   *     functionblocks as dependencies.
   *   </li>
   *   <li>
   *     Adds an attachment to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Adds a link to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Adds a mapping to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Loads the "StreetLamp" model with the REST call used by the UI, and verifies / validates:
   *     <ul>
   *       <li>
   *         Basic {@link org.eclipse.vorto.repository.core.ModelInfo} properties.
   *       </li>
   *       <li>
   *         The Base64-encoded model syntax
   *         (see {@link ModelFullDetailsDTO#getEncodedModelSyntax()})
   *       </li>
   *       <li>
   *         Mapping (see {@link ModelFullDetailsDTO#getMappings()}).
   *       </li>
   *       <li>
   *         Attachment (see {@link ModelFullDetailsDTO#getAttachments()}
   *       </li>
   *       <li>
   *         Link (see {@link ModelFullDetailsDTO#getLinks()}
   *       </li>
   *       <li>
   *         References (see {@link ModelFullDetailsDTO#getReferences()}
   *       </li>
   *       <li>
   *         Policies (see {@link ModelFullDetailsDTO#getPolicies()} and
   *         {@link ModelFullDetailsDTO#getBestPolicy()} for the creating user, and conversely, for
   *         an extraneous user with no access.
   *       </li>
   *       <li>
   *         Workflow actions (see {@link ModelFullDetailsDTO#getActions()} for the creating user,
   *         and conversely, for an extraneous user with no access.
   *       </li>
   *     </ul>
   *   </li>
   *   <li>
   *     Loads the "Lamp" functionblock model with the REST call used by the UI, and verifies the
   *     "referenced by" node (see {@link ModelFullDetailsDTO#getReferencedBy()}.
   *   </li>
   *   <li>
   *     Finally, cleans up and deletes all 4 models, then the namespace.
   *   </li>
   * </ol>
   *
   * @throws Exception
   */
  @Test
  public void verifyFullModelPayloadForUI() throws Exception {
    // model names, ids and file names
    String namespace = "com.test";
    String version = "1.0.0";
    String idFormat = "%s.%s:%s";
    String zoneModelName = "Zone";
    String zoneModelID = String.format(idFormat, namespace, zoneModelName, version);
    String zoneFileName = zoneModelName.concat(".type");
    String lampModelName = "Lamp";
    String lampModelID = String.format(idFormat, namespace, lampModelName, version);
    String lampFileName = lampModelName.concat(".fbmodel");
    String addressModelName = "Address";
    String addressModelID = String.format(idFormat, namespace, addressModelName, version);
    String addressFileName = addressModelName.concat(".fbmodel");
    String streetLampModelName = "StreetLamp";
    String streetLampModelID = String.format(idFormat, namespace, streetLampModelName, version);
    String streetLampFileName = streetLampModelName.concat(".infomodel");
    String streetLampAttachmentFileName = "StreetLampAttachment.json";
    String streetLampLinkURL = "https://vorto.eclipse.org/";
    String streetLampLinkName = "Vorto";

    // creates the namespace as sysadmin
    createNamespaceSuccessfully(namespace, userSysadmin);

    // creates the collaborator payload to add userModelCreator to the namespace
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    userModelCreatorCollaborator.setRoles(Arrays.asList("model_viewer", "model_creator"));
    userModelCreatorCollaborator.setTechnicalUser(false);
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);

    // allows creator rights to userCreator on namespace
    repositoryServer
        .perform(
            put(String.format("/rest/namespaces/%s/users", namespace))
            .with(userSysadmin)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userModelCreatorCollaborator))
        )
        .andExpect(status().isOk());

    // creates the Zone model
    createModel(userModelCreator, zoneFileName, zoneModelID);

    // creates the Lamp model
    createModel(userModelCreator, lampFileName, lampModelID);

    // creates the Address model
    createModel(userModelCreator, addressFileName, addressModelID);

    // creates the StreetLamp model
    createModel(userModelCreator, streetLampFileName, streetLampModelID);

    // adds an attachment to the StreetLamp model
    addAttachment(streetLampModelID, userModelCreator, streetLampAttachmentFileName, MediaType.APPLICATION_JSON);

    // adds a link to the StreetLamp model
    addLink(streetLampModelID, userModelCreator, new ModelLink(streetLampLinkURL, streetLampLinkName));

    /*
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin));*/
  }

}
