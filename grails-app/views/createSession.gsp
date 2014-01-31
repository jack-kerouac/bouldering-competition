<%@ page import="bcomp.Ascent" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></title>
</head>

<body id="log-session-page">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></h1>

        <p>Markers show the color of the boulder. Zoom in by double click (desktop) or pinching (mobile).</p>
    </div>
</div>

<form ng-controller="SessionCtrl" ng-submit="logSession()">
    <input type="hidden" name="boulderer.id" value="${sec.loggedInUserInfo(field: 'id')}"/>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.date.label"/></label>
        </div>

        <div class="small-9 column">
            <input type="date" ng-model="session.date">
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.gym.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="session.gym" ng-options="gym.name for gym in gyms" required></select>
        </div>
    </div>


    <div class="row">
        <div class="medium-12 small-11 column">
            <div class="boulder-location-map">
                <div class="map"></div>
            </div>
        </div>
    </div>

    <div class="row" ng-if="errors">
        <div class="medium-12 column">
            <ul class="error">
                <li ng-repeat="error in errors">
                    <small class="error">{{error}}</small>
                </li>
            </ul>
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