/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jackrabbit.oak.cache;


import com.google.common.cache.Weigher;

/**
 * Determines the weight of object based on the memory taken by them. The memory estimates
 * are based on empirical data and not exact
 */
public class EmpiricalWeigher implements Weigher<String, CacheValue> {

    @Override
    public int weigh(String key, CacheValue value) {
        return 48 + key.length() * 2 + value.getMemory();
    }
    
}