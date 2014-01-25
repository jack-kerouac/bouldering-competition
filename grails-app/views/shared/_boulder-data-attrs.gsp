data-id="${boulder.id}"
data-initial-grade="${boulder.initialGrade}"
data-x="${boulder.location.x * boulder.location.floorPlan.widthInPx}"
data-y="${boulder.location.y * boulder.location.floorPlan.heightInPx}"
data-color-primary="${rgb(color: boulder.color.primaryColor)}"
<g:if test="${boulder.color.hasSecondaryColor()}">
    data-color-secondary="${rgb(color: boulder.color.secondaryColor)}"
</g:if>
data-current-font-grade="${boulder.grade.mean.toFontScale()}"
data-color="<g:message code="bcomp.boulder.color.${boulder.color}"/>"