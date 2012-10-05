<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:output method="html" />	
	<xsl:template match="/">
		<h2>Code List: <xsl:value-of select="//message:CodeLists/structure:CodeList/structure:Name" /></h2>
		<p>
		<ul>
			<li>Identifier: <xsl:value-of select="//message:CodeLists/structure:CodeList/@id"/></li>
			<li>Agency [version]: <xsl:value-of select="//message:CodeLists/structure:CodeList/@agencyID"/>[<xsl:value-of select="//message:CodeLists/structure:CodeList/@version"/>]</li>
			<li>IsFinal: <xsl:value-of select="//message:CodeLists/structure:CodeList/@isFinal"/> (<xsl:choose>
				<xsl:when test="//message:CodeLists/structure:CodeList[@isFinal='true']"><SPAN class="readONLY">read-only</SPAN></xsl:when>
				<xsl:otherwise><SPAN class="canBeModified">can be modified</SPAN></xsl:otherwise>
			</xsl:choose>)</li>
		</ul>
		<br />
			<xsl:value-of select="//message:CodeLists/structure:CodeList/structure:Description" />
		</p>
		<div class="xmlContents">
			&lt;<span class="scTag">structure:CodeList </span><span class="scTag">id</span>="<span class="scContent"><xsl:value-of select="//message:CodeLists/structure:CodeList/@id"/></span>"
				<span class="scTag">agencyID</span>="<span class="scContent"><xsl:value-of select="//message:CodeLists/structure:CodeList/@agencyID"/></span>"
				<span class="scTag">version</span>="<span class="scContent"><xsl:value-of select="//message:CodeLists/structure:CodeList/@version"/></span>"
				<span class="scTag">isFinal</span>="<span class="scContent"><xsl:value-of select="//message:CodeLists/structure:CodeList/@isFinal"/></span>"
				<span class="scTag">urn</span>="<span class="scContent"><xsl:value-of select="//message:CodeLists/structure:CodeList/@urn"/></span>"
			/&gt;
		</div>

		<table class="tblItems">
			<tr>
				<th>Key</th>
				<th>Value (EN)</th>
			</tr>
			<xsl:for-each select="//message:CodeLists/structure:CodeList/structure:Code">
			<xsl:sort select="@value" />
			<tr>
				<td><xsl:value-of select="@value" /></td>
				<td><xsl:value-of select="structure:Description[@xml:lang='en']" /></td>
			</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>
