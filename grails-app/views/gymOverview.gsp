<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="bcomp.gym.overview.label"/> {{gym.name}}</title>
</head>

<body>

<!-- TODO: move ng-controller to body -->
<div ng-controller="GymOverviewCtrl" ng-cloak>

    <div class="row">
        <div class="medium-12 column"><h1><g:message code="bcomp.gym.overview.label"/> {{gym.name}}</h1></div>
    </div>

    <div class="row content">

        <div class="medium-8 column">
            <gym-floor-plan floor-plan="gym.floorPlans[0]" boulders="boulders"
                            boulder-click="select(boulder)"
                            selected="currentBoulder"></gym-floor-plan>
        </div>

        <div class="medium-4 column">
            <boulder-meta boulder="currentBoulder" ascents="currentBoulderAscents" users="users"></boulder-meta>
        </div>
    </div>

    <div class="row content">
        <div class="medium-12 column">
            <table>
                <thead>
                <tr>
                    <th><g:message code="bcomp.date.label"/></th>
                    <th><g:message code="bcomp.userRole.boulderer.label"/></th>
                    <th><g:message code="bcomp.ascents.label"/></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="session in sessions | orderBy:'-date'">
                    <td>{{session.date | amDateFormat: 'LL'}}</td>
                    <td>{{users[session.boulderer].username}}</td>
                    <td>{{session.ascents.length}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>