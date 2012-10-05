<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:output method="html" />	
	<xsl:template match="/">
	<h2>Hierarchical Code Lists: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/structure:Name" /></h2>
	<p>
	<ul>
		<li>Identifier: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@id"/></li>
		<li>Agency [version]: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@agencyID"/>[<xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@version"/>]</li>
		<li>IsFinal: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@isFinal"/> (<xsl:choose>
			<xsl:when test="//message:HierarchicalCodelists/structure:HierarchicalCodelist[@isFinal='true']"><SPAN class="readONLY">read-only</SPAN></xsl:when>
			<xsl:otherwise><SPAN class="canBeModified">can be modified</SPAN></xsl:otherwise>
		</xsl:choose>)</li>
	</ul>
		<br />	
		<xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/structure:Description" />
	</p>
	<div class="xmlContents">
		&lt;<span class="scTag">structure:HierarchicalCodelist </span><span class="scTag">id</span>="<span class="scContent"><xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@id"/></span>"
			<span class="scTag">agencyID</span>="<span class="scContent"><xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@agencyID"/></span>"
			<span class="scTag">version</span>="<span class="scContent"><xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@version"/></span>"
			<span class="scTag">isFinal</span>="<span class="scContent"><xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@isFinal"/></span>"
			<span class="scTag">urn</span>="<span class="scContent"><xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@urn"/></span>"
		/&gt;
	</div>			
	<div id="DOMViewer" class="domViewer" >
	  <table width="60%" border="1">
		<tr>
		  <td align="left" valign="top">
			<!-- Draw the DOMViewer -->
			<ul class="domol" id="DOMtree">
					<li class="liOpen"><img src='misc/DOM.gif' />DOM
							<ul>
									<li id='DOMhierarchicalcodelistsLI'><img src='misc/list.gif' />Hierarchical Code-Lists
											<ul id="DOMhierarchicalcodelists">
												<xsl:for-each select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/structure:Hierarchy">
													<li><xsl:value-of select="structure:Name" />
													<ul>
														<xsl:apply-templates select="structure:CodeRef" />															
													</ul>
													</li>														
												</xsl:for-each>
											</ul>
									</li>
							</ul>
					  </li>
			</ul>
		   </td>
		</tr>
	  </table>
	</div>
	</xsl:template>
	<xsl:template match="structure:CodeRef">
		<xsl:variable name="codelistfilename" select="substring-after(structure:CodelistAliasRef,'AL_')" />
		<xsl:variable name="codelistfile" select="concat('CL_',$codelistfilename,'.SDMX-HD.1.0.xml')"/>																	
		<xsl:variable name="codevalue" select="structure:CodeID"/>																
		<li><xsl:value-of select="structure:CodeID" /><img src='misc/ref.gif' alt='reference' /><xsl:value-of select="document($codelistfile)/message:Structure/message:CodeLists/structure:CodeList/structure:Code[@value=$codevalue]/structure:Description[@xml:lang='en']" />																	
		<xsl:if test="structure:CodeRef">															
			<ul>
			<xsl:apply-templates select="structure:CodeRef" />
			</ul>
		</xsl:if>
		</li>		
	</xsl:template>	
</xsl:stylesheet>
