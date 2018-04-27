<#if property.name.equalsIgnoreCase("petId")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("id")
</#if>
<#if property.name.equalsIgnoreCase("petName")>
    @${pojo.importType("com.fasterxml.jackson.annotation.JsonProperty")}("name")
</#if>