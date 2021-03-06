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

package org.opennms.netmgt.flows.rest;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.opennms.netmgt.flows.rest.model.FlowGraphUrlInfo;
import org.opennms.netmgt.flows.rest.model.FlowNodeDetails;
import org.opennms.netmgt.flows.rest.model.FlowNodeSummary;
import org.opennms.netmgt.flows.rest.model.FlowSeriesResponse;
import org.opennms.netmgt.flows.rest.model.FlowSummaryResponse;

@Path("flows")
public interface FlowRestService {

    String DEFAULT_STEP_MS = "300000"; // 5 minutes
    String DEFAULT_TOP_N = "10";
    String DEFAULT_LIMIT = "10";

    /**
     * Retrieves the number of flows persisted in the repository.
     *
     * Supports filtering.
     *
     * @param uriInfo JAX-RS context
     * @return number of flows that match the given query
     */
    @GET
    @Path("count")
    Long getFlowCount(@Context final UriInfo uriInfo);

    /**
     * Retrieves a summary of the nodes that have exported flows.
     *
     * Supports filtering.
     *
     * @return node summaries
     */
    @GET
    @Path("exporters")
    @Produces(MediaType.APPLICATION_JSON)
    List<FlowNodeSummary> getFlowExporters();

    /**
     * Retrieved detailed information about a specific node.
     *
     * Supports filtering.
     *
     * @param nodeId node id
     * @return node details
     */
    @GET
    @Path("exporters/{nodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    FlowNodeDetails getFlowExporter(@PathParam("nodeId") final Integer nodeId);

    @GET
    @Path("dscp/enumerate")
    @Produces(MediaType.APPLICATION_JSON)
    List<Integer> getDscpValues(
            @Context UriInfo uriInfo
    );

    @GET
    @Path("dscp")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSummaryResponse getDscpSummaries(
            @Context UriInfo uriInfo
    );

    @GET
    @Path("dscp/series")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSeriesResponse getDscpSeries(
            @DefaultValue(DEFAULT_STEP_MS) @QueryParam("step") final long step,
            @Context UriInfo uriInfo
    );

    /**
     * Retrieve the list of applications.
     *
     * Supports filtering.
     *
     * @param matchingPrefix a string prefix that can be used to further filter the results
     * @param limit the maximum number of applications to return
     * @return the list of applications
     */
    @GET
    @Path("applications/enumerate")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getApplications(@DefaultValue("") @QueryParam("prefix") final String matchingPrefix,
                                 @DefaultValue(DEFAULT_LIMIT) @QueryParam("limit") final long limit,
                                 @Context UriInfo uriInfo);

    @GET
    @Path("applications")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSummaryResponse getApplicationSummary(
            @QueryParam("N") final Integer N,
            @QueryParam("application") final Set<String> applications,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context UriInfo uriInfo
    );

    @GET
    @Path("applications/series")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSeriesResponse getApplicationSeries(
            @DefaultValue(DEFAULT_STEP_MS) @QueryParam("step") final long step,
            @QueryParam("N") final Integer N,
            @QueryParam("application") final Set<String> applications,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context final UriInfo uriInfo
    );

    /**
     * Retrieve the list of hosts.
     *
     * Supports filtering.
     *
     * @param prefix a string prefix that can be used to further filter the results
     * @param limit the maximum number of hosts to return
     * @return the list of hosts
     */
    @GET
    @Path("hosts/enumerate")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getHosts(@DefaultValue(".*") @QueryParam("pattern") final String regex,
                          @DefaultValue(DEFAULT_LIMIT) @QueryParam("limit") final long limit,
                          @Context UriInfo uriInfo);

    @GET
    @Path("hosts")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSummaryResponse getHostSummary(
            @QueryParam("N") final Integer N,
            @QueryParam("host") final Set<String> hosts,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context UriInfo uriInfo
    );

    @GET
    @Path("hosts/series")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSeriesResponse getHostSeries(
            @DefaultValue(DEFAULT_STEP_MS) @QueryParam("step") final long step,
            @QueryParam("N") final Integer N,
            @QueryParam("host") final Set<String> hosts,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context final UriInfo uriInfo
    );

    /**
     * Retrieve the list of conversations.
     *
     * Supports filtering.
     * 
     * @param locationPattern the regex pattern for the location field
     * @param protocolPattern the regex pattern for the protocol field
     * @param lowerIPPattern the regex pattern for the lower IP field
     * @param upperIPPattern the regex pattern for the upper IP field
     * @param applicationPattern the regex pattern for the application field
     * @param limit limit for how many conversations to return
     * @return the list of conversations
     */
    @GET
    @Path("conversations/enumerate")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getConversations(@DefaultValue(".*") @QueryParam("location") final String locationPattern,
                                  @DefaultValue(".*") @QueryParam("protocol") final String protocolPattern,
                                  @DefaultValue(".*") @QueryParam("lower") final String lowerIPPattern,
                                  @DefaultValue(".*") @QueryParam("upper") final String upperIPPattern,
                                  @DefaultValue(".*") @QueryParam("application") final String applicationPattern,
                                  @DefaultValue(DEFAULT_LIMIT) @QueryParam("limit") final long limit,
                                  @Context UriInfo uriInfo);
    
    @GET
    @Path("conversations")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSummaryResponse getConversationSummary(
            @QueryParam("N") final Integer N,
            @QueryParam("conversation") final Set<String> conversations,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context final UriInfo uriInfo
    );

    @GET
    @Path("conversations/series")
    @Produces(MediaType.APPLICATION_JSON)
    FlowSeriesResponse getConversationSeries(
            @DefaultValue(DEFAULT_STEP_MS) @QueryParam("step") final long step,
            @QueryParam("N") final Integer N,
            @QueryParam("conversation") final Set<String> conversations,
            @DefaultValue("false") @QueryParam("includeOther") boolean includeOther,
            @Context final UriInfo uriInfo
    );

    @GET
    @Path("flowGraphUrl")
    FlowGraphUrlInfo getFlowGraphUrlInfo(@Context final UriInfo uriInfo);

}
