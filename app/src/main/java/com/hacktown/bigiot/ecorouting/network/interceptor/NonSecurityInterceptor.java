package com.hacktown.bigiot.ecorouting.network.interceptor;

import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.Interceptor;

public class NonSecurityInterceptor implements Interceptor {
    /**
     * Intercepts the chain and calls onOnIntercept defined method
     * @param chain to be intercepted
     * @return the result specified in onOnIntercept method
     * @throws IOException thrown
     */
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        return onOnIntercept(chain);
    }

    /**
     * Definition of an interceptor in order to handle Time Out Exceptions
     */
    private okhttp3.Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            okhttp3.Response response = chain.proceed(chain.request());
            return response;
        }
        catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
