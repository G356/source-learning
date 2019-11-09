package com.springboot.security.dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
public class  ResponseUtil {
	private static ObjectMapper objectMapper=new ObjectMapper();
	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods","*");
			response.setStatus(status);
			response.getWriter().write(objectMapper.writeValueAsString(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}