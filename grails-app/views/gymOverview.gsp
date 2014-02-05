<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>

<div ng-controller="GymOverviewCtrl" class="row content">
    <div class="medium-8 column">
        <floor-plan plan="floorPlan" click="click(point)" boulders="boulders"></floor-plan>
    </div>
    <div class="medium-4 column">
        <p>test</p>
    </div>
</div>

</body>
</html>