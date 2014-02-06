<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>

<div ng-controller="GymOverviewCtrl" class="row content">
    <div class="medium-8 column">
        <gym-floor-plan floor-plan="fp" floor-plan-click="fpClick(point)" boulders="bs"
                        boulder-click="bClick(boulder)"
                        boulders-draggable="true"></gym-floor-plan>
    </div>
    <div class="medium-4 column">
        <p>test</p>
    </div>
</div>

</body>
</html>