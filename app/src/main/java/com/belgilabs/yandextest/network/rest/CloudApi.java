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


package com.belgilabs.yandextest.network.rest;

import com.belgilabs.yandextest.network.rest.exceptions.NetworkIOException;
import com.belgilabs.yandextest.network.rest.exceptions.ServerIOException;
import com.belgilabs.yandextest.network.rest.json.Link;
import com.belgilabs.yandextest.network.rest.json.Resource;
import com.belgilabs.yandextest.network.rest.json.ResourceList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CloudApi {

    @GET("/v1/disk/resources")
    Call<Resource> getResources(@Query("path") String path, @Query("fields") String fields,
                                @Query("limit") Integer limit, @Query("offset") Integer offset,
                                @Query("sort") String sort, @Query("preview_size") String previewSize,
                                @Query("preview_crop") Boolean previewCrop)
            throws NetworkIOException, ServerIOException;

    @GET("/v1/disk/resources/files")
    Call<ResourceList> getFlatResourceList(@Query("limit") Integer limit, @Query("media_type") String mediaType,
                                     @Query("offset") Integer offset, @Query("fields") String fields,
                                     @Query("preview_size") String previewSize,
                                     @Query("preview_crop") Boolean previewCrop)
            throws NetworkIOException, ServerIOException;

    @GET("/v1/disk/resources/download")
    Call<Link> getDownloadLink(@Query("path") String path)
            throws NetworkIOException, ServerIOException;
}

