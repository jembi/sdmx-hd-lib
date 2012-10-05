<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:output method="html" />	
	<xsl:template match="/">
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
		<br />
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
	</xsl:template>
</xsl:stylesheet>
