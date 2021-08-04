package net.anotheria.portalkit.apis.asynctask.task;

import net.anotheria.anoplass.api.APIException;

public interface AsyncTaskSerializer {

    String serialize(AsyncTask asyncTask) throws APIException;
}
