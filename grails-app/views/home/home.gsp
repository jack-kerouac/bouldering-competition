<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Bouldering Competition</title>
</head>

<body id="home-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>Bouldering Competition</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-6 column">
        <g:link controller="boulder" action="createForm" class="button">create boulder</g:link>
    </div>

    <div class="medium-6 column">
        <g:link controller="ascent" action="createForm" class="button">create ascent</g:link>
    </div>
</div>


</body>
</html>