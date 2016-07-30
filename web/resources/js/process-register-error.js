/**
 * Created by deoxys on 29.07.16.
 */

function process(error) {
    if (error) {
        const VALIDATION_STYLE = "has-error";
        switch (error) {
            case "wrong_email":
                document.getElementById("email").classList.add(VALIDATION_STYLE);
                document.getElementById("email-help").innerHTML = "Wrong email";

                // Remove old errors
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("surname").classList.remove(VALIDATION_STYLE);
                break;
            case "email_dup":
                document.getElementById("email").classList.add(VALIDATION_STYLE);
                document.getElementById("email-help").innerHTML = "Email is already reserved";

                // Remove old errors
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("surname").classList.remove(VALIDATION_STYLE);
                break;
            case "weak_password":
                document.getElementById("password").classList.add(VALIDATION_STYLE);
                document.getElementById("password-help").innerHTML = "Password is weak";

                // Remove old errors
                document.getElementById("email").classList.remove(VALIDATION_STYLE);
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("surname").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_name":
                document.getElementById("name").classList.add(VALIDATION_STYLE);
                document.getElementById("name-help").innerHTML = "Pick a name";

                // Remove old errors
                document.getElementById("email").classList.remove(VALIDATION_STYLE);
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                document.getElementById("surname").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_surname":
                document.getElementById("surname").classList.add(VALIDATION_STYLE);
                document.getElementById("surname-help").innerHTML = "Pick a surname";

                // Remove old errors
                document.getElementById("email").classList.remove(VALIDATION_STYLE);
                document.getElementById("password").classList.remove(VALIDATION_STYLE);
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                break;
            default:
                break;
        }
    }
}
