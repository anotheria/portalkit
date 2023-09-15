package net.anotheria.portalkit.services.phoneverification;

public enum VerifyResponse {

    VERIFICATION_SUCCESS("approved"),

    VERIFICATION_FAILED("pending");

    private final String response;


    VerifyResponse(String response) {
        this.response = response;
    }


    public String getResponse() {
        return response;
    }

    public static VerifyResponse getResponseByValue(String value) {
        for (VerifyResponse res : VerifyResponse.values()) {
            if (res.getResponse().equals(value)) {
                return res;
            }
        }
        return null;
    }

}