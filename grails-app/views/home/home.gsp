<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
</head>

<body id="home-page">

<div class="row content">
    <h1>Tasks</h1>

    <div class="medium-4 columns">
        <g:link resource="boulder" action="create" class="button">
            <i class="fi-plus"></i>
            <g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/>
        </g:link>
    </div>


    <div class="medium-4 columns">
        <g:link resource="boulderingSession" action="create" class="button">

            <i class="fi-pencil"></i>
            <g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/>
        </g:link>
    </div>

    <div class="medium-4 columns">
        <g:link controller="leaderboard" action="view" class="button">
            <i class="fi-results"></i>
            <g:message code="default.show.label" args="[message(code: 'bcomp.leaderboard.label')]"/>
        </g:link>
    </div>
</div>

<div class="row">
    <div class="medium-12 columns">
        <p>Sollte euch irgendwas auffallen, Fehler, Verbesserungsvorschläge, neue Ideen,
        dann benutzt die orange Sprechblase rechts unten! Danke. Florian</p>
    </div>
</div>

<footer>
    <div class="row">
        <div class="column medium-6">
            <p><small><g:link controller="gradeLearning" action="showMeta">Grade Learning Algorithm</g:link></small></p>
        </div>

        <div class="column medium-6">
            <p><small>${grails.util.Environment.getCurrentEnvironment()}</small></p>
        </div>
    </div>
</footer>

</body>
</html>