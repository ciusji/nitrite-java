/*
 * Copyright (c) 2017-2020. Nitrite author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dizitart.no2.filters;

import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.common.tuples.Pair;
import org.dizitart.no2.exceptions.FilterException;
import org.dizitart.no2.index.ComparableIndexer;
import org.dizitart.no2.store.NitriteMap;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.dizitart.no2.common.util.Numbers.compare;

/**
 * @author Anindya Chatterjee
 */
class LesserEqualFilter extends ComparisonFilter {
    LesserEqualFilter(String field, Comparable<?> value) {
        super(field, value);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Set<NitriteId> findIndexedIdSet() {
        Set<NitriteId> idSet = new LinkedHashSet<>();
        if (getIsFieldIndexed()) {
            if (getIndexer() instanceof ComparableIndexer && getValue() instanceof Comparable) {
                ComparableIndexer comparableIndexer = (ComparableIndexer) getIndexer();
                idSet = comparableIndexer.findLesserEqual(getCollectionName(), getField(), (Comparable) getValue());
            } else {
                if (getValue() instanceof Comparable) {
                    throw new FilterException("lte filter is not supported on indexed field "
                        + getField());
                } else {
                    throw new FilterException(getValue() + " is not comparable");
                }
            }
        }
        return idSet;
    }

    @Override
    protected Set<NitriteId> findIdSet(NitriteMap<NitriteId, Document> collection) {
        throw new FilterException("lte filter cannot be applied on _id field");
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean apply(Pair<NitriteId, Document> element) {
        Comparable comparable = getComparable();
        Document document = element.getSecond();
        Object fieldValue = document.get(getField());
        if (fieldValue != null) {
            if (fieldValue instanceof Number && comparable instanceof Number) {
                return compare((Number) fieldValue, (Number) comparable) <= 0;
            } else if (fieldValue instanceof Comparable) {
                Comparable arg = (Comparable) fieldValue;
                return arg.compareTo(comparable) <= 0;
            } else {
                throw new FilterException(fieldValue + " is not comparable");
            }
        }

        return false;
    }
}
