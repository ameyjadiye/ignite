/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.pagemem.wal.record.delta;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.internal.pagemem.PageMemory;
import org.apache.ignite.internal.processors.cache.database.tree.io.BPlusMetaIO;

/**
 *
 */
public class MetaPageInitRootInlineRecord extends MetaPageInitRootRecord {

    /** */
    private final int inlineSize;

    /**
     * @param cacheId
     * @param pageId Meta page ID.
     * @param rootId
     * @param inlineSize Inline size.
     */
    public MetaPageInitRootInlineRecord(int cacheId, long pageId, long rootId, int inlineSize) {
        super(cacheId, pageId, rootId);
        this.inlineSize = inlineSize;
    }

    /**
     * @return Inline size.
     */
    public int inlineSize() {
        return inlineSize;
    }

    /** {@inheritDoc} */
    @Override public void applyDelta(PageMemory pageMem, long pageAddr) throws IgniteCheckedException {
        BPlusMetaIO io = BPlusMetaIO.VERSIONS.forPage(pageAddr);

        io.initRoot(pageAddr, rootId, pageMem.pageSize());
        io.setInlineSize(pageAddr, inlineSize);
    }

    /** {@inheritDoc} */
    @Override public RecordType type() {
        return RecordType.BTREE_META_PAGE_INIT_ROOT2;
    }
}
