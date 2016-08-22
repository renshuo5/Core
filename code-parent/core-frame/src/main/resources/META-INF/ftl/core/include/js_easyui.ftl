<#include "/core/include/include_js_init.ftl">
<#include "/core/include/js_jquery.ftl">
<#if !jsInclude?seq_contains("easyui")>
<link rel="stylesheet" type="text/css" href="${rc.contextPath}/static/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="${rc.contextPath}/static/easyui/themes/icon.css">  
<script type="text/javascript" src="${rc.contextPath}/static/easyui/jquery.easyui.min.js"></script>  
<script type="text/javascript" src="${rc.contextPath}/static/easyui/easyui-lang-zh_CN.js"></script>

<#assign jsInclude = jsInclude+["easyui"]>
</#if>