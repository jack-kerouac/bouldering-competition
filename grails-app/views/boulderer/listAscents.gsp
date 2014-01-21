<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${boulderer.username}'s <g:message code="bcomp.ascents.label"/> in ${gym.name}</title>
</head>

<body id="list-ascents-page">

<div class="row">
    <div class="medium-12 columns">
        <h1>${boulderer.username}'s <g:message code="bcomp.ascents.label"/> in ${gym.name}</h1>
    </div>
</div>

<div class="row content">
    <div class="medium-12 column">
        <div class="boulder-location-map">
            <tmpl:/shared/floorPlan floorPlan="${gym.floorPlans.first()}"/>

            <ul class="boulders">
                <g:each in="${ascents}">
                    <g:set var="boulder" value="${it.key}"/>
                    <li id="boulder-${boulder.id}" <tmpl:/shared/boulder-data-attrs boulder="${boulder}"/>>
                        <ul>
                            <g:each in="${it.value}" var="ascent">
                                <li>
                                    <span><g:formatDate type="date" date="${ascent.date}"/></span>:
                                    <span>${ascent.style}</span>
                                </li>
                            </g:each>
                        </ul>
                    </li>
                </g:each>
            </ul>
        </div>
    </div>
</div>

</body>
</html>