package de.wdwelab.camunda;

import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.ProcessScenario;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public abstract class BaseTest {

  @Rule
  public ProcessEngineRule engine = TestCoverageProcessEngineRuleBuilder.create().build();

  @Mock
  public ProcessScenario musikaboFreischaltung;

  public void passed(VerificationMode mode, String... activities) {
    for (String activity: activities) {
      verify(musikaboFreischaltung, mode).hasFinished(activity);
    }
  }

  @Before
  public void init() {
    initMocks(this);
  }

}
