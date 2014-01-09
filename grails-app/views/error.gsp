<!DOCTYPE html>
<html>
<head>
    <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
    <g:if env="development">

    </g:if>
    <g:else>
        <meta name="layout" content="main">
    </g:else>
    <g:if env="development"><link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}"
                                  type="text/css"></g:if>
</head>

<body>
<g:if env="development">
    <g:renderException exception="${exception}"/>
</g:if>
<g:else>
    <div class="row">
        <div class="column small-12">
            <p>An error has occurred.</p>
        </div>
    </div>
</g:else>

</body>
</html>
