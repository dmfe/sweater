<#import "parts/common.ftl" as c>
<#import "parts/auth.ftl" as a>

<@c.page>
    <div class="mb-1">Add new user</div>
    ${message?ifExists}
    <@a.auth "/registration" true />
</@c.page>