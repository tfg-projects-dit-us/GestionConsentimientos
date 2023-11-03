package us.dit.consentimientos.service.services.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreatePatientMenu implements IMapper<Map<Long, String>, String> {

	@Override
	public String map(Map<Long, String> in) {
		String contentHtml = generateHtml(in);
		
		return contentHtml;
	}
	
	private String generateHtml(Map<Long, String> instancesAndTitle) {
		String html = "<!DOCTYPE html>\r\n"
				+ "<html xmlns:sec=\"http://www.springframework.org/security/tags\">\r\n";
		
		String head = generateHead();
		html = html + head;
		
		String body = generateBody(instancesAndTitle);
		html = html + body;
		
		html = html + "</html>";
		
		return html;
	}
	
	private String generateHead() {
		String head = null;
	
		
		head = "<head>\r\n"
				+ "<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>Gestor Consentimientos</title>\r\n"
				+ "<link rel=\"stylesheet\" href=\"/form-styles.css\">"
				+ "</head>\r\n";
		
		return head;
	}
	
	private String generateBody(Map<Long, String> instancesAndTitle) {
		String body = "<body>\r\n"
				+ "<div class=\"container\">\r\n";
		
		
		
		body = body + "    <div class=\"menu-box\">\r\n"
				+ "        <h2>Pending consent request</h2>\r\n"
				+ "        <div class=\"menu-links\">\r\n";
		
		List<Long> instances = new ArrayList<Long>(instancesAndTitle.keySet());
		for (Long idInstance : instances) {
			body = body + "<a href=\"/private/patient/seeRequestedConsent?param="
					+ idInstance
					+ "\">"
					+ instancesAndTitle.get(idInstance)
					+ "</a>\r\n";
		}
		
		body = body + "</div>\r\n"
				+ "    </div>\r\n"
				+ "    <form action=\"/logout\" method=\"POST\">\r\n"
				+ "        <button class=\"logout-button\">Logout</button>\r\n"
				+ "    </form>\r\n"
				+ "</div>\r\n"
				+ "</body>\r\n";
		
		return body;
	}

}
