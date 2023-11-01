package us.dit.consentimientos.service.services.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateConsentsRequestedMenu implements IMapper<Map<Long, String>, String> {

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
		String style = generateStyle();
		
		head = "<head>\r\n"
				+ "<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>Gestor Consentimientos</title>\r\n"
				+ style
				+ "</head>\r\n";
		
		return head;
	}
	
	private String generateBody(Map<Long, String> instancesAndTitle) {
		String body = "<body>\r\n"
				+ "<div class=\"container\">\r\n";
		
		body = body + "<a class=\"home-link\" href=\"/private/practitioner/\">Back</a>";
		
		body = body + "    <div class=\"menu-box\">\r\n"
				+ "        <h2>Consent request</h2>\r\n"
				+ "        <div class=\"menu-links\">\r\n";
		
		List<Long> instances = new ArrayList<Long>(instancesAndTitle.keySet());
		for (Long idInstance : instances) {
			body = body + "<a href=\"/private/practitioner/consentsRequested/consent?param="
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
	
	private String generateStyle() {
		String style = "<style>\r\n"
				+ "html {\r\n"
				+ "    height: 100%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "body {\r\n"
				+ "    margin: 0;\r\n"
				+ "    padding: 0;\r\n"
				+ "    font-family: sans-serif;\r\n"
				+ "    background: linear-gradient(#141e30, #243b55);\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".container {\r\n"
				+ "    display: flex;\r\n"
				+ "    align-items: center;\r\n"
				+ "    justify-content: center;\r\n"
				+ "    height: 100%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-box {\r\n"
				+ "    position: absolute;\r\n"
				+ "	top: 50%;\r\n"
				+ "	left: 50%;\r\n"
				+ "	transform: translate(-50%, -50%);\r\n"
				+ "    width: 400px;\r\n"
				+ "    padding: 40px;\r\n"
				+ "    background: rgba(0, 0, 0, .5);\r\n"
				+ "    box-shadow: 0 15px 25px rgba(0, 0, 0, .6);\r\n"
				+ "    border-radius: 10px;\r\n"
				+ "    text-align: center;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-box h2 {\r\n"
				+ "    margin: 0 0 30px;\r\n"
				+ "    color: #fff;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-links a {\r\n"
				+ "    display: block;\r\n"
				+ "    margin-bottom: 10px;\r\n"
				+ "    padding: 10px 20px;\r\n"
				+ "    font-size: 16px;\r\n"
				+ "    text-transform: uppercase;\r\n"
				+ "    color: #03e9f4;\r\n"
				+ "    border: 2px solid #03e9f4;\r\n"
				+ "    border-radius: 5px;\r\n"
				+ "    cursor: pointer;\r\n"
				+ "    outline: none;\r\n"
				+ "    transition: background-color 0.3s, color 0.3s;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".logout-button {\r\n"
				+ "    position: absolute;\r\n"
				+ "    top: 20px;\r\n"
				+ "    right: 20px;\r\n"
				+ "    display: inline-block;\r\n"
				+ "    padding: 10px 20px;\r\n"
				+ "    font-size: 16px;\r\n"
				+ "    text-decoration: none;\r\n"
				+ "    text-transform: uppercase;\r\n"
				+ "    background: transparent;\r\n"
				+ "    color: #03e9f4;\r\n"
				+ "    border: 2px solid #03e9f4;\r\n"
				+ "    border-radius: 5px;\r\n"
				+ "    cursor: pointer;\r\n"
				+ "    outline: none;\r\n"
				+ "    transition: background-color 0.3s, color 0.3s;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-links a:hover, .logout-button:hover {\r\n"
				+ "    background-color: #03e9f4;\r\n"
				+ "    color: #fff;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-links a:active, .logout-button:active {\r\n"
				+ "    background-color: #03c2d8;\r\n"
				+ "    border-color: #03c2d8;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-links {\r\n"
				+ "    margin-top: 20px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".menu-links a {\r\n"
				+ "    text-decoration: none;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".home-link {\r\n"
				+ "    position: absolute;\r\n"
				+ "    top: 20px;\r\n"
				+ "    left: 20px;\r\n"
				+ "    font-size: 18px;\r\n"
				+ "    color: #fff;\r\n"
				+ "    text-decoration: none;\r\n"
				+ "    cursor: pointer;\r\n"
				+ "}\r\n"
				+ "</style>\r\n";
		
		return style;
	}

}
