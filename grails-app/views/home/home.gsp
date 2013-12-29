<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>Bouldering Competition</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-4 column">
        <g:link controller="boulder" action="createForm" class="button">
            <g:message code="default.new.label" args="[message(code: 'bcomp.boulder.label')]"/>
        </g:link>
    </div>

    <div class="medium-4 column">
        <g:link controller="ascent" action="createForm" class="button">
            <g:message code="default.new.label" args="[message(code: 'bcomp.ascent.label')]"/>
        </g:link>
    </div>

    <div class="medium-4 column">
        <g:link controller="leaderboard" action="view" class="button">
            <g:message code="default.show.label" args="[message(code: 'bcomp.leaderboard.label')]"/>
        </g:link>
    </div>
</div>

</body>
</html>