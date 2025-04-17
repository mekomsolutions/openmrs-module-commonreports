<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" 
    xmlns:barcode="http://barcode4j.krysalis.org/ns">

    <!-- Attribute for dynamic page height (default to 50mm if not provided) -->
    <xsl:variable name="sticker-height" select="/patientIdStickers/@sticker-height"/>
    <xsl:variable name="sticker-width" select="/patientIdStickers/@sticker-width"/>
    <!-- Calculate effective body height (page height minus margins) -->
    <xsl:variable name="body-height" select="concat(number(substring-before($sticker-height, 'mm')) - 4, 'mm')"/>
    
    <!-- Calculate section heights -->
    <xsl:variable name="header-height" select="concat(number(substring-before($body-height, 'mm')) * 0.2, 'mm')"/>
    <xsl:variable name="barcode-height" select="concat(number(substring-before($body-height, 'mm')) * 0.2, 'mm')"/>
    <xsl:variable name="table-height" select="concat(number(substring-before($body-height, 'mm')) * 0.6, 'mm')"/>

    <xsl:template match="patientIdStickers">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="sticker" 
                    page-width="{$sticker-width}" page-height="{$sticker-height}"
                    margin="1.5mm">
                    <fo:region-body margin="0"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <xsl:apply-templates select="patientIdSticker"/>
        </fo:root>
    </xsl:template>

    <xsl:template match="patientIdSticker">
        <fo:page-sequence master-reference="sticker">
            <fo:flow flow-name="xsl-region-body">
                <!-- Main container with dynamic height -->
                <fo:block-container height="{$body-height}" display-align="distribute">
                    <!-- Header (20%) -->
                    <xsl:if test="header">
                        <fo:block-container height="{$header-height}" display-align="center">
                            <fo:block>
                                <fo:table width="100%">
                                    <fo:table-column column-width="50%"/>
                                    <fo:table-column column-width="50%"/>
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <xsl:if test="header/branding/logo != ''">
                                                    <fo:block>
                                                        <fo:external-graphic 
                                                            max-width="30mm" 
                                                            max-height="{number(substring-before($header-height, 'mm')) - 1}mm"
                                                            content-width="scale-to-fit"
                                                            scaling="uniform"
                                                            src="{header/branding/logo}"/>
                                                    </fo:block>
                                                </xsl:if>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block text-align="right" font-size="7pt" line-height="8pt">
                                                    <xsl:value-of select="header/headerText"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </fo:block-container>
                    </xsl:if>
                    
                    <!-- Barcode (10%) -->
                    <xsl:if test="barcode">
                        <fo:block-container height="{$barcode-height}" display-align="center">
                            <fo:block text-align="center">
                                <fo:instream-foreign-object scaling="uniform" 
                                    content-height="{number(substring-before($barcode-height, 'mm')) - 1}mm">
                                    <barcode:barcode message="{barcode/@barcodeValue}">
                                        <barcode:code39>
                                            <!-- <barcode:human-readable>false</barcode:human-readable> -->
                                        </barcode:code39>
                                    </barcode:barcode>
                                </fo:instream-foreign-object>
                            </fo:block>
                        </fo:block-container>
                    </xsl:if>
                    
                    <!-- Table (70%) -->
                    <fo:block-container height="{$table-height}" display-align="before" overflow="hidden">
                        <xsl:apply-templates select="fields"/>
                    </fo:block-container>
                </fo:block-container>
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>

    <xsl:template match="fields">
        <!-- Calculate row height based on number of fields -->
        <xsl:variable name="row-height" select="number(substring-before($table-height, 'mm')) div count(field)"/>
        
        <!-- Dynamic font sizing based on row height -->
        <xsl:variable name="font-size">
            <xsl:choose>
                <xsl:when test="$row-height > 5">7pt</xsl:when>
                <xsl:when test="$row-height > 3">6pt</xsl:when>
                <xsl:otherwise>5pt</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <!-- Calculate line height as 80% of font size to reduce spacing -->
        <xsl:variable name="line-height" select="concat(round($font-size * 0.8 * 10) div 10, 'pt')"/>
            
        <fo:block>
            <fo:table width="100%" table-layout="fixed">
                <fo:table-column column-width="40%"/>
                <fo:table-column column-width="60%"/>
                <fo:table-body>
                    <xsl:for-each select="field">
                        <fo:table-row height="{concat($row-height - 1, 'mm')}">
                        <!-- <fo:table-row> -->
                            <fo:table-cell padding="0.5mm" display-align="center">
                                <fo:block font-size="{$font-size}" line-height="{$line-height}" font-style="italic" wrap-option="wrap">
                                    <xsl:value-of select="@label"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="0.5mm" display-align="center">
                                <fo:block font-size="{$font-size}" line-height="{$line-height}" font-weight="bold" wrap-option="wrap">
                                    <xsl:value-of select="."/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

</xsl:stylesheet>
