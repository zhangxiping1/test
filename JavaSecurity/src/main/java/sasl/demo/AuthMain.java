package sasl.demo;

import javax.security.auth.callback.*;
import javax.security.sasl.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class AuthMain {



    /**

     * @param args

     * @throws SaslException

     */

    public static void main(String[] args) throws SaslException {

        Map<String, String> props = new TreeMap<String, String>();

        props.put(Sasl.QOP, "auth");

        SaslServer ss = Sasl.createSaslServer("DIGEST-MD5", "xmpp", "java.com",

                props, new ServerCallbackHandler());

        byte[] token = new byte[0];

        byte[] challenge = ss.evaluateResponse(token);



        SaslClient sc = Sasl.createSaslClient(new String[] { "DIGEST-MD5" },

                "tony5", "xmpp", "java.com", null, new ClientCallbackHandler());

        byte response[];

        if (challenge != null) {

            response = sc.evaluateChallenge(challenge);

        } else {

            response = sc.evaluateChallenge(null);

        }



        ss.evaluateResponse(response);

        if (ss.isComplete()) {

            System.out.println("auth success");

        }

    }



}



class ClientCallbackHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException,

            UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {

            if (callbacks[i] instanceof NameCallback) {

                NameCallback ncb = (NameCallback) callbacks[i];

                ncb.setName("tony");

            } else if (callbacks[i] instanceof PasswordCallback) {

                PasswordCallback pcb = (PasswordCallback) callbacks[i];

                pcb.setPassword("admin1".toCharArray());

            } else if (callbacks[i] instanceof RealmCallback) {

                RealmCallback rcb = (RealmCallback) callbacks[i];

                rcb.setText("java.com");

            } else {

                throw new UnsupportedCallbackException(callbacks[i]);

            }

        }

    }

}



class ServerCallbackHandler implements CallbackHandler {



    public ServerCallbackHandler() {

    }



    public void handle(final Callback[] callbacks) throws IOException,

            UnsupportedCallbackException {



        for (Callback callback : callbacks) {

            if (callback instanceof RealmCallback) {

                //do your business

            } else if (callback instanceof NameCallback) {

                //do your business

            } else if (callback instanceof PasswordCallback) {

                ((PasswordCallback) callback).setPassword("admin1"

                        .toCharArray());

            } else if (callback instanceof AuthorizeCallback) {

                AuthorizeCallback authCallback = ((AuthorizeCallback) callback);

                authCallback.setAuthorized(true);

            } else {

                System.out.println(callback.getClass().getName());

                throw new UnsupportedCallbackException(callback,

                        "Unrecognized Callback");

            }

        }

    }

}
