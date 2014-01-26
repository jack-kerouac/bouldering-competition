<%@ page import="bcomp.gym.Boulder" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/></title>
</head>

<body id="create-boulder-page" ng-controller="BoulderCtrl">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.set.label" args="[message(code: 'bcomp.boulder.label')]"/></h1>

        <p>Wähle die Farbe des Boulders aus, den Grad und klicke dann auf den Grundriss unten um den Start der
        Boulder zu markieren. Es können beliebig viele Boulder mit der gewählten Farbe und dem gewählten Grad gesetzt
        werden. In Chrome muss man die Karte kurz verschieben/zoomen, damit die gesetzten Marker auftauchen...
        </p>
    </div>
</div>

<g:form action="create">

    <input type="hidden" name="gym.id" value="${gym.id}"/>


    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.boulder.color.label"/></label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'color', 'error')}">
            <select name="color">
                <g:each in="${colors}" var="color">
                    <option value="${color}" data-color-primary="${rgb(color: color.primaryColor)}"
                        <g:if test="${color.hasSecondaryColor()}">
                            data-color-secondary="${rgb(color: color.secondaryColor)}"
                        </g:if>
                        <g:if test="${cmd.color == color}">
                            selected
                        </g:if>/>
                    <g:message code="bcomp.boulder.color.${color}"/>
                    </option>
                </g:each>
            </select>
            <tmpl:/shared/fieldError field="color"/>
        </div>
    </div>


    <div class="row">
        <div class="small-3 column">
            <label class="right inline"><g:message code="bcomp.boulder.grade.label"/></label>
        </div>

        <div class="small-9 column">
            <p><small>use grades: ${bcomp.gym.Grade.FONT_GRADES}</small></p>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" name="gradeCertainty" value="ASSIGNED"
                           id="assigned-grade" ${cmd.gradeCertainty == Boulder.GradeCertainty.ASSIGNED ? 'checked' : ''}>
                    <label for="assigned-grade" class="inline"><g:message
                            code="bcomp.boulder.gradeCertainty.assigned.label"/></label>
                    <input type="text" name="grade" placeholder="6b+" value="${cmd.grade}"/>
                </div>
            </div>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" name="gradeCertainty" value="RANGE" id="grade-range" ${cmd.gradeCertainty ==
                            Boulder.GradeCertainty.RANGE ? 'checked' : ''}>
                    <label for="grade-range" class="inline">
                        <g:message code="bcomp.boulder.gradeCertainty.range.label"/>
                    </label>
                    <input type="text" name="gradeRangeLow" placeholder="6a+" value="${cmd.gradeRangeLow}"/>
                    -
                    <input type="text" name="gradeRangeHigh" placeholder="6c+" value="${cmd.gradeRangeHigh}"/>
                </div>
            </div>

            <div class="row">
                <div class="column small-12">
                    <input type="radio" name="gradeCertainty" value="UNKNOWN" id="unknown-grade" ${cmd.gradeCertainty ==
                            Boulder.GradeCertainty.UNKNOWN ? 'checked' : ''}>
                    <label for="unknown-grade" class="inline">
                        <g:message code="bcomp.boulder.gradeCertainty.unknown.label"/>
                    </label>
                </div>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="small-12 column">
            <div class="boulder-location-map">
                <g:set var="floorPlan" value="${gym.floorPlans.first()}"></g:set>

                <tmpl:/shared/floorPlan floorPlan="${floorPlan}"/>
                <input type="hidden" name="floorPlan.id" value="${floorPlan.id}"/>
            </div>
        </div>
        <ul class="coordinates" style="display: none">
        </ul>
    </div>

    <div class="row">
        <div class="small-9 small-offset-3">
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.set.label')}"/>
        </div>
    </div>
</g:form>

</body>
</html>