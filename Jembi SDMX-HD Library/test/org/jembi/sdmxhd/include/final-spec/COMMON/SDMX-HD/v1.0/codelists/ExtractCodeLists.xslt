<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
	<xsl:template match="//Structure/CodeLists">
		<xsl:apply-templates select="CodeLists" /> 
	</xsl:template>
	<xsl:template match="structure:CodeList/structure:Code">
exec CreateCodeList '<xsl:value-of select="parent::structure:CodeList/@agencyID" />','<xsl:value-of select="parent::structure:CodeList/structure:Name" />','<xsl:value-of select="parent::structure:CodeList/@id" />','<xsl:value-of select="./structure:Description" />','<xsl:value-of select="@value" />','<xsl:value-of select="parent::structure:CodeList/structure:Description" />',null;
	</xsl:template>
</xsl:stylesheet>
