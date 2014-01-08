<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="default.new.label" args="[message(code: 'bcomp.boulder.label')]"/></title>
</head>

<body>

<div class="row">
    <div class="small-12 column">
        <h1><g:message code="default.new.label" args="[message(code: 'bcomp.boulder.label')]"/></h1>
    </div>
</div>

<g:form action="create">

    <input type="hidden" name="gym.id" value="${gym.id}"/>

    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Location Description</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'locationDescription', 'error')}">
            <g:textField name="locationDescription" value="${cmd.locationDescription?.id}"/>
            <g:hasErrors field="locationDescription">
                <small>
                    <g:eachError><span>${it.defaultMessage}</span></g:eachError>
                </small>
            </g:hasErrors>
        </div>
    </div>


    <div class="row">
        <div class="small-3 column">
            <label class="right inline">Grade</label>
        </div>

        <div class="small-9 column ${hasErrors(field: 'grade', 'error')}">
            <g:select name="grade" from="${grades}"
                      value="${cmd.grade}"></g:select>
            <g:hasErrors field="grade">
                <small>
                    <g:eachError><span>${it.defaultMessage}</span></g:eachError>
                </small>
            </g:hasErrors>
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