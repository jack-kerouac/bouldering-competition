<%@ page import="bcomp.gym.Grade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.statistics.label"/></title>
</head>

<body id="list-sessions-page"  ng-controller="StatisticsCtrl">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s <g:message code="bcomp.statistics.label"/></h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <p class="about-boulderer">
            <g:message code="bcomp.user.registration"/>: <tmpl:/shared/date date="${boulderer.registrationDate}"/><br/>
            <g:message code="bcomp.user.initialGrade"/>: <span class="grade">${boulderer.initialGrade.value}</span>
        </p>

        <g:set var="grades"
               value="${bcomp.gym.Grade.FONT_GRADES.collect { [Grade.fromFontScale(it).value, "\"$it\""] }}"/>
        <h2><g:message code="bcomp.boulderingSessions.label"/></h2>

        <div class="chart" data-grades="${grades}"></div>
        <table id="sessions">
            <thead>
            <tr>
                <th><g:message code="bcomp.date.label"/></th>
                <th class="grade"><g:message code="bcomp.user.currentGrade.label"/></th>
                <th class="ascent-count"><g:message code="bcomp.ascents.label"/></th>
                %{-- TODO: what about score? --}%
                %{--<th class="score"><g:message code="bcomp.leaderboard.score.label"/></th>--}%
            </tr>
            </thead>
            <tbody>
            <g:each in="${stats}" var="stat">
                <tr>
                    <td><tmpl:/shared/date date="${stat.session.date}"/></td>
                    <td class="grade">${stat.grade.mean.value}</td>
                    <td class="ascent-count">${stat.session.ascents.size()}</td>
                    %{--<td class="score">320</td>--}%
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>