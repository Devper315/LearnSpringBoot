package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public enum ErrorCode {
	USER_EXISTED(404, "User đã tồn tại", HttpStatus.INTERNAL_SERVER_ERROR),
	USER_NOT_EXIST(405, "User không tồn tại", HttpStatus.BAD_REQUEST),
	UNCATEGORIEZD_EXCEPTION(9999, "uncategorized exception", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD(1003, "Mật khẩu có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
	INVALID_KEY(555, "Mã lỗi không tồn tại", HttpStatus.NOT_FOUND),
	UNAUTHENTICATED(913, "Sai thông tin tài khoản", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(914, "Không thể xác thực", HttpStatus.FORBIDDEN),
	INVALID_DOB(915, "Số tuổi phải lớn hơn {min}", HttpStatus.BAD_REQUEST);
	
	private int code;
	private String message;
	private HttpStatusCode statusCode;
	
	
}
