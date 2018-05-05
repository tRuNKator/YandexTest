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

package com.belgilabs.yandextest.network.rest.json;

import com.belgilabs.yandextest.network.rest.utils.ISO8601;
import com.belgilabs.yandextest.network.rest.utils.ResourcePath;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @see <p>API reference <a href="http://api.yandex.com/disk/api/reference/response-objects.xml#resourcelist">english</a>,
 * <a href="https://tech.yandex.ru/disk/api/reference/response-objects-docpage/#resourcelist">russian</a></p>
 */
public class Resource {

    @SerializedName("public_key")
    private String publicKey;

    @SerializedName("_embedded")
    private ResourceList resourceList;

    @SerializedName("name")
    private String name;

    @SerializedName("created")
    private String created;

    @SerializedName("public_url")
    private String publicUrl;

    @SerializedName("origin_path")
    private String originPath;

    @SerializedName("modified")
    private String modified;

    @SerializedName("path")
    private String path;

    @SerializedName("md5")
    private String md5;

    @SerializedName("type")
    private String type;

    @SerializedName("mime_type")
    private String mimeType;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("preview")
    private String preview;

    @SerializedName("size")
    private long size;

    @SerializedName("custom_properties")
    private Object properties;

    public String getPublicKey() {
        return publicKey;
    }

    public ResourceList getResourceList() {
        return resourceList;
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created != null ? ISO8601.parse(created) : null;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public ResourcePath getOriginPath() {
        return originPath != null ? new ResourcePath(originPath) : null;
    }

    public Date getModified() {
        return modified != null ? ISO8601.parse(modified) : null;
    }

    public ResourcePath getPath() {
        return path != null ? new ResourcePath(path) : null;
    }

    public String getMd5() {
        return md5;
    }

    public String getType() {
        return type;
    }

    public boolean isDir() {
        return "dir".equalsIgnoreCase(type);
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getPreview() {
        return preview;
    }

    public long getSize() {
        return size;
    }

    public Object getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "publicKey='" + publicKey + '\'' +
                ", resourceList=" + resourceList +
                ", name='" + name + '\'' +
                ", created='" + getCreated() + '\'' +
                ", publicUrl='" + publicUrl + '\'' +
                ", originPath='" + getOriginPath() + '\'' +
                ", modified='" + getModified() + '\'' +
                ", path='" + getPath() + '\'' +
                ", md5='" + md5 + '\'' +
                ", type='" + type + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", preview='" + preview + '\'' +
                ", size=" + size +
                ", properties=" + properties +
                '}';
    }
}
