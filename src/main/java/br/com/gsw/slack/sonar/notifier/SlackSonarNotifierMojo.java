package br.com.gsw.slack.sonar.notifier;

import br.com.gsw.slack.sonar.notifier.plugin.factory.LogFactory;
import br.com.gsw.slack.sonar.notifier.plugin.factory.NotifierFactory;
import br.com.gsw.slack.sonar.notifier.plugin.factory.PluginLoadPropertiesFactory;
import br.com.gsw.slack.sonar.notifier.plugin.factory.PluginValidatorFactory;
import br.com.gsw.slack.sonar.notifier.scm.model.Scm;
import br.com.gsw.slack.sonar.notifier.slack.model.Slack;
import br.com.gsw.slack.sonar.notifier.sonar.model.Sonar;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "sonar-notifier",  requiresOnline = true)
public class SlackSonarNotifierMojo extends AbstractMojo {
    @Parameter
    private Sonar sonar;

    @Parameter
    private Slack slack;

    @Parameter
    private Scm scm;

    @Parameter(defaultValue = "${project}")
    private MavenProject mavenProject;

    @Parameter(defaultValue = "false", property = "skipNotifier")
    private Boolean skipNotifier;

    @Parameter(defaultValue = "false", property = "breakNotifier")
    private Boolean breakNotifier;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        LogFactory.init(getLog());
        final Log log = LogFactory.getInstance();

        log.info("------------------------------------------------------------------------");
        log.info("Slack Sonar Notifier Plugin");
        log.info("------------------------------------------------------------------------");

        log.debug(String.format("Receive sonar %s", sonar));
        log.debug(String.format("Receive slack %s", slack));
        log.debug(String.format("Receive scm %s", scm));
        log.debug(String.format("Receive skipNotifier %s", skipNotifier));
        log.debug(String.format("Receive breakNotifier %s", breakNotifier));

        log.debug("------------------------------------------------------------------------");

        if (skipNotifier) {
            log.info("Slack Sonar Notifier Plugin is skipped.");
            return;
        }

        sonar = PluginLoadPropertiesFactory.getInstance().sonar(sonar, mavenProject);
        slack = PluginLoadPropertiesFactory.getInstance().slack(slack);
        scm = PluginLoadPropertiesFactory.getInstance().scm(scm);

        PluginValidatorFactory.getInstance().sonar(sonar);
        PluginValidatorFactory.getInstance().slack(slack);

        NotifierFactory.getInstance().start(sonar, slack, scm, breakNotifier);
    }

}
