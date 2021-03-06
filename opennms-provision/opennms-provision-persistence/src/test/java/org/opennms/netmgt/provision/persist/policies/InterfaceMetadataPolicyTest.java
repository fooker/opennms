/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.persist.policies;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.spring.BeanUtils;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

public class InterfaceMetadataPolicyTest implements InitializingBean {
    private List<OnmsNode> m_nodes;

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    @Before
    public void setUp() {
        MockLogAppender.setupLogging();
        m_nodes = new ArrayList<>();
        final OnmsNode node1 = new OnmsNode();
        node1.setNodeId("1");
        node1.setForeignSource("fulda");
        node1.setForeignId("websrv");
        node1.setLabel("WWW machine");
        final OnmsIpInterface if1 = new OnmsIpInterface();
        if1.setIpAddress(InetAddressUtils.addr("192.168.1.1"));
        node1.addIpInterface(if1);
        m_nodes.add(node1);

        final OnmsNode node2 = new OnmsNode();
        node2.setNodeId("2");
        node2.setForeignSource("rdu");
        node2.setForeignId("crmsrv");
        node2.setLabel("CRM system");
        final OnmsIpInterface if2 = new OnmsIpInterface();
        if2.setIpAddress(InetAddressUtils.addr("172.16.2.2"));
        node2.addIpInterface(if2);
        m_nodes.add(node2);
    }

    @Test
    @Transactional
    public void testMatchingLabel() {
        final InterfaceMetadataSettingPolicy p = new InterfaceMetadataSettingPolicy();
        p.setIpAddress("~.*\\.168\\..*");
        p.setMetadataKey("theKey");
        p.setMetadataValue("theValue");
        final List<OnmsNode> modifiedNodes = applyPolicy(p);

        final OnmsNode node1 = modifiedNodes.stream().filter(n->n.getId() == 1).findFirst().get();
        final OnmsNode node2 = modifiedNodes.stream().filter(n->n.getId() == 2).findFirst().get();

        assertEquals(1, node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().size());
        assertEquals("requisition", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getContext());
        assertEquals("theKey", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getKey());
        assertEquals("theValue", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getValue());
        assertEquals(0, node2.getIpInterfaces().iterator().next().getRequisitionedMetaData().size());
    }

    @Test
    @Transactional
    public void testMatchingLabelWithCustomContext() {
        final InterfaceMetadataSettingPolicy p = new InterfaceMetadataSettingPolicy();
        p.setIpAddress("~.*\\.168\\..*");
        p.setMetadataKey("theKey");
        p.setMetadataValue("theValue");
        p.setMetadataContext("customContext");
        final List<OnmsNode> modifiedNodes = applyPolicy(p);

        final OnmsNode node1 = modifiedNodes.stream().filter(n->n.getId() == 1).findFirst().get();
        final OnmsNode node2 = modifiedNodes.stream().filter(n->n.getId() == 2).findFirst().get();

        assertEquals(1, node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().size());
        assertEquals("customContext", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getContext());
        assertEquals("theKey", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getKey());
        assertEquals("theValue", node1.getIpInterfaces().iterator().next().getRequisitionedMetaData().get(0).getValue());
        assertEquals(0, node2.getIpInterfaces().iterator().next().getRequisitionedMetaData().size());
    }

    @Test
    @Transactional
    public void testMatchingNothing() {
        final InterfaceMetadataSettingPolicy p = new InterfaceMetadataSettingPolicy();
        p.setIpAddress("~^foobar$");
        p.setMetadataKey("theKey");
        p.setMetadataValue("theValue");

        final List<OnmsNode> modifiedNodes = applyPolicy(p);

        final OnmsNode node1 = modifiedNodes.stream().filter(n->n.getId() == 1).findFirst().get();
        final OnmsNode node2 = modifiedNodes.stream().filter(n->n.getId() == 2).findFirst().get();

        assertEquals(0, node1.getRequisitionedMetaData().size());
        assertEquals(0, node1.getRequisitionedMetaData().size());
    }

    private List<OnmsNode> applyPolicy(final InterfaceMetadataSettingPolicy p) {
        final List<OnmsNode> newNodes = new ArrayList<>(m_nodes);
        newNodes.stream().forEach(n -> n.setIpInterfaces(n.getIpInterfaces().stream().map(i -> p.apply(i, new HashMap<>())).collect(Collectors.toSet())));
        return newNodes;
    }
}
