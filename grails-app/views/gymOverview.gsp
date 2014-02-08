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
            <table class="boulder-meta-data" ng-class="{inactive: !currentBoulder}">
                <tbody>
                <tr>
                    <th>ID</th>
                    <td>{{currentBoulder.id}}</td>
                </tr>
                <tr>
                    <th><g:message code="bcomp.boulder.color.label"/></th>
                    <td>{{currentBoulder.color.germanName}}</td>
                </tr>
                <tr>
                    <th><g:message code="bcomp.boulder.initialGrade.label"/></th>
                    <td>{{currentBoulder.initialGrade.readable}}</td>
                </tr>
                <tr>
                    <th><g:message code="bcomp.boulder.currentGrade.label"/></th>
                    <td>{{currentBoulder.grade.mean.font}}</td>
                </tr>
                <tr ng-if="currentBoulder.end">
                    <th><g:message code="bcomp.boulder.end.label"/></th>
                    <td>{{currentBoulder.end | amDateFormat: 'LL'}}</td>
                </tr>
                <tr>
                    <th><g:message code="bcomp.ascents.label"/></th>
                    <td>
                        <span>{{currentBoulderAscents.length}}</span>
                        <span ng-show="currentBoulderAscents.length > 0">({{(currentBoulderAscents | filter:{style:'flash'}).length}} flash)</span>
                        <ul ng-show="currentBoulderAscents.length > 0">
                            <li ng-repeat="ascent in currentBoulderAscents | orderBy:'-date'">
                                {{ascent.date | amDateFormat: 'LL'}}: {{users[ascent.boulderer].username}}
                                ({{users[ascent.boulderer].grade.mean.font}})
                            </li>
                        </ul>
                    </td>
                </tr>
                </tbody>
            </table>

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