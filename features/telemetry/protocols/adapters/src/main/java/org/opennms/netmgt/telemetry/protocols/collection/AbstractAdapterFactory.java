/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.telemetry.protocols.collection;

import org.opennms.netmgt.dao.api.InterfaceToNodeCache;
import org.opennms.netmgt.telemetry.api.adapter.AdapterFactory;
import org.opennms.netmgt.telemetry.api.registry.TelemetryRegistry;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAdapterFactory implements AdapterFactory {
    protected final BundleContext bundleContext;

    @Autowired
    private InterfaceToNodeCache interfaceToNodeCache;

    @Autowired
    private TelemetryRegistry telemetryRegistry;

    public AbstractAdapterFactory(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    public InterfaceToNodeCache getInterfaceToNodeCache() {
        return this.interfaceToNodeCache;
    }

    public void setInterfaceToNodeCache(final InterfaceToNodeCache interfaceToNodeCache) {
        this.interfaceToNodeCache = interfaceToNodeCache;
    }

    public TelemetryRegistry getTelemetryRegistry() {
        return this.telemetryRegistry;
    }

    public void setTelemetryRegistry(final TelemetryRegistry telemetryRegistry) {
        this.telemetryRegistry = telemetryRegistry;
    }
}
