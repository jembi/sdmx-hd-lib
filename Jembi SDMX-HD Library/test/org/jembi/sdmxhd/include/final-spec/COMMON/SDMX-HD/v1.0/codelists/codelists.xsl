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
		<table class="tblItems">
		<tr>
			<th>Id</th>
			<th>Name (en)</th>
			<th>Description (en)</th>
			<th>Is Final</th>
			<th>Agency ID</th>			
		</tr>
		<xsl:for-each select="//CodeLists/CodeList">
		<xsl:sort select="@id" />
		<xsl:variable name="xdoc" select="document(@id)"/>
		<tr>
			<td><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/@id" /></td>
			<td><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/structure:Name[@xml:lang='en']" /></td>
			<td><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/structure:Description[@xml:lang='en']" /></td>
			<td>
				<xsl:choose>
					<xsl:when test="$xdoc/message:Structure/message:CodeLists/structure:CodeList[@isFinal='false']"><span style="color:red"><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/@isFinal" /></span></xsl:when>
					<xsl:otherwise><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/@isFinal" /></xsl:otherwise>
				</xsl:choose>			
			</td>
			<td><xsl:value-of select="$xdoc/message:Structure/message:CodeLists/structure:CodeList/@agencyID" /></td>			
		</tr>
		</xsl:for-each>

	</table>

<p>&#0160;</p>
<p>&#0160;</p>
</body>
</html>
	</xsl:template>

</xsl:stylesheet>
