<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>Learning Algorithm meta data</h1>
    </div>
</div>

<div class="row content">
    <div class="large-6 column">
        <h2>Users</h2>
        <table>
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
                    <td>${user.currentGrade}</td>
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
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>initial grade</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${boulders.sort({ it.id })}">
                <tr>
                    <td>${it.id}</td>
                    <td>${it.gradeRangeLow} - ${it.gradeRangeHigh}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>