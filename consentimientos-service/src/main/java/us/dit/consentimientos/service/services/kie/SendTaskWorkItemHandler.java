package us.dit.consentimientos.service.services.kie;

import javax.jms.ConnectionFactory;


import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.kie.api.runtime.process.WorkItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;

@Component("ExternalSendTask")
public class SendTaskWorkItemHandler extends AbstractLogOrThrowWorkItemHandler implements Cacheable {
	@Autowired
	private KieUtilService kie;

	private static final Logger logger = LogManager.getLogger();


    public SendTaskWorkItemHandler() {
        
    	logger.info("Creado SendTaskWorkITemHandler");
    }


 

    @Override
    public void executeWorkItem(WorkItem workItem,
                                WorkItemManager manager) {
    	logger.info("Ejecuto el método executeWorkITem de SendTaskWorkITem");
    	String type= workItem.getParameter("Signal").toString();
    	Object message=workItem.getParameter("Data");
        kie.sendSignal(type, message);        
    }

    @Override
    public void abortWorkItem(WorkItem workItem,
                              WorkItemManager manager) {
        // no-op
    }




	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}  
}