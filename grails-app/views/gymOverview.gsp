<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>

<!-- TODO: move ng-controller to body -->
<div ng-controller="GymOverviewCtrl">

    <div class="row">
        <div class="medium-12 column"><h1><g:message code="bcomp.gym.overview.label"/> {{gym.name}}</h1></div>
    </div>

    <div class="row content">

        <div class="medium-8 column">
            <gym-floor-plan floor-plan="gym.floorPlans[0]" boulders="boulders"
                            boulder-click="select(boulder)"
                            boulders-draggable="true"></gym-floor-plan>
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
                    <td><span ng-show="currentBoulderAscents.length > 0">{{currentBoulderAscents.length}} ({{(currentBoulderAscents | filter:{style:
                    'flash'})
                    .length}} flash)</span>
                        <ul ng-show="currentBoulderAscents.length > 0">
                            <li ng-repeat="ascent in currentBoulderAscents">
                                {{ascent.date | amDateFormat: 'LL'}}: {{users[ascent.boulderer].username}}
                                ({{users[ascent.boulderer].grade.mean.font}})
                            </li>
                        </ul>
                    </td>
                </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</div>

</body>
</html>