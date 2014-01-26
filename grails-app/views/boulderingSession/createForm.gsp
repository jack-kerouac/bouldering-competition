<%@ page import="bcomp.Ascent" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></title>
</head>

<body id="log-session-page" ng-controller="SessionCtrl">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.log.label" args="[message(code: 'bcomp.boulderingSession.label')]"/></h1>

        <p>Markers show the color of the boulder. Zoom in by double click (desktop) or pinching (mobile).</p>
    </div>
</div>

<g:form action="log">
    <input type="hidden" name="boulderer.id" value="${boulderer.id}"/>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.date.label"/></label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'date', 'error')}">
            <input name="date" type="date" value="${formatDate(date: cmd.date, format: 'yyyy-MM-dd')}">
            <tmpl:/shared/fieldError field="date"/>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.gym.label"/></label>
        </div>

        <div class="small-9 column">
            <select ng-model="gym" ng-options="gym.name for gym in gyms" required></select>
            <input type="hidden" name="gym.id" value="{{gym.id}}"/>

            <tmpl:/shared/fieldError field="date"/>
        </div>
    </div>


    <div class="row">

        <div class="medium-12 small-11 column">
            <div class="boulder-location-map">
                <img class="floor-plan" ng-src="{{gym.floorPlans[0].img.url}}"
                     width="{{gym.floorPlans[0].img.widthInPx}}"
                     height="{{gym.floorPlans[0].img.heightInPx}}"/>

                %{--<tmpl:/shared/floorPlan floorPlan="${gym.floorPlans.first()}"/>--}%

                <ul class="boulders">
                    <li ng-repeat="boulder in boulders | orderBy:boulderOrderProp"
                        id="{{'boulder-' + boulder.id}}"
                        data-id="{{boulder.id}}"
                        data-initial-grade="{{boulder.initialGrade}}"
                        data-x="{{boulder.location.x * boulder.location.floorPlan.img.widthInPx}}"
                        data-y="{{boulder.location.y * boulder.location.floorPlan.img.heightInPx}}"
                        data-color="{{boulder.color.name}}"
                        data-color-primary="{{boulder.color.primary}}"
                        data-color-secondary="{{boulder.color.secondary}}"
                        data-grade="{{boulder.grade.mean.value}}">
                        <h2>Boulder {{boulder.id}}</h2>
                        <input type="hidden" name="ascents[{{$index}}].boulder.id" value="{{boulder.id}}">

                        <input type="radio" name="ascents[{{$index}}].style" value="flash"
                               id="boulder-{{boulder.id}}-flash">
                        <label for="boulder-{{boulder.id}}-flash" class="inline"><g:message
                                code="bcomp.ascent.style.flash.label"/></label>
                        <input type="radio" name="ascents[{{$index}}].style" value="top"
                               id="boulder-{{boulder.id}}-top">
                        <label for="boulder-{{boulder.id}}-top" class="inline"><g:message
                                code="bcomp.ascent.style.top.label"/></label>
                        <input type="radio" name="ascents[{{$index}}].style" value="none"
                               id="boulder-{{boulder.id}}-none" checked>
                        <label for="boulder-{{boulder.id}}-none" class="inline">none</label>

                    </li>
                </ul>
            </div>
            <tmpl:/shared/fieldError field="boulder"/>
        </div>
    </div>

    <div class="row">
        <div class="small-9 small-offset-3">
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.log.label')}"/>
        </div>
    </div>
</g:form>

</body>
</html>