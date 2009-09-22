<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

	<xsl:template match="*:ProjectEntry | *:TimeEntry">
		<entry id="{substring(@id, 5)}">
			<name>
				<xsl:value-of select="name"/>
			</name>
			<xsl:if test="categroy/@id">
				<type class="string" id="{substring(categroy/@id, 5)}">
					<xsl:value-of select="categroy"/>
				</type>
			</xsl:if>
			<xsl:if test="categroy/@href">
				<type class="string" reference="{substring(categroy/@href, 6)}">
					<xsl:value-of select="categroy"/>
				</type>
			</xsl:if>
			<xsl:if test="parent/@href">
				<parent class="entry">
					<xsl:attribute name="reference" select="substring(parent/@href, 6)"/>
				</parent>
			</xsl:if>
			<xsl:if test="entries/@href">
				<children id="{substring(entries/@href, 6)}">
					<xsl:variable name="id1" select="substring(entries/@href, 2)"/>
					<xsl:variable name="id2" select="substring(../*:ProjectEntry_x002B_ProjectEntryCollection[@id=$id1]/CollectionBase_x002B_list/@href, 2)"/>
					<xsl:variable name="id3" select="substring(../*:ArrayList[@id=$id2]/_items/@href, 2)"/>
					<xsl:apply-templates select="../*:Array[@id=$id3]/item"/>
				</children>
			</xsl:if>
			<xsl:if test="start">
				<start>
					<xsl:value-of select="start"/>
				</start>
			</xsl:if>
			<xsl:if test="end">
				<end>
					<xsl:value-of select="end"/>
				</end>
			</xsl:if>
		</entry>
	</xsl:template>

	<xsl:template match="item">
		<entry reference="{substring(@href, 6)}"/>
	</xsl:template>

	<xsl:template match="*:Envelope">
		<entries>
			<xsl:apply-templates select="*/*:ProjectEntry|*/*:TimeEntry"/>
		</entries>
	</xsl:template>

	<xsl:template match="*[.=Envelope]">
		<xsl:element name="{local-name()}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:attribute name="{local-name()}">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2005. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\..\..\..\Arbeitszeit.xml" htmlbaseurl="" outputurl="..\..\..\..\..\..\project\stylus\Temp1.xml" processortype="saxon8" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition></MapperBlockPosition></MapperMetaTag>
</metaInformation>
-->