<#include "/core/include/include_js_init.ftl">
<#if !jsInclude?seq_contains("jquery")>
<script type="text/javascript" src="${rc.contextPath}/static/js/jquery.min.js"></script>
<#assign jsInclude = jsInclude+["jquery"]>
</#if>