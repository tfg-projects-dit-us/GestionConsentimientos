package us.dit.consentimientos.service.services.fhir;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

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

}
