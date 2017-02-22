package com.example.tarunkukreja.weather.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by tarunkukreja on 22/02/17.
 */

/** We do not need this for Weather as there is no authentication required
 * for the weather app so just creating a stub code for the Authenticator . Even if our app
 * does'nt require Authenticator we still need to define it for using sync adapters .
 *  You also need to provide a bound Service that allows the sync adapter framework to call the authenticator's methods.Follow
 * the link on how to create a stub code
 * https://developer.android.com/training/sync-adapters/creating-authenticator.html
 */

public class WeatherAuthenticator extends AbstractAccountAuthenticator {


    public WeatherAuthenticator(Context context) {
        super(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        throw new UnsupportedOperationException() ;
    }
}
