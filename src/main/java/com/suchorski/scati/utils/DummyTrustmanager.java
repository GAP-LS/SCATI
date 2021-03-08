package com.suchorski.scati.utils;

import javax.net.ssl.X509TrustManager;

public class DummyTrustmanager implements X509TrustManager {

	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
		return;
	}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
		return;
	}

	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
