/**
 * Created by deoxys on 29.07.16.
 */

function process(error) {
    if (error) {
        const VALIDATION_STYLE = "has-error";
        switch (error) {
            case "wrong_email":
                document.getElementById("email").classList.add(VALIDATION_STYLE);
                document.getElementById("email-help").innerHTML = "Email doesn't match";

                // Remove old errors
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                break;
            case "wrong_password":
                document.getElementById("password").classList.add(VALIDATION_STYLE);
                document.getElementById("password-help").innerHTML = "Password doesn't match";

                // Remove old errors
                document.getElementById("email").classList.remove(VALIDATION_STYLE);
                break;
            case "account_closed":
                document.getElementById("email").classList.add(VALIDATION_STYLE);
                document.getElementById("email-help").innerHTML = "Account was closed";
                
                // Remove old errors
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                break;
            default:
                break;
        }
    }
}