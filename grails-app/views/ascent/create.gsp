<%@ page import="bcomp.Ascent" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.new.label" args="[message(code: 'bcomp.ascent.label')]"/></title>
</head>

<body id="create-ascent-page">

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.new.label" args="[message(code: 'bcomp.ascent.label')]"/></h1>
    </div>
</div>

<g:form action="create">
    <input type="hidden" name="boulderer.id" value="${user.id}"/>

    <div class="row">
        <div class="small-12 column">
            <g:set var="floorPlan" value="${gym.floorPlans.first()}"></g:set>

            <div class="boulder-location-map">
                <img class="floor-plan" src="${createLink(controller: 'floorPlan', action: 'image', params: [gymId:
                        gym.id, floorPlanId: floorPlan.id])}"/>
                <ul>
                    <g:each in="${boulders}" var="boulder">
                        <g:if test="${boulder.location instanceof bcomp.gym.OnFloorPlan}">
                            <li data-x="${boulder.location.x}"
                                data-y="${boulder.location.y}"
                                data-grade="${boulder.grade}"/>
                                <input type="radio" name="boulder.id" value="${boulder.id}">
                            </li>
                        </g:if>
                        <g:else>
                            <p class="error">Location of Boulder is not on a floorplan.</p>
                        </g:else>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Date</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'date', 'error')}">
            <input name="date" type="date" value="${formatDate(date: cmd.date, format: 'yyyy-MM-dd')}">
            <g:hasErrors field="date">
                <small>
                    <g:eachError><span>${it.defaultMessage}</span></g:eachError>
                </small>
            </g:hasErrors>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Style</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'tries', 'error')}">
            <div class="row">
                <div class="small-9 column">
                    <input type="radio" name="style" value="flash" id="flash"
                        ${cmd.style == Ascent.Style.flash ? 'checked' : ''}><label
                        for="flash">flash</label>
                </div>
            </div>

            <div class="row">
                <div class="medium-12 column">
                    <input type="radio" name="style" value="top"
                           id="top" ${cmd.style == Ascent.Style.top ? 'checked' : ''
                    }><label for="top" class="inline">top in</label>
                    <input type="number" name="tries" value="${cmd.tries}" min="2" id="tries"/>
                    <label for="tries" class="inline">tries</label>
                    <g:hasErrors field="tries">
                        <small>
                            <g:eachError><span>${it.defaultMessage}</span></g:eachError>
                        </small>
                    </g:hasErrors>
                </div>
            </div>
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