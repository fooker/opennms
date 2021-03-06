/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.telemetry.config.api;

import java.util.List;

/**
 * Telemetry connector definition.
 */
public interface ConnectorDefinition extends TelemetryBeanDefinition {

    /**
     * The name of the connect.
     *
     * @return the protocol name
     */
    @Override
    String getName();

    /**
     * The name of the queue the connector "writes" to.
     *
     * @return The name of the queue the parser "writes" to. Must not be null.
     */
    String getQueueName();

    /**
     * The name of the IP-service associated with this connector definition
     *
     * @return the name of the service
     */
    String getServiceName();

    /**
     * Packages may contain settings for specific sources.
     *
     * @return the list of configured packages
     */
    List<? extends PackageDefinition> getPackages();

}
