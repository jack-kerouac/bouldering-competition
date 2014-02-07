<%@ page import="bcomp.gym.Boulder" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/></title>
</head>

<body id="create-boulder-page">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/></h1>

        <p>Wähle die Farbe des Boulders aus, den Grad und klicke dann auf den Grundriss unten um den Start der
        Boulder zu markieren. Es können beliebig viele Boulder mit der gewählten Farbe und dem gewählten Grad gesetzt
        werden. In Chrome muss man die Karte kurz verschieben/zoomen, damit die gesetzten Marker auftauchen...
        </p>
    </div>
</div>

<form ng-controller="BoulderCtrl" ng-submit="registerBoulders()">

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.gym.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="boulders.gym" ng-options="gym.name for gym in gyms" required></select>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.boulder.color.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="boulders.color" required>
                %{-- TODO: use proper locale here --}%
                <option ng-repeat="color in boulders.gym.colors" value="{{color.name}}">{{color.germanName}}</option>
            </select>
        </div>
    </div>


    <div class="row grades">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.boulder.initialGrade.label"/></label>
        </div>

        <div class="small-9 column">
            <p><small>use grades: <span ng-repeat="grade in grades">{{ grade.font }},</span></small></p>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" id="assigned-grade" ng-model="boulders.gradeCertainty" value="ASSIGNED">
                    <label for="assigned-grade" class="inline"><g:message
                            code="bcomp.boulder.gradeCertainty.assigned.label"/></label>
                    <input type="text" ng-model="boulders.grade" placeholder="6b+"/>
                </div>
            </div>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" id="grade-range" ng-model="boulders.gradeCertainty" value="RANGE">
                    <label for="grade-range" class="inline">
                        <g:message code="bcomp.boulder.gradeCertainty.range.label"/>
                    </label>
                    <input type="text" ng-model="boulders.gradeRangeLow" placeholder="6a+"/>
                    -
                    <input type="text" ng-model="boulders.gradeRangeHigh" placeholder="6c+"/>
                </div>
            </div>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" id="unknown-grade" ng-model="boulders.gradeCertainty" value="UNKNOWN">
                    <label for="unknown-grade" class="inline">
                        <g:message code="bcomp.boulder.gradeCertainty.unknown.label"/>
                    </label>
                </div>
            </div>
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
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.set.label')}"/>
        </div>
    </div>
</form>

</body>
</html>