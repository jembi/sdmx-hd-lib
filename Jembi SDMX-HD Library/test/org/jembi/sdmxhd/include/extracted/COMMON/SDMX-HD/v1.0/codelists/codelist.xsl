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
		<h1 style="font-size: 18pt; letter-spacing: 2px;">SDMX-HD (Health Domain) v1.0 documentation<br />
            <span style="font-size: medium" >&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160;&#0160; 
            Version 2009.04.WHO.03</span></h1>
		<h2>Code List: <xsl:value-of select="//message:CodeLists/structure:CodeList/structure:Name" /></h2>
		<p>
		<ul>
			<li>Identifier: <xsl:value-of select="//message:CodeLists/structure:CodeList/@id"/></li>
			<li>Agency [version]: <xsl:value-of select="//message:CodeLists/structure:CodeList/@agencyID"/>[<xsl:value-of select="//message:CodeLists/structure:CodeList/@version"/>]</li>
			<li>IsFinal: <xsl:value-of select="//message:CodeLists/structure:CodeList/@isFinal"/> (<xsl:choose>
				<xsl:when test="//message:CodeLists/structure:CodeList[@isFinal='true']"><span style="color:red">read-only</span></xsl:when>
				<xsl:otherwise><span style="color:blue">can be modified</span></xsl:otherwise>
			</xsl:choose>)</li>
		</ul>
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
		<xsl:sort select="structure:Description[@lang='en']" />
		<tr>
			<td><xsl:value-of select="@value" /></td>
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
<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="CodeLists" userelativepaths="yes" externalpreview="no" url="CL_ASOURCE+SDMX-HD+1.0.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal"
		          customvalidator="">
			<advancedProp name="sInitialMode" value=""/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bSchemaAware" value="true"/>
			<advancedProp name="bXml11" value="false"/>
			<advancedProp name="iValidation" value="0"/>
			<advancedProp name="bExtensions" value="true"/>
			<advancedProp name="iWhitespace" value="0"/>
			<advancedProp name="sInitialTemplate" value=""/>
			<advancedProp name="bTinyTree" value="true"/>
			<advancedProp name="bWarnings" value="true"/>
			<advancedProp name="bUseDTD" value="false"/>
			<advancedProp name="iErrorHandling" value="fatal"/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition></MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->