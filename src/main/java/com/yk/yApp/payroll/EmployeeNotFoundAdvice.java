package com.yk.yApp.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(EmployeeNotFoundException ex) {
		return ex.getMessage();
	}
}

/*
	@ResponseBodyは、このアドバイスがレスポンス本文に直接レンダリングされることを通知します。
	@ExceptionHandlerは、EmployeeNotFoundExceptionがスローされた場合にのみ応答するようにアドバイスを構成します。
	@ResponseStatusは、HttpStatus.NOT_FOUND、つまりHTTP404を発行するように指示します。
	アドバイスの本文がコンテンツを生成します。この場合、例外のメッセージが表示されます。
*/