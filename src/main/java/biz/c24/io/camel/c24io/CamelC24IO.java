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

/**
 * A set of helper methods for working with C24IO data formats Camel
 */
public class CamelC24IO {

    public static C24IODataFormat c24io(Class<?> elementType) {
        return new C24IODataFormat(elementType);
    }

    public static C24IODataFormat c24io(Class<?> elementType, C24IOContentType contentType) {
        return new C24IODataFormat(elementType, contentType);
    }

    public static C24IODataFormat c24io(C24IOContentType contentType) {
        return new C24IODataFormat(contentType);
    }
}