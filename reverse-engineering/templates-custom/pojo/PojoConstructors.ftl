<#--  /** default constructor */ -->
public ${pojo.getDeclarationName()}() {
}

<#if pojo.needsMinimalConstructor()>    <#-- /** minimal constructor */ -->
public ${pojo.getDeclarationName()}(${c2j.asParameterList(pojo.getPropertyClosureForMinimalConstructor(), jdk5, pojo)}) {
    <#if pojo.getDeclarationName()?contains("Abstract") || pojo.isComponent()>
        <#foreach field in pojo.getPropertiesForMinimalConstructor()>
        this.${field.name} = ${field.name};
        </#foreach>
    <#else>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForMinimalConstructor())});        
    </#if>
}
</#if>    

<#if pojo.needsFullConstructor()>
<#-- /** full constructor */ -->
public ${pojo.getDeclarationName()}(${c2j.asParameterList(pojo.getPropertyClosureForFullConstructor(), jdk5, pojo)}) {
    <#if pojo.getDeclarationName()?contains("Abstract") || pojo.isComponent()>
        <#foreach field in pojo.getPropertiesForFullConstructor()> 
        this.${field.name} = ${field.name};
        </#foreach>
    <#else>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForFullConstructor())});        
    </#if>
}
</#if>