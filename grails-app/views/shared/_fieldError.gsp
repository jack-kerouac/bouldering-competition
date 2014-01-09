<g:hasErrors field="${field}">
    <p class="error">
        <small>
            <g:message error="${cmd.errors.getFieldError(field)}"/>
            <g:if test="${cmd.errors.getFieldErrorCount(field) > 1}">
                <span>(and ${cmd.errors.getFieldErrorCount(field) - 1} more</span>
            </g:if>
        </small>
    </p>
</g:hasErrors>