/**
 * Created by deoxys on 29.07.16.
 */

function embedErrorMessage(error) {
    if (error) {
        const VALIDATION_STYLE = "has-error";
        switch (error) {
            case "email_dup":
                document.getElementById("email").classList.add(VALIDATION_STYLE);
                document.getElementById("email-help").innerHTML = "Email is already reserved";

                // Remove old errors
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("surname").classList.remove(VALIDATION_STYLE);
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