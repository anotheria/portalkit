package net.anotheria.portalkit.apis.online;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoplass.api.NoLoggedInUserException;
import net.anotheria.anoplass.api.generic.login.LoginAPI;
import net.anotheria.anoplass.api.generic.observation.ObservationAPI;
import net.anotheria.anoplass.api.generic.observation.ObservationSubjects;
import net.anotheria.anoplass.api.generic.observation.Observer;
import net.anotheria.anoplass.api.generic.observation.SubjectUpdateEvent;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anoprise.processor.ElementWorker;
import net.anotheria.anoprise.processor.QueuedMultiProcessor;
import net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder;
import net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.AccountIsOfflineException;
import net.anotheria.portalkit.services.online.AccountIsOnlineException;
import net.anotheria.portalkit.services.online.NoActivityDataFoundException;
import net.anotheria.portalkit.services.online.OnlineService;
import net.anotheria.portalkit.services.online.OnlineServiceException;
import net.anotheria.util.StringUtils;
import net.anotheria.util.log.LogMessageUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link OnlineAPI} implementation.
 * Implementation serves as Observer for subjects listed in {@link SupportedActivityEvents}.
 *
 * @author h3llka
 */
public class OnlineAPIImpl extends AbstractAPIImpl implements OnlineAPI, Observer {
    /**
     * API session attribute name - which stores last activity update time.
     * AS activity currently we will understand  notifyLogin || notifyUserActivity call to the {@link OnlineService}.
     */
    private static final String MY_ACTIVITY_LAST_UPDATE_CALL_TIMESTAMP = "myActivityLastUpdate";
    /**
     * {@link OnlineService} instance.
     */
    private OnlineService onlineService;
    /**
     * {@link LoginAPI} instance.
     */
    private LoginAPI loginAPI;
    /**
     * {@link ObservationAPI} instance.
     */
    private ObservationAPI observationAPI;
    /**
     * {@link OnlineAPIConfiguration} configuration.
     */
    private OnlineAPIConfiguration config;

    /**
     * QueuedMultiProcessor  for ActivityEvent.
     * Allow to communicate with {@link OnlineService} via async gateway.
     */
    private QueuedMultiProcessor<ActivityEvent> processor;

    /**
     * Supported Observation subjects.
     */
    private static final List<String> SUPPORTED_SUBJECTS;

    /**
     * Static initialisation.
     */
    static {
        SUPPORTED_SUBJECTS = new ArrayList<String>(SupportedActivityEvents.values().length);
        for (SupportedActivityEvents sb : SupportedActivityEvents.values())
            SUPPORTED_SUBJECTS.add(sb.subject);
    }

    @Override
    public void deInit() {
        super.deInit();
        onlineService = null;
        loginAPI = null;
        //unRegistering observer
        observationAPI.unRegisterObserver(this, SUPPORTED_SUBJECTS.toArray(new String[SUPPORTED_SUBJECTS.size()]));
        observationAPI = null;
        config = null;
        processor = null;
    }

    @Override
    public void init() throws APIInitException {
        super.init();
        try {
            onlineService = MetaFactory.get(OnlineService.class);
        } catch (MetaFactoryException e) {
            log.fatal("OnlineService init failed", e);
            throw new APIInitException("OnlineService init failed", e);
        }
        loginAPI = APIFinder.findAPI(LoginAPI.class);
        observationAPI = APIFinder.findAPI(ObservationAPI.class);
        config = OnlineAPIConfiguration.getInstance();

        //registering observer
        observationAPI.registerObserver(this, SUPPORTED_SUBJECTS.toArray(new String[SUPPORTED_SUBJECTS.size()]));


        //  processor init - for Async stuff!
        processor = new QueuedMultiProcessorBuilder<ActivityEvent>().
                setSleepTime(config.getActivityEventProcessorSleepTime()).
                setQueueSize(config.getActivityEventProcessorSize()).
                setProcessorChannels(config.getActivityEventProcessorSize()).
                setProcessingLog(log).
                attachMoskitoLoggers("OnlineAPI : ActivityEvent processor", "API", "default").
                build("ActivityEventMultiProcessor", new ActivityEventWorker());
        processor.start();

    }

