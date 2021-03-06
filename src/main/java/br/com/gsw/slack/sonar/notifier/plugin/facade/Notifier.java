package br.com.gsw.slack.sonar.notifier.plugin.facade;

import br.com.gsw.slack.sonar.notifier.breaker.factory.BreakerFactory;
import br.com.gsw.slack.sonar.notifier.breaker.service.Breaker;
import br.com.gsw.slack.sonar.notifier.plugin.factory.LogFactory;
import br.com.gsw.slack.sonar.notifier.scm.model.Scm;
import br.com.gsw.slack.sonar.notifier.slack.adapter.SlackRequestAdapter;
import br.com.gsw.slack.sonar.notifier.slack.factory.SlackPusherFactory;
import br.com.gsw.slack.sonar.notifier.slack.factory.SlackRequestAdapterFactory;
import br.com.gsw.slack.sonar.notifier.slack.model.Slack;
import br.com.gsw.slack.sonar.notifier.slack.service.SlackPusher;
import br.com.gsw.slack.sonar.notifier.slack.web.model.SlackRequest;
import br.com.gsw.slack.sonar.notifier.sonar.adapter.SonarAdapter;
import br.com.gsw.slack.sonar.notifier.sonar.factory.OnlyErrorsFilterFactory;
import br.com.gsw.slack.sonar.notifier.sonar.factory.SonarAdapterFactory;
import br.com.gsw.slack.sonar.notifier.sonar.model.Sonar;
import br.com.gsw.slack.sonar.notifier.sonar.model.SonarStats;
import br.com.gsw.slack.sonar.notifier.sonar.service.OnlyErrorsFilter;
import org.apache.maven.plugin.logging.Log;

public class Notifier {
    private static final Log LOGGER = LogFactory.getInstance();

    private SonarAdapter sonarAdapter = SonarAdapterFactory.getInstance();
    private SlackRequestAdapter slackRequestAdapter = SlackRequestAdapterFactory.getInstance();
    private SlackPusher slackPusher = SlackPusherFactory.getInstance();
    private OnlyErrorsFilter onlyErrorsFilter = OnlyErrorsFilterFactory.getInstance();
    private Breaker breaker = BreakerFactory.getInstance();

    public void start(final Sonar sonar, final Slack slack, final Scm scm, final Boolean toBreak) {
        LOGGER.debug("Starting notifier...");

        SonarStats sonarStats = sonarAdapter.adapter(sonar);

        if (slack.getOnlyErrors() != null && slack.getOnlyErrors()) {
            sonarStats = onlyErrorsFilter.filter(sonarStats, sonar.getCoverage());
        }

        final SlackRequest slackRequest = slackRequestAdapter.adapter(sonarStats, scm);
        final String projectName = sonarStats.getProject().getName();

        if (slackRequest == null) {
            LOGGER.info(String.format("Not found errors in project %s", projectName));
            return;
        }
        slackPusher.slackPusher(slack, slackRequest);
        breaker.toBreak(toBreak, projectName);
    }
}