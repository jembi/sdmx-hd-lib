<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:output method="html" />	
	<xsl:template match="/">
	<h2>Concept Scheme: <xsl:value-of select="//message:Concepts/structure:ConceptScheme/structure:Name" /></h2>
	<p>
	<ul>
		<li>Identifier: <xsl:value-of select="//message:Concepts/structure:ConceptScheme/@id"/></li>
		<li>Agency [version]: <xsl:value-of select="//message:Concepts/structure:ConceptScheme/@agencyID"/>[<xsl:value-of select="//message:Concepts/structure:ConceptScheme/@version"/>]</li>
		<li>IsFinal: <xsl:value-of select="//message:Concepts/structure:ConceptScheme/@isFinal"/> (<xsl:choose>
			<xsl:when test="//message:Concepts/structure:ConceptScheme[@isFinal='true']"><SPAN class="readONLY">read-only</SPAN></xsl:when>
			<xsl:otherwise><SPAN class="canBeModified">can be modified</SPAN></xsl:otherwise>
		</xsl:choose>)</li>
	</ul>
		<xsl:value-of select="//message:Concepts/structure:ConceptScheme/structure:Description" />
	</p>
	<div class="xmlContents">
		&lt;<span class="scTag">structure:ConceptScheme </span><span class="scTag">id</span>="<span class="scContent"><xsl:value-of select="//message:Concepts/structure:ConceptScheme/@id"/></span>"
			<span class="scTag">agencyID</span>="<span class="scContent"><xsl:value-of select="//message:Concepts/structure:ConceptScheme/@agencyID"/></span>"
			<span class="scTag">version</span>="<span class="scContent"><xsl:value-of select="//message:Concepts/structure:ConceptScheme/@version"/></span>"
			<span class="scTag">isFinal</span>="<span class="scContent"><xsl:value-of select="//message:Concepts/structure:ConceptScheme/@isFinal"/></span>"
			<span class="scTag">urn</span>="<span class="scContent"><xsl:value-of select="//message:Concepts/structure:ConceptScheme/@urn"/></span>"
		/&gt;
	</div>

	<table class="tblItems">
		<tr>
			<th>Concept Id</th>
			<th>Format</th>
			<th>Name (EN)</th>
			<th>Description (EN)</th>
		</tr>
		<xsl:for-each select="//message:Concepts/structure:ConceptScheme/structure:Concept">
		<xsl:sort select="structure:Name[@lang='en']" />
		<tr>
			<td><xsl:value-of select="@id" /></td>
			<td><xsl:value-of select="structure:TextFormat/@textType" /></td>
			<td><xsl:value-of select="structure:Name[@xml:lang='en']" /></td>
			<td><xsl:value-of select="structure:Description[@xml:lang='en']" /></td>
		</tr>
		</xsl:for-each>
	</table>
	</xsl:template>
</xsl:stylesheet>
