/**
 * Created by deoxys on 27.07.16.
 */

function getFirstByName(name) {
    return document.getElementsByName(name)[0];
}

function addField() {
    var next = 0;
    while (getFirstByName("field" + next + "_name") != null) next++;
    
    // Container <div> where dynamic content will be added
    var container = document.getElementById("field_container");

    // Create an <input> element, set its type and name attributes
    var name_inp = document.createElement("input");
    name_inp.setAttribute("type", "text");
    name_inp.setAttribute("name", "field" + next + "_name");
    name_inp.setAttribute("placeholder", "Name?");
    container.appendChild(name_inp);
    container.appendChild(document.createTextNode(" "));
    var val_inp = document.createElement("input");
    val_inp.setAttribute("type", "text");
    val_inp.setAttribute("name", "field" + next + "_val");
    val_inp.setAttribute("placeholder", "Value?");
    container.appendChild(val_inp);
    container.appendChild(document.createTextNode(" "));
    
    // Create an <a> element for removing created field
    var rem_link = document.createElement("a");
    rem_link.setAttribute("href", "#");
    rem_link.setAttribute("name", "field" + next + "_rem");
    rem_link.setAttribute("onclick", "removeField(" + next + ")");
    rem_link.innerHTML = "x";
    container.appendChild(rem_link);
    container.appendChild(document.createElement("br"));
}

function removeField(n) {

    // Container <div> from where dynamic content will be removed
    var container = document.getElementById("field_container");
    
    var name_inp = getFirstByName("field" + n + "_name");
    var val_inp = getFirstByName("field" + n + "_val");
    var rem_link = getFirstByName("field" + n + "_rem");
    
    container.removeChild(name_inp);
    container.removeChild(val_inp);
    container.removeChild(rem_link.nextSibling);    // Remove line break
    container.removeChild(rem_link);
}