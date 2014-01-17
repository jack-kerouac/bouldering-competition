<h2>Boulder ${boulder.id}</h2>
<p>current grade: ${boulder.currentGrade.toFontScale()}</p>
<input type="hidden" name="ascents[${number}].boulder.id" value="${boulder.id}">


<input type="radio" name="ascents[${number}].style" value="flash" id="boulder-${boulder.id}-flash">
<label for="boulder-${boulder.id}-flash" class="inline"><g:message code="bcomp.ascent.style.flash.label"/></label>
<input type="radio" name="ascents[${number}].style" value="top" id="boulder-${boulder.id}-top">
<label for="boulder-${boulder.id}-top" class="inline"><g:message code="bcomp.ascent.style.top.label"/></label>
<input type="radio" name="ascents[${number}].style" value="none" id="boulder-${boulder.id}-none" checked>
<label for="boulder-${boulder.id}-none" class="inline">none</label>
