<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${message(code: 'bcomp.leaderboard.label')}</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>${message(code: 'bcomp.leaderboard.label')}</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <table>
            <thead>
                <tr>
                    <th>Position</th>
                    <th>Boulderer</th>
                    <th>Score</th>
                    <th>Last Session</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${ranking}">
                <tr>
                    <td>${it.position}</td>
                    <td>${it.boulderer.username}</td>
                    <td>${it.score} (${it.countFlashes} flash, ${it.countTops} tops)</td>
                    <td><g:formatDate type="date" date="${it.lastSession}"/></td>
                </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>