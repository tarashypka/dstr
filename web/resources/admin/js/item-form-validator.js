// @param style: Bootstrap validation style 
// could be has-error, has-warning, has-success, ...
function validateForm(style) {
    // Since some browsers may have Js disabled,
    // it's required by server-side to know if form was already validated with Js.
    // Default value is false, meaning validation should be performed on the server.
    $("input[name=validated]").val(true);

    // Remove previous validation error messages
    $("#name").removeClass(style);
    $("#tags").removeClass(style);
    $("#price").removeClass(style);
    $("#currency").removeClass(style);
    $("#stocked").removeClass(style);
    $("#name-help").text("");
    $("#tags-help").text("");
    $("#price-help").text("");
    $("#currency-help").text("");
    $("#stocked-help").text("");
    
    for (i = 0; i < $("#extFields").val(); i++) {
        $("#field" + i + "-name").removeClass(style);
        $("#field" + i + "-val").removeClass(style);
        $("#field" + i + "-name-help").text("");
        $("#field" + i + "-val-help").text("");
    }

    if (! $("#name-inp").val()) {
        $("#name").addClass(style);
        $("#name-help").text("Pick the name");
        $("#name").scrollView();
        return false;
    }

    if (! $("#tags-inp").val()) {
        $("#tags").addClass(style);
        $("#tags-help").text("Choose some tags");
        $("#tags").scrollView();
        return false;
    }

    if (! $("#price-inp").val()) {
        $("#price").addClass(style);
        $("#price-help").text("How much it will cost?");
        $("#price").scrollView();
        return false;
    }

    if (! $("#currency-inp").val()) {
        $("#currency").addClass(style);
        $("#currency-help").text("Select currency");
        $("#currency").scrollView();
        return false;
    }

    if (! $("#stocked-inp").val()) {
        $("#stocked").addClass(style);
        $("#stocked-help").text("How many in stock?");
        $("#stocked").scrollView();
        return false;
    }
    
    // Validate ext fields names / values
    for (i = 0; i < $("#extFields").val(); i++) {
        if (! $("#field" + i + "-name-inp").val()) {
            $("#field" + i + "-name").addClass(style);
            $("#field" + i + "-name-help").text("Enter name or delete this field");
            $("#field" + i).scrollView();
            return false;
        }
        if (! $("#field" + i + "-val-inp").val()) {
            $("#field" + i + "-val").addClass(style);
            $("#field" + i + "-val-help").text("Enter value or delete this field");
            $("#field" + i).scrollView();
            return false;
        }
    }
}