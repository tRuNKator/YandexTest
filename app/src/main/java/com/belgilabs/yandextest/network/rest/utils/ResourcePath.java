/*
 * Copyright (c) 2015 Yandex
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.belgilabs.yandextest.network.rest.utils;

public class ResourcePath {

    private final static char SEPARATOR = ':';

    private final String prefix, path;

    public ResourcePath(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        int index = str.indexOf(SEPARATOR);
        if (index == -1) {
            prefix = null;
            path = str;
        } else {
            prefix = str.substring(0, index);
            path = str.substring(index + 1);
            if (prefix.length() == 0) {
                throw new IllegalArgumentException();
            }
        }
        if (path.length() == 0) {
            throw new IllegalArgumentException();
        }
    }

    public ResourcePath(String prefix, String path) {
        if (prefix == null || path == null || prefix.length() == 0 || path.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prefix = prefix;
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourcePath that = (ResourcePath) o;

        return (path != null ? path.equals(that.path) : that.path == null)
                && (prefix != null ? prefix.equals(that.prefix) : that.prefix == null);
    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return (prefix != null ? prefix + SEPARATOR : "") + path;
    }
}
