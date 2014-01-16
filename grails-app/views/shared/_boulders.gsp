<ul class="boulders">
<g:each in="${boulders}" var="boulder">
    <g:if test="${boulder.location instanceof bcomp.gym.OnFloorPlan}">
        <li id="boulder-${boulder.id}"
            data-x="${boulder.location.x * boulder.location.floorPlan.widthInPx}"
            data-y="${boulder.location.y * boulder.location.floorPlan.heightInPx}"
            data-color-primary="${rgb(color: boulder.color.primaryColor)}"
            <g:if test="${boulder.color.hasSecondaryColor()}">
                data-color-secondary="${rgb(color: boulder.color.secondaryColor)}"
            </g:if>
            data-current-font-grade="${boulder.currentGrade.toFontScale()}"
            data-color="${boulder.color}"
        />
        <g:render template="${boulderTemplate}" model="['boulder': boulder]" />
        </li>
    </g:if>
    <g:else>
        <p class="error">Location of boulder is not on a floor plan.</p>
    </g:else>
</g:each>
</ul>