<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:template match="/">
	<html>
		<head>
			<title>SDMX-HD v1.0 documentation</title>
			<style type="text/css">
			body { color: Black; background-color: White; font-family: Arial, sans-serif; font-size: 10pt; }
			hr { color: black; }
			h1 { font-size: 18pt; letter-spacing: 2px; border-bottom: 1px #ccc solid; padding-top: 5px; padding-bottom: 5px; }
			h2 { font-size: 14pt; letter-spacing: 1px; }
			h3 { font-size: 12pt; font-weight: bold; color: black; }
			.dimName { color: #F93 }
			div.xmlContents { border: 1px solid black; padding: 5px; }
			div.xmlContents .scTag { color: #933; /* maroon */ }
			div.xmlContents .scContent, div.schemaComponent div.contents .scContent a { color: black; font-weight: bold; }
			table { margin-top: 10px; margin-bottom: 10px; margin-left: 0px; margin-right: 0px; border: 1px solid #ccc; }
			table th, table td { font-size: 10pt; vertical-align: top; padding-top: 3px; padding-bottom: 3px; padding-left: 10px; padding-right: 10px; border: 1px solid #ccc; }
			table th { font-weight: bold; text-align: left; }
			table.tblItems th { background-color: #ccc; }
			table.tblItems td { background-color: #eee; }
			</style>
		</head>
		<body style="color: Black; background-color: White; font-family: Arial, sans-serif; font-size: 10pt;">
		<h1 style="font-size: 18pt; letter-spacing: 2px;">SDMX-HD (Health Domain) v1.0 documentation<br /></h1>
		<h2>Organisation Scheme: <xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/structure:Name" /></h2>
		<p>
		<ul>
			<li>Identifier: <xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@id"/></li>
			<li>Agency [version]: <xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@agencyID"/>[<xsl:value-of select="//message:Concepts/structure:ConceptScheme/@version"/>]</li>
			<li>IsFinal: <xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@isFinal"/> (<xsl:choose>
				<xsl:when test="//message:OrganisationSchemes/structure:OrganisationScheme[@isFinal='true']"><span style="color:red">read-only</span></xsl:when>
				<xsl:otherwise><span style="color:blue">can be modified</span></xsl:otherwise>
			</xsl:choose>)</li>
		</ul>
			<xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/structure:Description" />
		</p>
		<div class="xmlContents">
			&lt;<span class="scTag">structure:ConceptScheme </span><span class="scTag">id</span>="<span class="scContent"><xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@id"/></span>"
				<span class="scTag">agencyID</span>="<span class="scContent"><xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@agencyID"/></span>"
				<span class="scTag">version</span>="<span class="scContent"><xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@version"/></span>"
				<span class="scTag">isFinal</span>="<span class="scContent"><xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@isFinal"/></span>"
				<span class="scTag">urn</span>="<span class="scContent"><xsl:value-of select="//message:OrganisationSchemes/structure:OrganisationScheme/@urn"/></span>"
			/&gt;
		</div>

	<table class="tblItems">
		<tr>
			<th>Agency Id</th>
			<th>Name (EN)</th>
			<th>Description (EN)</th>
		</tr>
		<xsl:for-each select="//message:OrganisationSchemes/structure:OrganisationScheme/structure:Agencies/structure:Agency">
		<xsl:sort select="@id" />
		<tr>
			<td><xsl:value-of select="@id" /></td>
			<td><xsl:value-of select="structure:Name[@xml:lang='en']" /></td>
			<td><xsl:value-of select="structure:Description[@xml:lang='en']" /></td>
		</tr>
		</xsl:for-each>
	</table>

<p>&#0160;</p>
<p>&#0160;</p>
</body>
</html>
	</xsl:template>

</xsl:stylesheet>
