<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${gym.name} - <g:message code="bcomp.leaderboard.label"/></title>
</head>

<body>

<div class="row">
    <div class="medium-12 columns">
        <h1>${gym.name} - <g:message code="bcomp.leaderboard.label"/></h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <table>
            <thead>
            <tr>
                <th><g:message code="bcomp.leaderboard.position.label"/></th>
                <th><g:message code="bcomp.userRole.boulderer.label"/></th>
                <th><g:message code="bcomp.leaderboard.score.label"/></th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${ranking.sort({it.position})}">
                <tr>
                    <td>${it.position}</td>
                    <td><g:link controller="boulderer" action="listAscents"
                                params="[username: it.boulderer.username]">${it.boulderer.username}</g:link></td>
                    <td>${it.score}: ${it.countTops} tops (${it.countFlashes} flash)</td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>