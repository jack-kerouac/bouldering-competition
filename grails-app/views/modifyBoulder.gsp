<%@ page import="bcomp.Ascent" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Boulder ändern</title>
</head>

<body id="modify-boulder-page">
<div ng-controller="ModifyBoulderCtrl">

    <div class="row">
        <div class="small-12 column">
            <h1>Boulder ändern</h1>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.gym.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="gym" ng-options="gym.name for gym in gyms" required></select>
        </div>
    </div>


    <div class="row">
        <div class="medium-8 column">
            <gym-floor-plan floor-plan="gym.floorPlans[0]" boulders="boulders"
                            boulder-click="select(boulder)"
                            selected="currentBoulder"></gym-floor-plan>
        </div>

        <div class="medium-4 column">
            <boulder-meta boulder="currentBoulder"></boulder-meta>

            <button ng-click="unset(currentBoulder)">Abschrauben!</button>
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

</div>

</body>
</html>