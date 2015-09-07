package com.joggingtrackerapp;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;


public class application extends Application {
    @Override
    public void onCreate () {
        super.onCreate();
        CookieHandler.setDefault(new CookieManager(new CookieStore() {
            @Override
            public void add (URI uri, HttpCookie cookie) {

            }

            @Override
            public List<HttpCookie> get (URI uri) {
                return null;
            }

            @Override
            public List<HttpCookie> getCookies () {
                return null;
            }

            @Override
            public List<URI> getURIs () {
                return null;
            }

            @Override
            public boolean remove (URI uri, HttpCookie cookie) {
                return false;
            }

            @Override
            public boolean removeAll () {
                return false;
            }
        }, CookiePolicy.ACCEPT_ALL));

    }
}
