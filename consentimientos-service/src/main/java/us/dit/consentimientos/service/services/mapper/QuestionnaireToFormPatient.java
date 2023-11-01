package us.dit.consentimientos.service.services.mapper;

import java.util.List;

import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemAnswerOptionComponent;

public class QuestionnaireToFormPatient implements IMapper<Questionnaire, String> {

	@Override
	public String map(Questionnaire in) {
		String contentHtml = generateHtml(in);
		
		return contentHtml;
	}
	
	private String generateHtml(Questionnaire questionnaire) {
		String html = "<!DOCTYPE html>\r\n"
				+ "<html>\r\n";
		
		String head = generateHead();
		html = html + head;
		
		String body = generateBody(questionnaire);
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
	
	private String generateBody(Questionnaire questionnaire) {
		String body = null;
		
		body = "<body>\r\n"
				+"<div class=\"box\">\r\n"
				+ "<h2>"
				+ questionnaire.getTitle()
				+ "</h2>\r\n"
				+ "<form action=\"/private/patient\" method=\"post\">\r\n";
		
		for (Questionnaire.QuestionnaireItemComponent item : questionnaire.getItem()) {
			String question = generateQuestion(item);
			if (question != null) {
				body = body + question;
			}
		}
		
		body = body
				+ "<input type=\"submit\" value=\"Send\">\r\n"
				+ "</form>\r\n"
				+ "</div>\r\n"
				+ "</body>\r\n";
		
		return body;
	}
	
	private String generateQuestion(Questionnaire.QuestionnaireItemComponent item) {
		String question = null;
		
		switch (item.getType()) {
		case BOOLEAN:
			question = createBooleanComponent(item);
			break;
		case STRING:
			question = createStringComponent(item);
			break;
		case DATE:
			question = createDateComponent(item);
			break;
		case CHOICE:
			question = createChoiceComponent(item);
			break;
		case GROUP:
			question = createGroupComponent(item);
			break;
		default:
			throw new UnsupportedOperationException("Tipo de componente no soportado: " + item.getType().getDisplay());
		}
		
		return question;
	}
	
	private String createGroupComponent(Questionnaire.QuestionnaireItemComponent item) {
		String nameGroup = item.getText() + ":";
		String component = "<fieldset>\r\n"
				+ "<legend>+" + nameGroup + "+</legend>\r\n";
		
		for (Questionnaire.QuestionnaireItemComponent it : item.getItem()) {
			switch (it.getType()) {
			case BOOLEAN:
				component = component + createBooleanComponent(it);
				break;
			case STRING:
				component = component + createStringComponent(it);
				break;
			case DATE:
				component = component + createDateComponent(it);
				break;
			case CHOICE:
				component = component + createChoiceComponent(it);
				break;
			case GROUP:
				component = component + createGroupComponent(it);
				break;
			default:
				throw new UnsupportedOperationException("Tipo de componente no soportado: " + it.getType().getDisplay());
			}
		}
		
		component = component + "</fieldset>\r\n";
		
		return component;
	}
	
	private String createBooleanComponent(Questionnaire.QuestionnaireItemComponent item) {
		String question = item.getText();
		String id = item.getLinkId();
		
		String component = null;
		
		if (item.getRequired()) {
			component = "<div class=\"consent-question\">\r\n"
					+ "<label for=\"" + id + "\">" + question + "</label>\r\n"
					+ "    <div class=\"radio-group\">\r\n"
					+ "      <label>\r\n"
					+ "        <input type=\"radio\" id=\"" + id + "-yes\" name=\"" + id + "\" value=\"true\" required>\r\n"
					+ "        <span>Sí, estoy de acuerdo y autorizo</span>\r\n"
					+ "      </label>\r\n"
					+ "      <label>\r\n"
					+ "        <input type=\"radio\" id=\"" + id + "-no\" name=\"" + id + "\" value=\"false\" required>\r\n"
					+ "        <span>No, no estoy de acuerdo</span>\r\n"
					+ "      </label>\r\n"
					+ "    </div>\r\n"
					+ "</div>\r\n";
		} else {
			String value = item.getAnswerOption().get(0).getValue().toString();
			
			if (value.equals("true")) {
				component = "<label for=\"" + id + "\">" + question + "</label>\r\n"
						+ "    <div class=\"radio-group\">\r\n"
						+ "      <label>\r\n"
						+ "        <input type=\"radio\" id=\"" + id + "-yes\" name=\"" + id + "\" value=\"true\" checked> Sí\r\n"
						+ "      </label>\r\n"
						+ "      <label>\r\n"
						+ "        <input type=\"radio\" id=\"" + id + "-no\" name=\"" + id + "\" value=\"false\" disabled> No\r\n"
						+ "      </label>\r\n"
						+ "    </div>\r\n";
			} else {
				component = "<label for=\"" + id + "\">" + question + "</label>\r\n"
						+ "    <div class=\"radio-group\">\r\n"
						+ "      <label>\r\n"
						+ "        <input type=\"radio\" id=\"" + id + "-yes\" name=\"" + id + "\" value=\"true\" disabled> Sí\r\n"
						+ "      </label>\r\n"
						+ "      <label>\r\n"
						+ "        <input type=\"radio\" id=\"" + id + "-no\" name=\"" + id + "\" value=\"false\" checked> No\r\n"
						+ "      </label>\r\n"
						+ "    </div>\r\n";
			}
		}
		
		return component;
	}
	
	private String createStringComponent(Questionnaire.QuestionnaireItemComponent item) {
		String question = item.getText();
		String id = item.getLinkId();
		String value = item.getAnswerOption().get(0).getValue().toString();
		
		String component = "<label for=\"" + id + "\">" + question + "</label>\r\n"
					+ "<input type=\"text\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + value + "\" readonly>\r\n";
		
		return component;
	}
	
	private String createDateComponent(Questionnaire.QuestionnaireItemComponent item) {
		String question = item.getText();
		String id = item.getLinkId();
		String value = item.getAnswerOption().get(0).getValue().toString();
		String[] dates = value.split(";");
		
		String component = "	<label for=\"" + id + "\">" + question + "</label>\r\n"
				+ "     <div>\r\n"
				+ "     <span>Start:</span>\r\n"
				+ "		<input type=\"date\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + dates[0] + "\" readonly>\r\n"
				+ "     </div>\r\n"
				+ "     <div>\r\n"
				+ "     <span>Start:</span>\r\n"
				+ "		<input type=\"date\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + dates[1] + "\" readonly>\r\n"
				+ "     </div>\r\n";
		
		return component;
	}
	
	private String createChoiceComponent(Questionnaire.QuestionnaireItemComponent item) {
		String question = item.getText();
		String id = item.getLinkId();
		String values = item.getAnswerOption().get(item.getAnswerOption().size()-1).getValue().toString();
		List<QuestionnaireItemAnswerOptionComponent> options = item.getAnswerOption().subList(0, item.getAnswerOption().size()-1);
		
		String component = "<label>" + question + "</label>\r\n"
				+ "      <div class=\"checkbox-group\">\r\n";
		
		for (QuestionnaireItemAnswerOptionComponent option : options) {
			String code = option.getValueCoding().getCode();
			String display = option.getValueCoding().getDisplay();
			if (checkMark(values, code)) {
				component = component + "<label><input type=\"checkbox\" name=\"" + id + "\" value=\"" + code + "\" checked disabled><span>" + display + "</span></label>\r\n";
			} else {
				component = component + "<label><input type=\"checkbox\" name=\"" + id + "\" value=\"" + code + "\" disabled><span>" + display + "</span></label>\r\n";
			}
		}
		
		component = component + "      </div>\r\n";
		
		return component;
	}
	
	private boolean checkMark(String values, String code) {
		Boolean check = false;
		
		for (String value : values.split(";")) {
			if (value.equals(code)) {
				check = true;
			}
		}
		
		return check;
	}
	
	private String generateStyle() {
		String style = "<style>\r\n"
				+ "html {\r\n"
				+ "	height: 100%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "body {\r\n"
				+ "	margin: 0;\r\n"
				+ "	padding: 0;\r\n"
				+ "	font-family: sans-serif;\r\n"
				+ "	background: linear-gradient(#141e30, #243b55);\r\n"
				+ "	background-attachment: fixed;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "h1 {\r\n"
				+ "	color: #333333;\r\n"
				+ "	text-align: center;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".box {\r\n"
				+ "	max-width: 500px;\r\n"
				+ "	margin: 0 auto;\r\n"
				+ "	background-color: #ffffff;\r\n"
				+ "	padding: 20px;\r\n"
				+ "	border-radius: 5px;\r\n"
				+ "	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "label {\r\n"
				+ "	display: block;\r\n"
				+ "	margin-bottom: 10px;\r\n"
				+ "	font-weight: bold;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "input[type=\"text\"], input[type=\"date\"] {\r\n"
				+ "	width: 100%;\r\n"
				+ "	height: 20px;\r\n"
				+ "	padding: 10px;\r\n"
				+ "	border: 1px solid #cccccc;\r\n"
				+ "	border-radius: 4px;\r\n"
				+ "	box-sizing: border-box;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".checkbox-group {\r\n"
				+ "	display: grid;\r\n"
				+ "	grid-template-columns: repeat(3, 1fr);\r\n"
				+ "	grid-gap: 10px;\r\n"
				+ "	margin-bottom: 10px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".checkbox-group label {\r\n"
				+ "	display: flex;\r\n"
				+ "	align-items: center;\r\n"
				+ "	font-size: 14px;\r\n"
				+ "	font-weight: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".checkbox-group label input[type=\"checkbox\"] {\r\n"
				+ "	margin-right: 5px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".box input[type=\"submit\"] {\r\n"
				+ "	display: block;\r\n"
				+ "	margin: 20px auto;\r\n"
				+ "	background-color: #4CAF50;\r\n"
				+ "	color: #ffffff;\r\n"
				+ "	padding: 10px 20px;\r\n"
				+ "	border: none;\r\n"
				+ "	border-radius: 4px;\r\n"
				+ "	cursor: pointer;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".box input[type=\"submit\"]:hover {\r\n"
				+ "	background-color: #45a049;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".consent-question {\r\n"
				+ "	margin-top: 20px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".consent-question label {\r\n"
				+ "	font-weight: lighter;\r\n"
				+ "	font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".consent-question input[type=\"radio\"] {\r\n"
				+ "	margin-right: 5px;\r\n"
				+ "	transform: scale(1.2);\r\n"
				+ "	-webkit-appearance: none;\r\n"
				+ "	-moz-appearance: none;\r\n"
				+ "	appearance: none;\r\n"
				+ "	border: 2px solid #333333;\r\n"
				+ "	border-radius: 50%;\r\n"
				+ "	outline: none;\r\n"
				+ "	width: 16px;\r\n"
				+ "	height: 16px;\r\n"
				+ "	cursor: pointer;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".consent-question input[type=\"radio\"]:checked {\r\n"
				+ "	background-color: #333333;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".consent-question input[type=\"radio\"]:focus {\r\n"
				+ "	box-shadow: 0 0 3 px #333333;\r\n"
				+ "}\r\n"
				+ ".checkbox-group label input[type=\"checkbox\"]:checked + span {\r\n"
				+ "  font-weight: bold;\r\n"
				+ "}\r\n"
				+ "</style>\r\n";
		
		return style;
	}

}
