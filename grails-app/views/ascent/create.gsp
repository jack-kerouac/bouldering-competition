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

        <p>Markers show the color of the boulder. Zoom in by double click (desktop) or pinching (mobile).</p>
    </div>
</div>

<g:form action="create">
    <input type="hidden" name="boulderer.id" value="${user.id}"/>

    <div class="row">
        <div class="small-12 column">
            <div class="boulder-location-map">

                <tmpl:/shared/floorPlan floorPlan="${gym.floorPlans.first()}"/>

                <tmpl:/shared/boulders boulders="${boulders}" boulderTemplate="/shared/boulder-radio-select"/>
            </div>
            <tmpl:/shared/fieldError field="boulder"/>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Date</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'date', 'error')}">
            <input name="date" type="date" value="${formatDate(date: cmd.date, format: 'yyyy-MM-dd')}">
            <tmpl:/shared/fieldError field="date"/>
        </div>
    </div>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Style</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'tries', 'error')}">
            <input type="radio" name="style" value="flash" id="flash" ${cmd.style == Ascent.Style.flash ?
                    'checked' : ''}>
            <label for="flash" class="inline">flash</label>
            <input type="radio" name="style" value="top" id="top" ${cmd.style == Ascent.Style.top ? 'checked'
                    : ''}>
            <label for="top" class="inline">top</label>
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