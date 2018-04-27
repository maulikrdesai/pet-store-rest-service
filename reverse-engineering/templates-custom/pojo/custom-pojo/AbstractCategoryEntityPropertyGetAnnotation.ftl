<#if property.name.equalsIgnoreCase("categoryId")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("id")
</#if>
<#if property.name.equalsIgnoreCase("categoryName")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("name")
</#if>