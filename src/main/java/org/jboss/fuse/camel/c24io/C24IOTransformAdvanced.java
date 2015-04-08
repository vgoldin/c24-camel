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
package org.jboss.fuse.camel.c24io;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.Element;
import biz.c24.io.api.data.ValidationException;
import biz.c24.io.api.transform.Transform;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Transforms an <a href="http://fabric.fusesource.org/documentation/camel/c24io.html">C24 IO</a>
 * object into some output format
 *
 * @version $Revision$
 */
public class C24IOTransformAdvanced implements Processor {
    private Transform transform;

    public C24IOTransformAdvanced(Transform transform) {
        this.transform = transform;
    }

    public static C24IOTransformAdvanced transform(Class<?> transformType) {
        Transform transformer = (Transform) ObjectHelper.newInstance(transformType);
        return transform(transformer);
    }

    public static C24IOTransformAdvanced transform(Transform transformer) {
        return new C24IOTransformAdvanced(transformer);
    }   

    public void process(Exchange exchange) throws Exception {
        ComplexDataObject[][] objects = null;
        ComplexDataObject dataObject = null;
        try {
            dataObject = exchange.getIn().getMandatoryBody(ComplexDataObject.class);
        } catch (InvalidPayloadException e1) {
            try {
                objects = exchange.getIn().getMandatoryBody(ComplexDataObject[][].class);
            } catch (InvalidPayloadException e2) {
                objects = getInBodyAsArray(exchange, objects);
            }
        }
            
        if (objects == null) {
            if (dataObject == null) {
                dataObject = unmarshalDataObject(exchange);
            }
            objects = new ComplexDataObject[][]{{dataObject}};
        }
        Object result = transform(objects);

        Message out = exchange.getOut();
        out.setBody(result);
    }

    private ComplexDataObject[][] getInBodyAsArray(Exchange exchange, ComplexDataObject[][] objects) {
        try {
            ComplexDataObject[] array = exchange.getIn().getMandatoryBody(ComplexDataObject[].class);
            if (array != null) {
                objects = new ComplexDataObject[][]{array};
            }
        } catch (InvalidPayloadException e) {
            // Do nothing here
        }
        return objects;
    }

    // Properties
    //-------------------------------------------------------------------------
    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }


    /**
     * Convert the output of a C24-iO transform to List form
     *
     * @param array
     * @return
     */
    private static List<List<Object>> toList(Object[][] array) {

        List<List<Object>> ret = null;

        if (array != null) {
            ret = new ArrayList<List<Object>>(array.length);

            for (int i = 0; i < array.length; i++) {
                ret.add(Arrays.asList(array[i]));
            }
        }

        return ret;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    protected List<List<Object>> transform(Object[][] objects) throws ValidationException {
        Transform transformer = getTransform();
        return toList(transformer.transform(objects));
    }

    // Implementation methods
    //-------------------------------------------------------------------------
    protected ComplexDataObject unmarshalDataObject(Exchange exchange) throws InvalidPayloadException, IOException {
        // lets try use the Source to unmarshall it
        Transform transformer = getTransform();
        Element input = transformer.getInput(0);
        C24IOSource source = new C24IOSource(input);
        return source.parseDataObject(exchange);
    }
}
