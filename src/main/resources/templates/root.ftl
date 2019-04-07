<#import "parts/common.ftl" as c>
<#import "parts/auth.ftl" as a>

<@c.page>
    <div>
        <@a.logout />
    </div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Input message">
            <input type="text" name="tag" placeholder="Tag">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Post</button>
        </form>
    </div>
    <div>Messages List</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter}"/>
        <button type="submit">Find</button>
    </form>

    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else>
        No messages
    </#list>
</@c.page>