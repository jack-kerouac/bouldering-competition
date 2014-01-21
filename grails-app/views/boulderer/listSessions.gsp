<%@ page import="bcomp.gym.Grade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.boulderingSessions.label"/></title>
</head>

<body id="list-sessions-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s <g:message code="bcomp.boulderingSessions.label"/></h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <g:set var="grades"
               value="${bcomp.gym.Grade.FONT_GRADES.collect {[Grade.fromFontScale(it).value, "\"$it\""]} }"/>
        <div class="chart" style="width:940px; height:400px;" data-grades="${grades}"></div>
        <ul id="sessions">
            <g:each in="${sessions}" var="session">
                <li><tmpl:/shared/date date="${session.date}"/>:
                    <span class="grade">${session.currentGrade.value}</span></li>
            </g:each>
        </ul>
    </div>
</div>

</body>
</html>