/**
 * Created by deoxys on 29.07.16.
 */

function isValidEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

// @param style: Bootstrap validation style 
// could be has-error, has-warning, has-success, ...
function validateForm(style) {
    // Since some browsers may have Js disabled,
    // it's required by server-side to know if form was already validated with Js.
    // Default value is false, meaning validation should be performed on the server.
    $("input[name=validated]").val(true);

    // Remove previous validation error messages
    $("#email").removeClass(style);
    $("#psswd").removeClass(style);
    $("#psswd2").removeClass(style);
    $("#name").removeClass(style);
    $("#sname").removeClass(style);
    $("#email-help").text("");
    $("#psswd-help").text("");
    $("#psswd2-help").text("");
    $("#name-help").text("");
    $("#sname-help").text("");

    var email = $("#email-inp").val();
    if (! email) {
        $("#email").addClass(style);
        $("#email-help").text("Enter email");
        return false;
    }
    if (! isValidEmail(email)) {
        $("#email").addClass(style);
        $("#email-help").text("'" + email + "' is not a valid email address");
        return false;
    }

    var psswd = $("#psswd-inp").val();
    if (! psswd) {
        $("#psswd").addClass(style);
        $("#psswd-help").text("Enter password");
        return false;
    }
    if (psswd.length < 8) {
        $("#psswd").addClass(style);
        $("#psswd-help").text("Password is weak, should be at least 8 characters");
        return false;
    }

    if ($("#psswd2-inp").val() !== psswd) {
        $("#psswd2").addClass(style);
        $("#psswd2-help").text("Passwords do not match");
        return false;
    }

    if (! $("#name-inp").val()) {
        $("#name").addClass(style);
        $("#name-help").text("Enter name");
        return false;
    }

    if (! $("#sname-inp").val()) {
        $("#sname").addClass(style);
        $("#sname-help").text("Enter surname");
        return false;
    }

    // At this point validation has succeeded
    // and 'password2' input field is not required anymore
    // so it will be redundant to send its value to the server
    $("#psswd2").remove();
}