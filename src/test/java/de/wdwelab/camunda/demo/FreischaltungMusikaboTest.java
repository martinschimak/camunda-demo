package de.wdwelab.camunda.demo;

import de.wdwelab.camunda.BaseTest;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.scenario.Scenario;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
@Deployment(resources = {"freischaltung-musikabo.bpmn", "vorauszahlung-ermitteln.dmn"})
public class FreischaltungMusikaboTest extends BaseTest {

  @Test
  public void vertragskunde_konto_ausgeglichen() {
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo").execute();
    passed(times(1), "musikabo-freigeschalten");
  }

  @Test
  public void vertragskunde_a_gemahnt() {
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "1")).execute();
    passed(times(1), "musikabo-freigeschalten");
  }

  @Test
  public void vertragskunde_b_gemahnt_zahlung_ok() {
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "2").putValue("kreditkarte", "123")).execute();
    passed(times(1), "musikabo-freigeschalten");
    passed(times(1), "vorauszahlung-einziehen");
    passed(never(), "zahlungsdaten-bereitstellen");
  }

  @Test
  public void prepaid_zahlung_ok() {
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "3").putValue("kreditkarte", "123")).execute();
    passed(times(1), "musikabo-freigeschalten");
    passed(times(1), "vorauszahlung-einziehen");
    passed(never(), "zahlungsdaten-bereitstellen");
  }

  @Test
  public void prepaid_zahlung_nicht_ok() {
    when(musikaboFreischaltung.waitsAtUserTask(anyString())).thenReturn(
        task -> task.complete(Variables.createVariables().putValue("kreditkarte", "123")
        ));
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "3")).execute();
    passed(times(1), "musikabo-freigeschalten");
    passed(times(2), "vorauszahlung-einziehen");
    passed(times(1), "zahlungsdaten-bereitstellen");
  }

  @Test
  public void vertragskunde_b_gemahnt_zahlung_nicht_ok() {
    when(musikaboFreischaltung.waitsAtUserTask(anyString())).thenReturn(
        task -> task.complete(Variables.createVariables().putValue("kreditkarte", "123")
    ));
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "2")).execute();
    passed(times(1), "musikabo-freigeschalten");
    passed(times(2), "vorauszahlung-einziehen");
    passed(times(1), "zahlungsdaten-bereitstellen");
  }

  @Test
  public void vertragskunde_b_gemahnt_zahlung_nicht_ok_fristablauf() {
    when(musikaboFreischaltung.waitsAtUserTask(anyString())).thenReturn(
        task -> task.defer("P10D", () -> task.complete(Variables.createVariables().putValue("kreditkarte", "123"))
    ));
    Scenario.run(musikaboFreischaltung)
        .startByKey("freischaltung-musikabo", Variables.createVariables().putValue("kunde", "2")).execute();
    passed(never(), "musikabo-freigeschalten");
    passed(times(1), "vorauszahlung-einziehen");
    passed(times(1), "zahlungsdaten-bereitstellen");
    passed(times(1), "musikabo-nicht-freigeschalten");
  }

}
