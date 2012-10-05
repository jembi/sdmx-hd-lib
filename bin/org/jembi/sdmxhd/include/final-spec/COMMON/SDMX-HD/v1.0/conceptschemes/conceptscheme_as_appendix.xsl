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
		<br />	
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
		<xsl:sort select="@id" />
		<tr>
			<td><xsl:value-of select="@id" /></td>
			<td>
			<xsl:choose>
				<xsl:when test="structure:TextFormat/@textType"><xsl:value-of select="structure:TextFormat/@textType" /></xsl:when>
				<xsl:otherwise>
					<xsl:variable name="coded">,_VSTATUS,AGROUP,ASOURCE,BOUNDARIES,CALENDAR_TYPE,COD,CONDITION,CONDITION_STATUS,CONTACT,CURRENCY,DATAELEMENT,DISAGG,DSOURCE,DSTYPE,DTYPE,EDUC,FPERIOD,FREQ,GBOUNDARY_TYPE,GCODE_COUNTRY,GENDER,GEOGRAPHIC_PLACE_NAME,GLEVEL,GLOCATION,HIF,INCOME,INDICATOR,LOGICAL_VALUE,METYPE,MULT,OBS_STATUS,OPERAND,ORG_COMP,ORG_MAINT,ORG_RESP,ORGANIZATION,OTYPE,PERIODICITY,PROG,PSTATUS,PTYPE,SECTOR,SET,SEX,TPOP,TSTAT,UNIT,VALUE_TYPE,</xsl:variable>
					<xsl:choose>
						<xsl:when test="contains(concat(',', $coded, ','), concat(',', @id, ','))">Coded</xsl:when>
						<xsl:otherwise>String</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>			
			</td>
			<td><xsl:value-of select="structure:Name[@xml:lang='en']" /></td>
			<td><xsl:value-of select="structure:Description[@xml:lang='en']" /></td>
		</tr>
		</xsl:for-each>
	</table>
	</xsl:template>
</xsl:stylesheet>



