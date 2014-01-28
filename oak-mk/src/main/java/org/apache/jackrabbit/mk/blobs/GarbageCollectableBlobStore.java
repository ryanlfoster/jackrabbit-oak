/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.mk.blobs;

import java.io.IOException;

/**
 * A blob store that support garbage collection.
 */
public interface GarbageCollectableBlobStore extends BlobStore {

    /**
     * Set the block size used by this blob store, if the blob store splits
     * binaries into blocks. If not, this setting is ignored.
     * 
     * @param x the block size in bytes.
     */
    void setBlockSize(int x);

    /**
     * Write a blob from a temporary file. The temporary file is removed
     * afterwards. A file based blob stores might simply rename the file, so
     * that no additional writes are necessary.
     *
     * @param tempFilePath the temporary file
     * @return the blob id
     */
    String writeBlob(String tempFileName) throws IOException;

    /**
     * Remove all unused blocks.
     * 
     * @return the number of removed blocks
     */
    int sweep() throws IOException;
    
    /**
     * Start the mark phase.
     */
    void startMark() throws IOException;
    
    /**
     * Clear all objects marked as "transiently in use".
     */
    void clearInUse();

    /**
     * Clear the cache.
     */
    void clearCache();

    /**
     * Get the minimum block size (if there is any).
     * 
     * @return the block size
     */
    long getBlockSizeMin();

}
