package us.dit.consentimientos.service.services.kie;

import javax.jms.BytesMessage;

import org.jbpm.process.workitem.jms.JMSSignalReceiver;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * https://github.com/kiegroup/jbpm/blob/main/jbpm-workitems/jbpm-workitems-jms/src/main/java/org/jbpm/process/workitem/jms/JMSSignalReceiver.java
 */
//@Component
public class ReceiveJMSEvents extends JMSSignalReceiver {
	private static final Logger log = LogManager.getLogger();

	@JmsListener(destination = "ExternalSignalQueue")
	public void processMessage(BytesMessage content) {
		log.info("Recibido mensaje en ReceiveJMSEvents " + content);
		super.onMessage(content);
	}

}