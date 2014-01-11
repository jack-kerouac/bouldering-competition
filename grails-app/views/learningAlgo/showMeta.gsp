<%@ page import="bcomp.gym.Grade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
</head>

<body id="show-meta-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>Learning Algorithm meta data</h1>
    </div>
</div>

<div class="row content">
    <div class="large-6 column">
        <h2>Users</h2>
        <table class="users">
            <thead>
            <tr>
                <th>name</th>
                <th>init. grade</th>
                <th>ascents</th>
                <th>current grade</th>
                <th>var.</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${users.entrySet().sort({ it.key.username })}">
                <g:set var="user" value="${it.key}"/>
                <g:set var="ascents" value="${it.value}"/>
                <tr>
                    <td>${user.username}</td>
                    <td>${user.initialGrade}</td>
                    <td>${(ascents.collect({ "$it.boulder.id ($it.style)" }) as List).join(', ')}</td>
                    <td><span><g:formatNumber number="${user.currentGrade.value}" minFractionDigits="4"
                                              maxFractionDigits="4"/></span>
                        <span>(${user.currentGrade.toFontScale()})</span>
                    </td>
                    <td><g:formatNumber number="${user.currentGradeVariance}" minFractionDigits="4"
                                        maxFractionDigits="4"/>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <g:form action="resetCurrentUserGrades">
            <g:submitButton name="submit" class="button" value="reset users"/>
        </g:form>

    </div>

    <div class="large-6 column">
        <h2>Boulders</h2>
        <table class="boulders">
            <thead>
            <tr>
                <th>ID</th>
                <th>init. grade</th>
                <th>current grade</th>
                <th>var.</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${boulders.sort({ it.id })}">
                <g:set var="boulder" value="${it}"/>
                <tr>
                    <td>${boulder.id}</td>
                    <td>${boulder.initialGradeRangeLow} - ${boulder.initialGradeRangeHigh}</td>
                    <td><span><g:formatNumber number="${boulder.currentGrade.value}" minFractionDigits="4"
                                              maxFractionDigits="4"/></span>
                        <span>(${boulder.currentGrade.toFontScale()})</span>
                    </td>
                    <td><g:formatNumber number="${boulder.currentGradeVariance}" minFractionDigits="4"
                                        maxFractionDigits="4"/>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <div id="chartModal" class="reveal-modal" data-reveal>
            <a class="close-reveal-modal">&#215;</a>
            <g:set var="ticks"
                   value="${bcomp.gym.Grade.FONT_GRADES.collect {[Grade.fromFontScale(it).value, "\"$it\""]} }"/>

            <div class="chart" style="width:940px; height:400px; margin-bottom: 20px;" data-ticks="${ticks}"></div>
            <p>With a 90% chance, the grade is between the two vertical lines.</p>
        </div>
        <g:form action="resetCurrentBoulderGrades">
            <g:submitButton name="submit" class="button" value="reset boulders"/>
        </g:form>
    </div>

</div>

</body>
</html>