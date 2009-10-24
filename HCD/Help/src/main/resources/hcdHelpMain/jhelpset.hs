<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
        "http://java.sun.com/products/javahelp/helpset_2_0.dtd">
<helpset>
    <title>Herbar CD-ROM Version 2</title>
    <maps>
        <homeID>top</homeID>
        <mapref location="jhelpmap.jhm"></mapref>
    </maps>
    <view mergetype="javax.help.UniteAppendMerge">
        <name>TOC</name>
        <label>Table Of Contents</label>
        <type>javax.help.TOCView</type>
        <data>jhelptoc.xml</data>
    </view>
    <view>
        <name>Index</name>
        <label>Index</label>
        <type>javax.help.IndexView</type>
        <data>jhelpidx.xml</data>
    </view>
    <view>
        <name>Search</name>
        <label>Search</label>
        <type>javax.help.SearchView</type>
        <data engine="com.sun.java.help.search.DefaultSearchEngine">JavaHelpSearch</data>
    </view>
    <subhelpset location="../hcdHelpGui/jhelpset.hs"/>
    <subhelpset location="../hcdHelpAttributes/jhelpset.hs"/>
    <subhelpset location="../hcdHelpAppendix/jhelpset.hs"/>
</helpset>
