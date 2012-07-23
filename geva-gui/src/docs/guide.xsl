<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/guide">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title><xsl:value-of select="@title" /></title>
        <style type="text/css">
          body    { font-family:      tahoma    }
          table   { font-size:        100%      }
          div     { margin-left:      1em       }
          div.c   { margin:           0px       ;
                    width:            100%      ;
                    overflow:         hidden    }
          div.s   { width:            auto      ;
                    background-color: #eee      ;
                    border-style:     dashed    ;
                    border-top-style: none      ;
                    border-width:     1px       ;
                    padding:          0.5em     ;
                    float:            right     }
          div.s a { color:            black     ;
                    text-decoration:  none      ;
                    border-style:     none      }
          div.h   { margin-left:      0px       ;
                    background-color: #eee      ;
                    overflow:         hidden    ;
                    height:           0.25em    }
          th      { text-align:       left      }
          h1      { background-color: #eee      ;
                    border-style:     dashed    ;
                    border-width:     1px       ;
                    font-size:        150%      ;
                    margin:           0px       ;
                    margin-top:       1em       }
          h2      { background-color: #ffe      ;
                    font-size:        125%      ;
                    margin:           0px       }
          p       { text-align:       justify   ;
                    margin:           0.5em     ;
                    text-indent:      -0.5em    ;
                    margin-left:      0.5em     }
          ul      { text-align:       justify   ;
                    list-style-type:  none      ;
                    text-indent:      -1em      ;
                    margin:           0px       ;
                    padding-left:     1em       }
          ul.p    { list-style-type:  disc      ;
                    text-indent:      0px       ;
                    margin:           0px       ;
                    padding-left:     2em       }
          li.p    { font-weight:      bold      }
          li.p p  { font-weight:      normal    ;
                    display:          inline    }
          a       { font-style:       italic    ;
                    text-decoration:  none      ;
                    border-style:     solid     ;
                    border-width:     1px       ;
                    border-color:     white     }
          a:hover { background-color: #eef      ;
                    border-color:     #cce      }
          a.o     { text-decoration:  underline }
        </style>
      </head>
      <body>
       <xsl:apply-templates select="chapter" />
      </body>
    </html>
  </xsl:template>

  <xsl:template match="p">
    <p><xsl:call-template name="text" /></p>
  </xsl:template>

  <xsl:template match="link">
    <xsl:choose>
      <xsl:when test="not(text())">
        <a><xsl:attribute name="href">#<xsl:value-of select="@name" /></xsl:attribute><xsl:value-of select="@name" /></a>
      </xsl:when>
      <xsl:otherwise>
        <a class="o"><xsl:attribute name="href"><xsl:call-template name="text" /></xsl:attribute><xsl:value-of select="@name" /></a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="chapter">
    <a><xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute></a>
    <h1><xsl:value-of select="@name" /></h1>
    <div>
      <div class="c">
        <xsl:apply-templates select="related" />
        <xsl:apply-templates select="p|hr|list|toc" />
      </div>
    </div>
  </xsl:template>

  <xsl:template match="related">
    <div class="s">
      <u>Related files:</u>
      <ul>
        <xsl:apply-templates select="file" />
      </ul>
    </div>
  </xsl:template>

  <xsl:template match="file">
    <li>
      <xsl:choose>
        <xsl:when test="@location">
          <a>
            <xsl:attribute name="href"><xsl:choose><xsl:when test="@geva='true'">../../GEVA/src/</xsl:when><xsl:otherwise>../src/</xsl:otherwise></xsl:choose><xsl:value-of select="translate(string(@location), '.:', '/.')" />/<xsl:value-of select="@name" />
            <xsl:if test="not(contains(@name, '.'))">.java</xsl:if></xsl:attribute>
            <xsl:attribute name="title"><xsl:value-of select="@location" /></xsl:attribute>
            <xsl:value-of select="@name" />
          </a>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@name" />
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="file">
        <ul>
          <xsl:apply-templates select="file" />
        </ul>
      </xsl:if>
    </li>
  </xsl:template>

  <xsl:template match="list">
    <ul class="p">
      <xsl:apply-templates select="item" />
    </ul>
  </xsl:template>

  <xsl:template match="item">
    <xsl:choose>
      <xsl:when test="@name">
        <li class="p"><xsl:value-of select="@name" />
          <xsl:choose>
            <xsl:when test="text()|c">
              <p><xsl:apply-templates select="*[1]" /></p>
              <xsl:apply-templates select="node()[2]" mode="group"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="node()[1]" mode="group"/>
            </xsl:otherwise>
          </xsl:choose>
        </li>
      </xsl:when>
      <xsl:otherwise>
        <li>
          <xsl:apply-templates select="node()[1]" mode="group"/>
        </li>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

   <xsl:template match="item" mode="group">
     <ul class="p">
       <xsl:apply-templates select="."/>
       <xsl:apply-templates select="following-sibling::node()[1][self::item]" mode="groupcont"/>
     </ul>
     <xsl:apply-templates select="following-sibling::node()[not(self::item)][1]" mode="group"/>
   </xsl:template>

   <xsl:template match="item" mode="groupcont">
     <xsl:apply-templates select="."/>
     <xsl:apply-templates select="following-sibling::node()[1][self::item]" mode="groupcont"/>
   </xsl:template>

   <xsl:template match="node()[not(self::item)]" mode="group">
     <xsl:apply-templates select="."/>
     <xsl:apply-templates select="following-sibling::node()[1]" mode="group"/>
   </xsl:template>

  <xsl:template match="hr">
    <xsl:if test="@title">
      <a><xsl:attribute name="name"><xsl:value-of select="../@name" />_<xsl:value-of select="@title" /></xsl:attribute></a>
    </xsl:if>   
    <div class="h"></div>
    <xsl:if test="@title">
      <h2><xsl:value-of select="@title" /></h2>
    </xsl:if>
  </xsl:template>

  <xsl:template match="toc">
    <table>
      <tr><th>Main Section</th><th>Sub-sections</th></tr>
      <xsl:call-template name="build_toc" />
    </table>
  </xsl:template>

  <xsl:template name="build_toc">
    <xsl:param name="depends" select="'null'" />
    <xsl:param name="indent" select="0" />
      <xsl:choose>
        <xsl:when test="$depends='null'">
          <xsl:for-each select="../../chapter[not(@depends) and not(toc)]">
            <tr><td><a><xsl:attribute name="href">#<xsl:value-of select="@name" /></xsl:attribute><xsl:value-of select="@name" /></a></td>
            <td>
              <xsl:if test="hr[@title]">
                <span style="font-size: 80%">
                <xsl:for-each select="hr[@title]">
                  <xsl:if test="position()>1"> . </xsl:if>
                  <a><xsl:attribute name="href">#<xsl:value-of select="../@name" />_<xsl:value-of select="@title" /></xsl:attribute><xsl:value-of select="@title" /></a>
                </xsl:for-each>
                </span>
              </xsl:if>
            </td></tr>
            <xsl:call-template name="build_toc">
              <xsl:with-param name="depends" select="@name" />
              <xsl:with-param name="indent" select="$indent+1" />
            </xsl:call-template>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:for-each select="/guide/chapter[@depends=string($depends)]">
            <tr><td><xsl:attribute name="style">text-indent: <xsl:value-of select="$indent" />em</xsl:attribute><a><xsl:attribute name="href">#<xsl:value-of select="@name" /></xsl:attribute><xsl:value-of select="@name" /></a></td>
            <td>
              <xsl:if test="hr[@title]">
                <span style="font-size: 80%">
                <xsl:for-each select="hr[@title]">
                  <xsl:if test="position()>1"> . </xsl:if>
                  <a><xsl:attribute name="href">#<xsl:value-of select="../@name" />_<xsl:value-of select="@title" /></xsl:attribute><xsl:value-of select="@title" /></a>
                </xsl:for-each>
                </span>
              </xsl:if>
            </td></tr>
            <xsl:call-template name="build_toc">
              <xsl:with-param name="depends" select="@name" />
              <xsl:with-param name="indent" select="$indent+1" />
            </xsl:call-template>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="text">
    <xsl:apply-templates select="text()|b|i|p|c|link" />
  </xsl:template>

  <xsl:template match="text()">
    <xsl:value-of select="." />
  </xsl:template>

  <xsl:template match="b">
    <b><xsl:call-template name="text" /></b>
  </xsl:template>

  <xsl:template match="i">
    <i><xsl:call-template name="text" /></i>
  </xsl:template>

  <xsl:template match="c">
    <code style="color: blue;">
      <xsl:apply-templates select="text()|a|b|i" mode="code" />
    </code>
  </xsl:template>

  <xsl:template match="text()" mode="code">
    <xsl:value-of select="." />
  </xsl:template>

  <xsl:template match="a" mode="code">
    <u><xsl:apply-templates select="text()|a|b" mode="code" /></u>
  </xsl:template>

  <xsl:template match="b" mode="code">
    <b><xsl:apply-templates select="text()|a|b" mode="code" /></b>
  </xsl:template>

  <xsl:template match="i" mode="code">
    <i><xsl:call-template name="text" /></i>
  </xsl:template>

</xsl:stylesheet>
