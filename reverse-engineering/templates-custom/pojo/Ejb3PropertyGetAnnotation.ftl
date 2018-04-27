<#if ejb3>
<#if pojo.hasIdentifierProperty()>
<#if property.equals(clazz.identifierProperty)>
 ${pojo.generateAnnIdGenerator()}
<#-- if this is the id property (getter)-->
<#-- explicitly set the column name for this property-->
</#if>
</#if>
<#if c2h.isOneToOne(property)>
@${pojo.importType("com.fasterxml.jackson.annotation.JsonIgnore")}
${pojo.generateOneToOneAnnotation(property, cfg)}
<#elseif c2h.isManyToOne(property)>
@${pojo.importType("com.fasterxml.jackson.annotation.JsonIgnore")}
${pojo.generateManyToOneAnnotation(property)}
<#--TODO support optional and targetEntity-->
${pojo.generateJoinColumnsAnnotation(property, cfg)}
<#elseif c2h.isCollection(property)>
@${pojo.importType("com.fasterxml.jackson.annotation.JsonIgnore")}
${pojo.generateCollectionAnnotation(property, cfg)}
<#else>
${pojo.generateBasicAnnotation(property)}
${pojo.generateAnnColumnAnnotation(property)}
</#if>
<#attempt>
	<#include "custom-pojo/${pojo.getDeclarationName()}PropertyGetAnnotation.ftl"/>
	<#recover>
</#attempt>
</#if>