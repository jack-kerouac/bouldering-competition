<ul class="boulders">
<g:each in="${boulders}" var="boulder" status="number">
    <g:if test="${boulder.location instanceof bcomp.gym.OnFloorPlan}">
        <li id="boulder-${boulder.id}"
            <tmpl:/shared/boulder-data-attrs boulder="${boulder}"/>/>
        <g:render template="${boulderTemplate}" model="['boulder': boulder, 'number': number]"/>
        </li>
    </g:if>
    <g:else>
        <p class="error">Location of boulder is not on a floor plan.</p>
    </g:else>
</g:each>
</ul>