<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.new.label" args="[message(code: 'bcomp.boulder.label')]"/></title>
</head>

<body id="create-boulder-page">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.new.label" args="[message(code: 'bcomp.boulder.label')]"/></h1>
        <p>Chose the color of the boulder and then click on the floor plan to select the start of the boulder
        (the position in the fields below will change).</p>
    </div>
</div>

<g:form action="create">

    <input type="hidden" name="gym.id" value="${gym.id}"/>


    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Color</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'color', 'error')}">
            <g:select name="color" from="${colors}"></g:select>
            <tmpl:/shared/fieldError field="color"/>
        </div>
    </div>


    <div class="row">
        <div class="small-12 column">
            <div class="boulder-location-map">
                <g:set var="floorPlan" value="${gym.floorPlans.first()}"></g:set>

                <img class="floor-plan" src="${createLink(controller: 'floorPlan', action: 'image', params: [gymId:
                        gym.id, floorPlanId: floorPlan.id])}"/>
                <input type="hidden" name="floorPlan.id" value="${floorPlan.id}"/>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Position</label>
        </div>

        <div class="small-9 column">
            <label for="x" class="inline">x:</label>
            <input type="text" id="x" name="x" readonly/>
            <label for="x" class="inline">y:</label>
            <input type="text" id="y" name="y" readonly/>
            <tmpl:/shared/fieldError field="x"/>
            <tmpl:/shared/fieldError field="y"/>
        </div>
    </div>

    <div class="row">
        <div class="small-9 small-offset-3">
            <g:submitButton name="submit" class="button" value="${message(code: 'default.button.create.label')}"/>
        </div>
    </div>
</g:form>

</body>
</html>