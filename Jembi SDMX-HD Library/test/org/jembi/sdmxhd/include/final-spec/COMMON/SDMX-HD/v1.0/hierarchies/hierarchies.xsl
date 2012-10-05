<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
	xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
	>
	<xsl:template match="/">
	<html>
		<head>		
			<script type="text/javascript">
			<![CDATA[ 			
			addEvent(window,"load",initPage);
			setTimeout("initPage();",5000);

			// Manager the Tree
			function addEvent(obj,type,fn) { if(obj.addEventListener) { obj[type+"Handler"]=fn; obj.addEventListener(type,fn,false); } else if (obj.attachEvent) {  obj[type+"Handler"]=fn; obj.attachEvent("on"+type,obj[type+"Handler"]); } }
			function removeEvent(obj,type) { var fct = null; if (typeof(obj[type+"Handler"]) == 'function') { fct = obj[type+"Handler"]; obj[type+"Handler"]=null; if(obj.removeEventListener) { obj.removeEventListener(type,fct,false); } else if (obj.detachEvent) { obj.detachEvent("on"+type,fct); } } }
			function setDefault(name,val){if(typeof(window[name])=="undefined" || window[name]==null){window[name]=val;}}
			function expandTree(treeId){var ul = document.getElementById(treeId);if(ul == null){return false;}expandCollapseList(ul,nodeOpenClass);}
			function collapseTree(treeId){var ul = document.getElementById(treeId);if(ul == null){return false;}expandCollapseList(ul,nodeClosedClass);}
			function expandToItem(treeId,itemId){var ul = document.getElementById(treeId);if(ul == null){return false;}var ret = expandCollapseList(ul,nodeOpenClass,itemId);if(ret){var o = document.getElementById(itemId);if(o.scrollIntoView){o.scrollIntoView(false);}}}
			function expandCollapseList(ul,cName,itemId){if(!ul.childNodes || ul.childNodes.length==0){return false;}for(var itemi=0;itemi<ul.childNodes.length;itemi++){var item = ul.childNodes[itemi];if(itemId!=null && item.id==itemId){return true;}if(item.nodeName == "LI"){var subLists = false;for(var sitemi=0;sitemi<item.childNodes.length;sitemi++){var sitem = item.childNodes[sitemi];if(sitem.nodeName=="UL"){subLists = true;var ret = expandCollapseList(sitem,cName,itemId);if(itemId!=null && ret){item.className=cName;return true;}}}if(subLists && itemId==null){item.className = cName;}}}}
			function convertTrees(){setDefault("treeClass","domol");setDefault("nodeClosedClass","liClosed");setDefault("nodeOpenClass","liOpen");setDefault("nodeBulletClass","liBullet");setDefault("nodeLinkClass","bullet");setDefault("preProcessTrees",true);if(preProcessTrees){if(!document.createElement){return;}uls = document.getElementsByTagName("ul");for(var uli=0;uli<uls.length;uli++){var ul=uls[uli];if(ul.nodeName=="UL" && ul.className==treeClass){processList(ul);}}}}
			function processList(ul){if(!ul.childNodes || ul.childNodes.length==0){return;}for(var itemi=0;itemi<ul.childNodes.length;itemi++){var item = ul.childNodes[itemi];if(item.nodeName == "LI"){var subLists = false;for(var sitemi=0;sitemi<item.childNodes.length;sitemi++){var sitem = item.childNodes[sitemi];if(sitem.nodeName=="UL"){subLists = true;processList(sitem);}} processNode(subLists,item); }}}
			function processNode(subLists,item) { var s= document.createElement("SPAN"); var t= '\u00A0'; s.className = nodeLinkClass; if(subLists) { if(item.className==null || item.className=="") { item.className = nodeClosedClass; } if(item.firstChild.nodeName=="#text") { t = t+item.firstChild.nodeValue; item.removeChild(item.firstChild); } s.onclick = function() { this.parentNode.className =(this.parentNode.className==nodeOpenClass) ? nodeClosedClass : nodeOpenClass; return false; } } else { item.className = nodeBulletClass; s.onclick = function() { return false; } } s.appendChild(document.createTextNode(t)); item.insertBefore(s,item.firstChild); }

			var initPageCalled = false;
				
			function initPage()
			{
				if (!initPageCalled)
				{
					initPageCalled = true;
					convertTrees();
				}
			}
			
			function debugMSG(message)
			{			
				var dw = document.getElementById('debugMessage');
				if (dw != null)
				{
					dw.innerHTML += message;
				}
			}
]]> 
			</script>
			<style type="text/css">
				@media screen, print { 
					/* Turn off list bullets */
					ul.domol  li { list-style: none; } 
					/* Control how "spaced out" the tree is */
					ul.domol, ul.domol ul , ul.domol li { margin-left:10px; padding:0px; }
					/* Provide space for our own "bullet" inside the LI */
					ul.domol  li           .bullet { padding-left: 15px; }
					/* Show "bullets" in the links, depending on the class of the LI that the link's in */
					ul.domol  li.liOpen    .bullet { cursor: pointer; background: url(../../../../misc/minus.gif)  center left no-repeat; }
					ul.domol  li.liClosed  .bullet { cursor: pointer; background: url(../../../../misc/plus.gif)   center left no-repeat; }
					ul.domol  li.liBullet  .bullet { cursor: default; background: url(../../../../misc/bullet.gif) center left no-repeat; }
					/* Sublists are visible or not based on class of parent LI */
					ul.domol  li.liOpen    ul { display: block; }
					ul.domol  li.liClosed  ul { display: none; }
					/* Format menu items differently depending on what level of the tree they are in */
					ul.domol  li { font-size: 12pt; }

					#footer { display: none; }


						
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
				}			
			</style>
		</head>
		<body style="color: Black; background-color: White; font-family: Arial, sans-serif; font-size: 10pt;">
			<h1 style="font-size: 18pt; letter-spacing: 2px;">SDMX-HD (Health Domain) v1.0 documentation<br /></h1>
			<h2>Hierarchical Code Lists: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/structure:Name" /></h2>
			<p>
			<ul>
				<li>Identifier: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@id"/></li>
				<li>Agency [version]: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@agencyID"/>[<xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@version"/>]</li>
				<li>IsFinal: <xsl:value-of select="//message:HierarchicalCodelists/structure:HierarchicalCodelist/@isFinal"/> (<xsl:choose>
					<xsl:when test="//message:HierarchicalCodelists/structure:HierarchicalCodelist[@isFinal='true']"><span style="color:red">read-only</span></xsl:when>
					<xsl:otherwise><span style="color:blue">can be modified</span></xsl:otherwise>
				</xsl:choose>)</li>
			</ul>
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
			<div id="DOMViewer" style="float:left; display: table-cell; height: 700px; width:90pc; padding: 0.5em;">
			  <table width="100%" border="0">
				<tr>
				  <td align="left" valign="top">
					<!-- Draw the DOMViewer -->
					<ul class="domol" id="DOMtree">
							<li class="liOpen"><img src='../../../../misc/DOM.gif' />DOM
									<ul>
											<li id='DOMhierarchicalcodelistsLI'><img src='../../../../misc/list.gif' />Hierarchical Code-Lists
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

	  </body>
	</html>
	</xsl:template>
	<xsl:template match="structure:CodeRef">
		<xsl:variable name="codelistfilename" select="substring-after(structure:CodelistAliasRef,'AL_')" />
		<xsl:variable name="codelistfile" select="concat('CL_',$codelistfilename,'.SDMX-HD.1.0.xml')"/>																	
		<xsl:variable name="codevalue" select="structure:CodeID"/>																
		<li><xsl:value-of select="structure:CodeID" /> - <xsl:value-of select="structure:CodelistAliasRef" /><img src='../../../../misc/ref.gif' alt='reference' /><xsl:value-of select="document($codelistfile)/message:Structure/message:CodeLists/structure:CodeList/structure:Code[@value=$codevalue]/structure:Description[@xml:lang='en']" />																	
		<xsl:if test="structure:CodeRef">															
			<ul>
			<xsl:apply-templates select="structure:CodeRef" />
			</ul>
		</xsl:if>
		</li>		
	</xsl:template>	
</xsl:stylesheet>