<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${message(code: 'bcomp.leaderboard.label')}</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s ascents in ${gym.name}</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Section</th>
                    <th>Grade</th>
                    <th>Style</th>
                    <th>Tries</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${ascents}">
                <tr>
                    <td><g:formatDate type="date" date="${it.date}"/></td>
                    <td>${it.boulder.section.name}</td>
                    <td>${it.boulder.grade}</td>
                    <td>${it.style}</td>
                    <td>${it.tries}</td>
                </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>