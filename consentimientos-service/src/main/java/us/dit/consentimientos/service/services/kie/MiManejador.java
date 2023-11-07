package us.dit.consentimientos.service.services.kie;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.springframework.stereotype.Component;

@Component("MiTarea")
public class MiManejador implements WorkItemHandler {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		logger.info("Se está ejecutando mi manejador con los detalles de workItem " + workItem);
		
		Map<String,Object> parametros = workItem.getParameters();
		logger.info("fhribase de entrada: "+(String)parametros.get("fhirbase"));
		logger.info("id de recurso: "+ (String) parametros.get("resourceId"));
		manager.completeWorkItem(workItem.getId(), null);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
