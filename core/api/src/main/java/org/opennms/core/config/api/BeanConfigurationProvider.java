/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.config.api;

import java.util.Objects;

/**
 * A {@link ConfigurationProvider} that uses a fixed object.
 *
 * @author jwhite
 * @param <T>
 */
public class BeanConfigurationProvider<T> implements ConfigurationProvider {
    private final T object;
    private final long createdAt = System.currentTimeMillis();

    public BeanConfigurationProvider(T object) {
        this.object = Objects.requireNonNull(object);
    }

    @Override
    public Class<?> getType() {
        return object.getClass();
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public long getLastUpdate() {
        return createdAt;
    }

    @Override
    public void registeredToConfigReloadContainer() {
    }

    @Override
    public void deregisteredFromConfigReloadContainer() {
    }
}
