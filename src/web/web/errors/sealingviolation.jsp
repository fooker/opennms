<%@page language="java" contentType="text/html" session="true" isErrorPage="true" %>

<%
    SecurityException e;

    if( exception instanceof ServletException ) {
        e = (SecurityException)((ServletException)exception).getRootCause();
    }
    else { 
        e = (SecurityException)exception;
    }

    if( !e.getMessage().equals( "sealing violation" )) {
        throw new ServletException( "security exception", e );
    }
%>


<html>
<head>
  <title>Incorrect Jar Files | Error | OpenNMS Web Console</title>
  <base HREF="<%=org.opennms.web.Util.calculateUrlBase( request )%>" />
  <link rel="stylesheet" type="text/css" href="includes/styles.css" />
</head>
<body marginwidth="0" marginheight="0" LEFTMARGIN="0" RIGHTMARGIN="0" TOPMARGIN="0">

<% String breadcrumb1 = java.net.URLEncoder.encode("Error"); %>
<jsp:include page="/includes/header.jsp" flush="false" >
  <jsp:param name="title" value="Error" />
  <jsp:param name="breadcrumb" value="<%=breadcrumb1%>" />
</jsp:include>

<br>

<!-- Body -->

<table width="100%" border="0" cellspacing="0" cellpadding="2">
  <tr>
    <td> &nbsp; </td>

    <td>
      <h2>Incorrect Jar Files</h2>

      <p>Some of the Java Archive files (jar files) in the Tomcat install
      are out of date.  Please replace them by going to this      
      <a href="http://faq.opennms.org/faq/fom-serve/cache/55.html">OpenNMS FAQ entry</a> 
      and following the directions there.  Otherwise, your OpenNMS Web system will 
      not work correctly, and you will get undefined results.</p>
    </td>

    <td> &nbsp; </td>
  </tr>
</table>                               

<br>


<jsp:include page="/includes/footer.jsp" flush="false" />

</body>
</html>
