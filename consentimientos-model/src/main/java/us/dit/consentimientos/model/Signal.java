/**
 * 
 */
package us.dit.consentimientos.model;

/**
 * Clase para definir los mensajes que se incluyen en las señales
 */
public class Signal{
	private String signalName;
	private Object message;	


	public Object getMessage() {
		
		return this.message;
	}
	
	public String getName() {
		return signalName;
	}
	public void setName(String signalName) {
		this.signalName = signalName;
	}
	public void setMessage(Object message) {
		this.message = message;
	}	

}
