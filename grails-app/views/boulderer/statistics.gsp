<%@ page import="bcomp.gym.Grade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.statistics.label"/></title>
</head>

<body id="list-sessions-page">

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
               value="${bcomp.gym.Grade.FONT_GRADES.collect {[Grade.fromFontScale(it).value, "\"$it\""]} }"/>
        <div class="chart" style="width:940px; height:400px;" data-grades="${grades}"></div>
        <ul id="sessions">
            <g:each in="${stats}" var="stat">
                <li><tmpl:/shared/date date="${stat.session.date}"/>:
                    <span class="grade">${stat.currentGrade.value}</span></li>
            </g:each>
        </ul>
    </div>
</div>

</body>
</html>