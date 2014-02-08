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

<form ng-controller="SessionCtrl" ng-submit="logSession()" ng-cloak>
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
        <div class="medium-8 column">
            <gym-floor-plan floor-plan="session.gym.floorPlans[0]" boulders="boulders"
                            boulder-click="select(boulder)"
                            selected="currentBoulder"></gym-floor-plan>
        </div>

        <div class="medium-4 column">
            <boulder-meta boulder="currentBoulder"></boulder-meta>

            <input id="flash" type="checkbox" ng-model="ascents[currentBoulder.id]" ng-true-value="flash"
                   ng-false-value="none" ng-change="removeAscentIfStyleNone(currentBoulder.id)"
                   ng-disabled="!currentBoulder">
            <label for="flash" ng-class="{inactive: !currentBoulder}">flash</label>
            <input id="top" type="checkbox" ng-model="ascents[currentBoulder.id]" ng-true-value="top"
                   ng-false-value="none" ng-change="removeAscentIfStyleNone(currentBoulder.id)"
                   ng-disabled="!currentBoulder">
            <label for="top" ng-class="{inactive: !currentBoulder}">top</label>
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
        <div class="medium-6 column">
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.log.label')}"/>
        </div>

        <div class="medium-6 column">
            <table>
                <thead>
                <tr>
                    <th>Boulder ID</th>
                    <th><g:message code="bcomp.boulder.color.label"/></th>
                    <th><g:message code="bcomp.boulder.currentGrade.label"/></th>
                    <th><g:message code="bcomp.ascent.style.label"/></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="(boulderId, style) in ascents" ng-click="select(boulder(boulderId))">
                    <td>{{boulderId}}</td>
                    <td>{{boulder(boulderId).color.germanName}}</td>
                    <td>{{boulder(boulderId).grade.mean.font}}</td>
                    <td>{{style}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</form>

</body>
</html>