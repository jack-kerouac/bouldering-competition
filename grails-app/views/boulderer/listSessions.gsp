<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.boulderingSessions.label"/> in ${gym.name}</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s <g:message code="bcomp.boulderingSessions.label"/> in ${gym.name}</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <table>
            <thead>
                <tr>
                    <th><g:message code="bcomp.date.label"/></th>
                    <th><g:message code="bcomp.ascents.label"/></th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${sessions}">
                <tr>
                    <td><g:formatDate type="date" date="${it.date}"/></td>
                    <td>${it.ascents.size()}</td>
                </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>