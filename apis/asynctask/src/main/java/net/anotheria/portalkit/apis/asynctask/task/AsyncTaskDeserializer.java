package net.anotheria.portalkit.apis.asynctask.task;

import net.anotheria.anoplass.api.APIException;

public interface AsyncTaskDeserializer {

    AsyncTask deserialize(String json) throws APIException;
}
