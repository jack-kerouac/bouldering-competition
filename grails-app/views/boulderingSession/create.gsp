<%@ page import="bcomp.Ascent" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></title>
</head>

<body id="log-session-page" ng-controller="SessionCtrl">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></h1>

        <p>Markers show the color of the boulder. Zoom in by double click (desktop) or pinching (mobile).</p>
    </div>
</div>

<form ng-submit="logSession()">
    <input type="hidden" name="boulderer.id" value="${boulderer.id}"/>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.date.label"/></label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'date', 'error')}">
            <input name="date" type="date" ng-model="date">
            <tmpl:/shared/fieldError field="date"/>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.gym.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="gym" ng-options="gym.name for gym in gyms" required></select>
            <input type="hidden" name="gym.id" value="{{gym.id}}"/>

            <tmpl:/shared/fieldError field="gym"/>
        </div>
    </div>


    <div class="row">

        <div class="medium-12 small-11 column">
            <div class="boulder-location-map">
                <div class="map"></div>

                <ul>
                    <li ng-repeat="(boulderId, style) in ascents">
                        {{boulderId}}, {{style}}
                    </li>
                </ul>
            </div>
            <tmpl:/shared/fieldError field="boulder"/>
        </div>
    </div>

    <div class="row">
        <div class="small-9 small-offset-3">
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.log.label')}"/>
        </div>
    </div>
</form>

</body>
</html>