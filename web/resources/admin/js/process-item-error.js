/**
 * Created by deoxys on 29.07.16.
 */

function process(error) {
    if (error) {
        const VALIDATION_STYLE = "has-warning";
        switch (error) {
            case "empty_name":
                document.getElementById("name").classList.add(VALIDATION_STYLE);
                document.getElementById("name-help").innerHTML = "Pick a name";

                // Remove old errors
                document.getElementById("tags").classList.remove(VALIDATION_STYLE);
                document.getElementById("price").classList.remove(VALIDATION_STYLE);
                document.getElementById("currency").classList.remove(VALIDATION_STYLE);
                document.getElementById("stocked").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_tags":
                document.getElementById("tags").classList.add(VALIDATION_STYLE);
                document.getElementById("tags-help").innerHTML = "Choose some tags";

                // Remove old errors
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("price").classList.remove(VALIDATION_STYLE);
                document.getElementById("currency").classList.remove(VALIDATION_STYLE);
                document.getElementById("stocked").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_price":
                document.getElementById("price").classList.add(VALIDATION_STYLE);
                document.getElementById("price-help").innerHTML = "How much it will cost?";

                // Remove old errors
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("tags").classList.remove(VALIDATION_STYLE);
                document.getElementById("currency").classList.remove(VALIDATION_STYLE);
                document.getElementById("stocked").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_currency":
                document.getElementById("currency").classList.add(VALIDATION_STYLE);
                document.getElementById("currency-help").innerHTML = "What is the currency?";

                // Remove old errors
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("tags").classList.remove(VALIDATION_STYLE);
                document.getElementById("price").classList.remove(VALIDATION_STYLE);
                document.getElementById("stocked").classList.remove(VALIDATION_STYLE);
                break;
            case "empty_stocked":
                document.getElementById("stocked").classList.add(VALIDATION_STYLE);
                document.getElementById("stocked-help").innerHTML = "How many left?";

                // Remove old errors
                document.getElementById("name").classList.remove(VALIDATION_STYLE);
                document.getElementById("tags").classList.remove(VALIDATION_STYLE);
                document.getElementById("price").classList.remove(VALIDATION_STYLE);
                document.getElementById("currency").classList.remove(VALIDATION_STYLE);
                break;
            default:
                break;
        }
    }
}