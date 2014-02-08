<table class="boulder-meta-data" ng-class="{inactive: !boulder}">
    <tbody>
    <tr>
        <th>ID</th>
        <td>{{boulder.id}}</td>
    </tr>
    <tr>
        <th>
            <g:message code="bcomp.boulder.color.label"/>
        </th>
        <td>{{boulder.color.germanName}}</td>
    </tr>
    <tr>
        <th>
            <g:message code="bcomp.boulder.initialGrade.label"/>
        </th>
        <td>{{boulder.initialGrade.readable}}</td>
    </tr>
    <tr>
        <th>
            <g:message code="bcomp.boulder.currentGrade.label"/>
        </th>
        <td>{{boulder.grade.mean.font}}</td>
    </tr>
    <tr ng-if="currentBoulder.end">
        <th>
            <g:message code="bcomp.boulder.end.label"/>
        </th>
        <td>{{boulder.end | amDateFormat: 'LL'}}</td>
    </tr>
    <tr ng-show="ascents != undefined">
        <th>
            <g:message code="bcomp.ascents.label"/>
        </th>
        <td>
            <span>{{ascents.length}}</span>
            <span ng-show="ascents.length > 0">({{(ascents | filter:{style:'flash'}).length}} flash)</span>
            <ul ng-show="ascents.length > 0">
                <li ng-repeat="ascent in ascents | orderBy:'-date'">
                    {{ascent.date | amDateFormat: 'LL'}}:
                    {{users[ascent.boulderer].username}} ({{users[ascent.boulderer].grade.mean.font}})
                </li>
            </ul>
        </td>
    </tr>
    </tbody>
</table>