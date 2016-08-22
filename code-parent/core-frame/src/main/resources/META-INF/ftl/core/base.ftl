<html class="no-js" lang="zh"><!--<![endif]-->
<head>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit">
	<meta name="force-rendering" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<#-- Force latest IE rendering engine or ChromeFrame if installed -->
	<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
	<!--  host name: -->
	<title>rensframework</title>
	
	<#include "/core/include/js_jquery.ftl">
	<#include "/core/include/js_easyui.ftl">
</head>
<body class="easyui-layout">
        <!-- 北方 -->  
        <div data-options="region:'north'" style="height: 40px;background-color:#95B8E7;">  
            <#include "manage/top.ftl">
        </div>  
        <!-- 西方 -->  
        <div data-options="region:'west',split:true" title="BBBBBB" style="width: 200px;">  
            <div class="easyui-accordion"  data-options="fit:true,border:false">  
                  <#include "manage/menu.ftl">
            </div>  
        </div>  
        <!-- 中间 -->  
        <div data-options="region:'center',title:'Center',iconCls:'icon-ok'">  
            <div id="tTabs" class="easyui-tabs" data-options="fit:true,border:false,plain:true">  
                <div title="DDDDD" data-options="closable:true" style="overflow: auto; padding: 20px;">  
                <#include "manage/main.ftl">
                </div>  
            </div>  
        </div>  
        <!-- 南方 -->  
        <div data-options="region:'south',split:true" style="height:10%;background-color:#95B8E7;">  
            <div style="" align="center">
            	<#include "manage/foot.ftl">
            </div>  
        </div>  
</body>
</html>