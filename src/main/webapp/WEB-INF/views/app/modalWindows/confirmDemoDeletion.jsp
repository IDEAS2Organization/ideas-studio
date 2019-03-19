<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-modal-dialog>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title"><spring:message code="editor.actions.modal.confirm_demo_deletion.title" /></h4>
    </div>
    <div class="modal-body">
        <spring:message code="editor.actions.modal.confirm_demo_deletion.msg_1" arguments="${workspaceName}" /><BR/><BR/>
        <b><spring:message code="editor.actions.modal.confirm_demo_deletion.msg_2" /></b><BR/>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn dismiss"><spring:message code="app.modal.close" /></a>
        <a class="btn btn-primary continue"><spring:message code="action.continue"/></a>
    </div>
</ideas:app-modal-dialog>