${pojo.getPackageDeclaration()}
<#assign classbody>
<#include "PojoTypeDeclaration.ftl"/> {

private static final long serialVersionUID = 1L;

<#if !pojo.isInterface()>

<#if pojo.getDeclarationName()?contains("Abstract") || pojo.isComponent()>
<#include "PojoFields.ftl"/>
</#if>

<#include "PojoConstructors.ftl"/>

<#if pojo.getDeclarationName()?contains("Abstract") || pojo.isComponent()>

<#include "PojoPropertyAccessors.ftl"/>

<#include "PojoToString.ftl"/>

<#include "PojoEqualsHashcode.ftl"/>
</#if>

<#else>
<#include "PojoInterfacePropertyAccessors.ftl"/>

</#if>
<#include "PojoExtraClassCode.ftl"/>

}
</#assign>

${pojo.generateImports()}
${classbody}