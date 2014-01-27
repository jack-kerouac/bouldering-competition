<%@ page import="bcomp.gym.Grade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.statistics.label"/></title>
</head>

<body id="list-sessions-page" ng-controller="StatisticsCtrl">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s <g:message code="bcomp.statistics.label"/></h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <input type="hidden" name="boulderer.id" value="${boulderer.id}"/>

        <p class="about-boulderer">
            <g:message code="bcomp.user.registration"/>: {{ user.registrationDate | amDateFormat: 'LLLL' }}<br/>
            <g:message code="bcomp.user.initialGrade"/>:  {{ user.initialGrade.font }}</span>
        </p>

        <h2><g:message code="bcomp.boulderingSessions.label"/></h2>

        <table id="sessions">
            <thead>
            <tr>
                <th><g:message code="bcomp.date.label"/></th>
                <th class="grade"><g:message code="bcomp.user.currentGrade.label"/></th>
                <th class="ascent-count"><g:message code="bcomp.ascents.label"/></th>
            </tr>
            </thead>
            <tbody>
                <tr ng-repeat="stat in statistics">
                    <td>{{ stat.session.date | amDateFormat: 'LLLL'}}</td>
                    <td>{{ stat.grade.mean.font }}</td>
                    <td>{{ stat.session.ascents.length }}</td>
                </tr>
            </tbody>
        </table>
        <div flot-chart data="chart.data" options="chart.options"/>
    </div>
</div>

</body>
</html>