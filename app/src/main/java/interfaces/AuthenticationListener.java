package interfaces;

/**
 * Created by root on 08.03.18.
 */

public interface AuthenticationListener {
    void onCodeReceived(String auth_token);
}
