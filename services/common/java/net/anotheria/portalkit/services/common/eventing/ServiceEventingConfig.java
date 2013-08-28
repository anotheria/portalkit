package net.anotheria.portalkit.services.common.eventing;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.LoggerFactory;

/**
 * Config for service operation eventing.
 *
 * @author Alex Osadchy
 */
@ConfigureMe(name = "pk-service-eventing")
public final class ServiceEventingConfig {

	/**
	 * {@link ServiceEventingConfig} singleton instance.
	 */
	private static volatile ServiceEventingConfig instance;

	/**
	 * If {@code true} eventing will be performed in asynchronous mode.
	 */
	private boolean asynchronousMode = false;

	/**
	 * Queue size.
	 */
	private int queueSize = 1000;

	/**
	 * Sleep time.
	 */
	private long sleepTime = 50;

	/**
	 * Default private constructor.
	 */
	private ServiceEventingConfig() {
		try {
			ConfigurationManager.INSTANCE.configure(this);
		} catch (Exception e) {
			String failMsg = "ServiceOperationEventingConfig configuration failed. Default values will be used";
			LoggerFactory.getLogger(ServiceEventingConfig.class).error(failMsg, e);
		}
	}

	public boolean isAsynchronousMode() {
		return asynchronousMode;
	}

	public void setAsynchronousMode(boolean asynchronousMode) {
		this.asynchronousMode = asynchronousMode;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	/**
	 * Returns {@link ServiceEventingConfig} instance.
	 *
	 * @return {@link ServiceEventingConfig} instance
	 */
	public static ServiceEventingConfig getInstance() {
		if (instance != null)
			return instance;

		synchronized (ServiceEventingConfig.class) {
			if (instance == null)
				instance = new ServiceEventingConfig();
		}

		return instance;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ServiceOperationEventingConfig{");
		sb.append("asynchronousMode=").append(asynchronousMode);
		sb.append(", queueSize=").append(queueSize);
		sb.append(", sleepTime=").append(sleepTime);
		sb.append('}');
		return sb.toString();
	}
}



