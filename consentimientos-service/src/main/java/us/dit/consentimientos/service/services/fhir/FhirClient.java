package us.dit.consentimientos.service.services.fhir;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import jdk.internal.org.jline.utils.Log;

@Service
public class FhirClient {
	private static final Logger logger = LogManager.getLogger();
	public Questionnaire getQuestionnaire(String serverBase,String questionnaireId) {
		// We're connecting to a DSTU1 compliant server in this example
		FhirContext ctx = FhirContext.forR5();
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		logger.info("Busco cuestionario " + questionnaireId+" en "+serverBase);
		Questionnaire questionnaire =
		      client.read().resource(Questionnaire.class).withId(questionnaireId).execute();

		

		logger.info("Localizado cuestionario " + questionnaire.getId());
		return questionnaire;

	}
	public String saveQuestionnaireResponse(String serverBase,QuestionnaireResponse resp) {
		FhirContext ctx = FhirContext.forR5();
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
	    MethodOutcome outcome = client.create()
		      .resource(resp)
		      .prettyPrint()
		      .encodedJson()
		      .execute();

		// The MethodOutcome object will contain information about the
		// response from the server, including the ID of the created
		// resource, the OperationOutcome response, etc. (assuming that
		// any of these things were provided by the server! They may not
		// always be)
	   
	    String respId=outcome.getId().getValueAsString();
	    logger.info("Id del recurso persistido "+respId);
		return respId;	
	}

}
