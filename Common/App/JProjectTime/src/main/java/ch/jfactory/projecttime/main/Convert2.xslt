<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="entries">
        <xsl:apply-templates select="entry[1]"/>
    </xsl:template>

    <xsl:template match="entry[@reference]">
        <xsl:variable name="ref" select="./@reference"/>
        <xsl:apply-templates select="//entry[@id=$ref]"/>
    </xsl:template>

    <xsl:template match="* | @*">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
        <!-- Stylus Studio meta-information - (c) 2004-2005. Progress Software Corporation. All rights reserved.
        <metaInformation>
        <scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\project\stylus\Temp1.xml" htmlbaseurl="" outputurl="..\..\..\..\..\..\project\stylus\Temp2.xml" processortype="saxon8" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition></MapperBlockPosition></MapperMetaTag>
        </metaInformation>
        -->