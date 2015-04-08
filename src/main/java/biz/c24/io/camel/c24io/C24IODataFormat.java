/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package biz.c24.io.camel.c24io;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents the <a href="http://fabric.fusesource.org/documentation/camel/c24io.html">C24IO</a>
 * {@link DataFormat}
 */
@XmlRootElement(name = "c24io")
@XmlAccessorType(XmlAccessType.FIELD)
public class C24IODataFormat extends DataFormatDefinition {
    @XmlAttribute(required = false)
    private String elementTypeName;
    @XmlAttribute(required = false)
    private Class<?> elementType;
    @XmlAttribute(required = false)
    private C24IOContentType contentType;

    public C24IODataFormat() {
        super("biz.c24.io.camel.c24io.C24IOFormat");
    }

    public C24IODataFormat(Class<?> elementType) {
        this();
        this.elementType = elementType;
    }

    public C24IODataFormat(Class<?> elementType, C24IOContentType contentType) {
        this();
        this.elementType = elementType;
        this.contentType = contentType;
    }

    public C24IODataFormat(C24IOContentType contentType) {
        this();
        this.contentType = contentType;
    }

    // Properties
    //-------------------------------------------------------------------------

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public C24IOContentType getContentType() {
        return contentType;
    }

    public void setContentType(C24IOContentType contentType) {
        this.contentType = contentType;
    }

    public Class<?> getElementType() {
        if (elementType == null) {
            if (elementTypeName != null) {
                elementType = ObjectHelper.loadClass(elementTypeName, getClass().getClassLoader());
                if (elementType == null) {
                    throw new IllegalArgumentException("The C24IO Element class " + elementTypeName + " is not on the classpath! Cannot use the dataFormat " + this);
                }
            }
        }
        return elementType;
    }

    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    @Override
    protected void configureDataFormat(DataFormat dataFormat, CamelContext camelContext) {
        Class<?> type = getElementType();
        if (type != null) {
            setProperty(camelContext, dataFormat, "elementType", type);
        }
        C24IOContentType content = getContentType();
        if (content != null) {
            setProperty(camelContext, dataFormat, "contentType", content);
        }
    }
}