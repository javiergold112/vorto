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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.03 at 10:07:53 AM SGT 
//


package org.eclipse.vorto.repository.importer.ipso;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bosch.lwm2m.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bosch.lwm2m.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LWM2M }
     * 
     */
    public LWM2M createLWM2M() {
        return new LWM2M();
    }

    /**
     * Create an instance of {@link LWM2M.Object }
     * 
     */
    public LWM2M.Object createLWM2MObject() {
        return new LWM2M.Object();
    }

    /**
     * Create an instance of {@link LWM2M.Object.Resources }
     * 
     */
    public LWM2M.Object.Resources createLWM2MObjectResources() {
        return new LWM2M.Object.Resources();
    }

    /**
     * Create an instance of {@link LWM2M.Object.Resources.Item }
     * 
     */
    public LWM2M.Object.Resources.Item createLWM2MObjectResourcesItem() {
        return new LWM2M.Object.Resources.Item();
    }

}
