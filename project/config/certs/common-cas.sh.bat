keytool -importcert -alias startssl-ca -v -file StartComCertificationAuthority.crt -storepass common-cas -keystore common-cas.jks
keytool -importcert -alias geotrust-ca -v -file GeoTrustGlobalCA.crt -storepass common-cas -keystore common-cas.jks
keytool -importcert -alias verisign-ca -v -file VeriSignClass3PublicPrimaryCertificationAuthority-G5.crt -storepass common-cas -keystore common-cas.jks
