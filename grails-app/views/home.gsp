<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Home</title>
</head>

<body id="home-page">

<div class="row content">
    <h1>Tasks</h1>

    <div class="medium-6 columns">
        <a href="/createBoulders" class="button">
            <i class="fi-plus"></i>
            <g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/>
        </a>
    </div>


    <div class="medium-6 columns">
        <a href="/modifyBoulder" class="button">
            <i class="fi-pencil"></i> Boulder ändern
        </a>
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
            <p><small><g:link controller="gradeLearning" action="showMeta">Grade Learning
            Algorithm</g:link></small></p>
        </div>

        <div class="column medium-6">
            <p><small>${grails.util.Environment.getCurrentEnvironment()}</small></p>
        </div>
    </div>
</footer>

</body>
</html>