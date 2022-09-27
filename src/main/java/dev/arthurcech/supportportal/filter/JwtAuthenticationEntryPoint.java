package dev.arthurcech.supportportal.filter;

import static dev.arthurcech.supportportal.constant.SecurityConstant.FORBIDDEN_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.arthurcech.supportportal.domain.HttpResponse;

/**
 * Este filtro é invocado quando o usuário tenta acessar um recurso sem estar
 * autenticado
 */
@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException {
		HttpResponse httpResponse = new HttpResponse(FORBIDDEN.value(), FORBIDDEN,
				FORBIDDEN.getReasonPhrase().toUpperCase(), FORBIDDEN_MESSAGE);

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(FORBIDDEN.value());

		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, httpResponse);
		outputStream.flush();
	}

}
