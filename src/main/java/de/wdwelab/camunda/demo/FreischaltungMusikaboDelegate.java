package de.wdwelab.camunda.demo;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class FreischaltungMusikaboDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
    String serviceTaskId = execution.getCurrentActivityId();
    if ("kundendaten-laden".equals(serviceTaskId))
      kundendatenLaden(execution);
    else if ("vorauszahlung-einziehen".equals(serviceTaskId))
      vorauszahlungEinziehen(execution);
    String serviceTaskName = execution.getCurrentActivityName();
    String processInstanceId = execution.getProcessInstanceId();
    System.out.println("Executing Process Instance [" + processInstanceId + "] Service [" + serviceTaskName + "]");
  }

  private void kundendatenLaden(DelegateExecution execution) {
    Object kunde = execution.getVariable("kunde");
    if (kunde == null) {
      execution.setVariable("vertragskunde", true);
      execution.setVariable("kundenwert", "A");
      execution.setVariable("mahnung", false);
    } else if ("1".equals(kunde.toString())) {
      execution.setVariable("vertragskunde", true);
      execution.setVariable("kundenwert", "A");
      execution.setVariable("mahnung", true);
    } else if ("2".equals(kunde.toString())) {
      execution.setVariable("vertragskunde", true);
      execution.setVariable("kundenwert", "B");
      execution.setVariable("mahnung", true);
    } else {
      execution.setVariable("vertragskunde", false);
      execution.setVariable("kundenwert", "A");
      execution.setVariable("mahnung", false);
    }
  }

  private void vorauszahlungEinziehen(DelegateExecution execution) {
    boolean vertragskunde = (boolean) execution.getVariable("vertragskunde");
    boolean mahnung = (boolean) execution.getVariable("mahnung");
    Object kreditkarte =  execution.getVariable("kreditkarte");
    if (kreditkarte == null && (mahnung || !vertragskunde))
      throw new BpmnError("einzug-nicht-moeglich");
  }

}
