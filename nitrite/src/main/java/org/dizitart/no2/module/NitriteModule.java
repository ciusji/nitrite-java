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

package org.dizitart.no2.module;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anindya Chatterjee
 */
public interface NitriteModule {
    static NitriteModule module(NitritePlugin... plugins) {
        return new NitriteModule() {
            @Override
            public Set<NitritePlugin> plugins() {
                return setOf(plugins);
            }
        };
    }

    Set<NitritePlugin> plugins();

    @SuppressWarnings("unchecked")
    default <T> Set<T> setOf(T... items) {
        Set<T> set = new HashSet<>();
        if (items != null) {
            set.addAll(Arrays.asList(items));
        }
        return set;
    }

    static <T extends ModuleConfig> T withConfig() {
        return null;
    }

    interface ModuleConfig {
        NitriteModule build();
    }
}