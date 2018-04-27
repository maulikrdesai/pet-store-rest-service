<#if property.name.equalsIgnoreCase("tagId")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("id")
</#if>
<#if property.name.equalsIgnoreCase("tagName")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("name")
</#if>