    @Override
    public boolean isOnline(AccountId accountId) throws OnlineAPIException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming accountId parameter is not valid");
        try {
            return onlineService.isOnline(accountId);
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, accountId);
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }

    }

    @Override
    public List<AccountId> readOnlineAccounts(OnlineAccountReadCriteriaAO criteria) throws OnlineAPIException {
        if (criteria == null)
            throw new IllegalArgumentException("Incoming criteria parameter is not valid");
        if (criteria.getCriteria() == null)
            throw new IllegalArgumentException("Incoming criteria parameter is not valid, as internal criteria is not valid");

        try {
            return onlineService.readOnlineUsers(criteria.getCriteria());
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, criteria);
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }
    }

    @Override
    public long readLastLoginTime(AccountId account) throws OnlineAPIException {
        if (account == null)
            throw new IllegalArgumentException("Incoming accountId parameter is not valid");
        try {
            return onlineService.readLastLogin(account);
        } catch (NoActivityDataFoundException e) {
            return 0; // TODO :actually we can throw exception here.... but  this will be useful only for administration stuff.... Waiting for feedback
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, account);
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }
    }

    @Override
    public long readLastActivityTime(AccountId account) throws OnlineAPIException {
        if (account == null)
            throw new IllegalArgumentException("Incoming accountId parameter is not valid");
        try {
            return onlineService.readLastActivity(account);
        } catch (NoActivityDataFoundException e) {
            return 0; // TODO :actually we can throw exception here.... but  this will be useful only for administration stuff.... Waiting for feedback
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, account);
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }
    }

    @Override
    public Map<AccountId, Long> readLastLoginTime(List<AccountId> accounts) throws OnlineAPIException {
        if (accounts == null)
            throw new IllegalArgumentException("Incoming parameter : [accounts] collection in not valid");
        if (accounts.isEmpty())
            return new HashMap<AccountId, Long>();
        try {
            return onlineService.readLastLoginTime(accounts);
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, accounts.size());
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }

    }

    @Override
    public Map<AccountId, Long> readLastActivityTime(List<AccountId> accounts) throws OnlineAPIException {
        if (accounts == null)
            throw new IllegalArgumentException("Incoming parameter : [accounts] collection in not valid");
        if (accounts.isEmpty())
            return new HashMap<AccountId, Long>();
        try {
            return onlineService.readLastLoginTime(accounts);
        } catch (OnlineServiceException e) {
            final String msg = LogMessageUtil.failMsg(e, accounts.size());
            log.error(msg, e);
            throw new OnlineAPIException(msg, e);
        }
    }


    @Override
    public void notifySubjectUpdatedForCurrentUser(SubjectUpdateEvent event) {
        if (event == null)
            return;
        if (!isSubjectSupported(event.getSubject()))
            return;

        SupportedActivityEvents subject = SupportedActivityEvents.getBySubjectString(event.getSubject());
        try {
            AccountId account = getLoggedInAccountId();
            //checking if we can send activity updates  // no matter which
            if (!activityUpdateRequired())
                return;
            switch (subject) {
                case LOGIN:
                    //async
                    if (config.isPerformNotificationAsync()) {
                        handleEventAsync(account, SupportedActivityEvents.LOGIN);
                        resetLastActivityUpdateTime();
                        return;
                    }
                    //sync
                    performLogin(account);
                    resetLastActivityUpdateTime();
                    break;

                case ACTIVITY:
                    //async
                    if (config.isPerformNotificationAsync()) {
                        handleEventAsync(account, SupportedActivityEvents.ACTIVITY);
                        resetLastActivityUpdateTime();
                        return;
                    }
                    //sync call!
                    performActivityUpdate(account);
                    resetLastActivityUpdateTime();

                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Subject " + subject + " can't be handled for current user.");
                    break;
            }
        } catch (OnlineServiceException e) {
            log.warn(LogMessageUtil.failMsg(e, event), e);
        } catch (OnlineAPIException e) {
            log.warn(event + " notification failed, cause getLoggedInAccountId failed" + e.getMessage());
        }
    }

    /**
     * Return {@code true} in case when last activity update was performed more than {@link OnlineAPIConfiguration#getActivityUpdateInterval()}
     * time ago.
     *
     * @return boolean
     */
    private boolean activityUpdateRequired() {
        Long lastActivityUpdate = getLastActivityUpdateTime();
        //check if we should call  update!
        return lastActivityUpdate == null || (System.currentTimeMillis() - lastActivityUpdate) >= config.getActivityUpdateInterval();
    }


    @Override
    public void notifySubjectUpdatedForUser(SubjectUpdateEvent event) {
        if (event == null)
            return;
        if (!isSubjectSupported(event.getSubject()))
            return;

        SupportedActivityEvents subject = SupportedActivityEvents.getBySubjectString(event.getSubject());
        switch (subject) {
            case LOGOUT:
                if (StringUtils.isEmpty(event.getTargetUserId())) {
                    log.warn("notifySubjectUpdatedForUser(" + event + ") can't be handled as Logout case, cause AccountId is absent");
                    return;
                }
                //async
                if (config.isPerformNotificationAsync()) {
                    handleEventAsync(new AccountId(event.getTargetUserId()), SupportedActivityEvents.LOGOUT);
                    return;
                }
                //sync
                try {
                    performLogout(new AccountId(event.getTargetUserId()));
                } catch (OnlineServiceException e) {
                    log.warn(LogMessageUtil.failMsg(e, event), e);
                }
                break;
            default:
                if (log.isDebugEnabled())
                    log.debug("Subject " + subject + " can't be handled for current user.");
                break;
        }


    }

    /**
     * Handling in async way.
     *
     * @param accountId {@link AccountId}
     * @param type      {@link SupportedActivityEvents
     */
    private void handleEventAsync(final AccountId accountId, final SupportedActivityEvents type) {
        try {
            processor.addToQueue(new ActivityEvent(accountId, type));
        } catch (UnrecoverableQueueOverflowException e) {
            log.error("queue is full! event [" + type.name() + "] for account[" + accountId + "] will be skipped! " + e.getMessage());
        }
    }

    /**
     * Delegate call to {@link OnlineService#notifyLoggedIn(AccountId)} (AccountId)}.
     * In case of {@link AccountIsOnlineException} -  if {@link OnlineAPIConfiguration#isPerformActivityUpdateOnLoginOnlineErrors} enabled - system will try
     * to  call {@link this#performActivityUpdate(AccountId)}.
     *
     * @param accountId {@link AccountId}
     * @throws OnlineServiceException on errors
     */
    private void performLogin(final AccountId accountId) throws OnlineServiceException {
        try {
            onlineService.notifyLoggedIn(accountId);
        } catch (AccountIsOnlineException e) {
            if (config.isPerformActivityUpdateOnLoginOnlineErrors())
                performActivityUpdate(accountId);
        }
    }

    /**
     * Delegate call to {@link OnlineService#notifyUserActivity(AccountId)} (AccountId)}.
     * In case of {@link AccountIsOfflineException} -  if {@link OnlineAPIConfiguration#isPerformLoginNotificationOnActivityOfflineErrors} enabled - system will try
     * to  call {@link this#performLogin(AccountId)}.
     *
     * @param accountId {@link AccountId}
     * @throws OnlineServiceException on errors
     */
    private void performActivityUpdate(final AccountId accountId) throws OnlineServiceException {
        try {
            onlineService.notifyUserActivity(accountId);
        } catch (AccountIsOfflineException e) {
            if (config.isPerformLoginNotificationOnActivityOfflineErrors())
                performLogin(accountId);
        }
    }

    /**
     * Delegate call to {@link OnlineService#notifyLoggedOut(AccountId)}.
     *
     * @param accountId {@link AccountId}
     * @throws OnlineServiceException on errors
     */
    private void performLogout(final AccountId accountId) throws OnlineServiceException {
        try {
            onlineService.notifyLoggedOut(accountId);
        } catch (AccountIsOfflineException e) {
            log.trace(e.getMessage(), e);
        }
    }

    /**
     * Return last activity update time - from session.
     *
     * @return time when current user activity was updated
     */
    private Long getLastActivityUpdateTime() {
        Object lastUpdate = getAttributeFromSession(MY_ACTIVITY_LAST_UPDATE_CALL_TIMESTAMP);
        if (lastUpdate instanceof Long)
            return Long.class.cast(lastUpdate);
        return null;
    }

    /**
     * Set 'myActivityLastUpdate' ti current time.
     */
    private void resetLastActivityUpdateTime() {
        setAttributeInSession(MY_ACTIVITY_LAST_UPDATE_CALL_TIMESTAMP, System.currentTimeMillis());
    }


    /**
     * Return loggedIn {@link AccountId}.
     *
     * @return {@link AccountId} if there is such
     * @throws OnlineAPIException in case if there is no loggedIn user found, or on other errors from {@link LoginAPI}
     */
    private AccountId getLoggedInAccountId() throws OnlineAPIException {
        if (loginAPI.isLogedIn())
            try {
                return new AccountId(loginAPI.getLogedUserId());
            } catch (APIException e) {
                boolean notLoggedIN = e instanceof NoLoggedInUserException;
                if (notLoggedIN)
                    throw new OnlineAPIException("No logged in user found");

                final String msg = LogMessageUtil.failMsg(e);
                log.error(msg, e);
                throw new OnlineAPIException(msg, e);
            }
        throw new OnlineAPIException("No logged in user found");
    }

    /**
     * Return {@code true} if incoming subject can be processed.
     *
     * @param subject incoming observation subject
     * @return boolean value
     */
    private static boolean isSubjectSupported(final String subject) {
        return !StringUtils.isEmpty(subject) && SUPPORTED_SUBJECTS.contains(subject);
    }


    /**
     * Activity event bean - for activity async handling.
     */
    private static class ActivityEvent implements Serializable {
        /**
         * Basic serial version UID.
         */
        private static final long serialVersionUID = -7038000973536177720L;
        /**
         * AccountId.
         */
        private AccountId account;

        /**
         * SupportedActivityEvents.
         */
        private SupportedActivityEvents subject;

        /**
         * Constructor.
         *
         * @param account {@link AccountId}
         * @param subject {@link SupportedActivityEvents}
         */
        public ActivityEvent(AccountId account, SupportedActivityEvents subject) {
            if (account == null)
                throw new IllegalArgumentException("not valid accountId parameter");
            if (subject == null)
                throw new IllegalArgumentException("not valid subject parameter");
            this.account = account;
            this.subject = subject;
        }

        public AccountId getAccount() {
            return account;
        }

        public SupportedActivityEvents getSubject() {
            return subject;
        }

        @Override
        public String toString() {
            return "ActivityEvent{" +
                    "account=" + account +
                    ", subject=" + subject +
                    '}';
        }
    }

    /**
     * Activity element worker which allow async - communications with {@link OnlineService}.
     */
    private class ActivityEventWorker implements ElementWorker<ActivityEvent> {

        @Override
        public void doWork(ActivityEvent activityEvent) throws Exception {
            if (activityEvent == null)
                throw new IllegalArgumentException("activityEvent is not valid");
            try {
                switch (activityEvent.getSubject()) {
                    case ACTIVITY:
                        performActivityUpdate(activityEvent.getAccount());
                        break;
                    case LOGIN:
                        performLogin(activityEvent.getAccount());
                        break;
                    case LOGOUT:
                        performLogout(activityEvent.getAccount());
                        break;
                    default:
                        throw new AssertionError(activityEvent.getSubject() + " notification is not supported");
                }
            } catch (OnlineServiceException e) {
                log.error(LogMessageUtil.failMsg(e, activityEvent), e);
            }

        }
    }


    /**
     * Enumeration of supported Observation subjects.
     */
    private static enum SupportedActivityEvents {
        /**
         * Login.
         */
        LOGIN(ObservationSubjects.LOGIN),
        /**
         * Logout.
         */
        LOGOUT(ObservationSubjects.LOGOUT),
        /**
         * Activity update.
         */
        ACTIVITY(ObservationSubjects.ACTIVITY_UPDATE);
        /**
         * String value.
         */
        private String subject;

        /**
         * Constructor.
         *
         * @param value subject string
         */
        SupportedActivityEvents(final String value) {
            this.subject = value;
        }

        /**
         * Return {@link SupportedActivityEvents} represented by incoming 'value' parameter.
         * Note: {@link EnumConstantNotPresentException} will be thrown in case if no such subject found!
         *
         * @param value incoming subject
         * @return {@link SupportedActivityEvents}
         */
        public static SupportedActivityEvents getBySubjectString(final String value) {
            for (SupportedActivityEvents subject : values())
                if (subject.subject.equals(value))
                    return subject;
            //noinspection unchecked
            throw new EnumConstantNotPresentException(SupportedActivityEvents.class, value);

        }

    }

}
