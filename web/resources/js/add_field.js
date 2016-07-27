/**
 * Created by deoxys on 27.07.16.
 */

function addField() {
    var next = 1;
    while (document.getElementById("field" + next) != null) next++;
    
    // Container <div> where dynamic content will go
    var container = document.getElementById("container");

    // Create an <input> element, set its type and name attributes
    var name_inp = document.createElement("input");
    name_inp.type = "text";
    name_inp.id = "field" + next;
    name_inp.name = "field_name" + next;
    name_inp.placeholder = "Name?";
    container.appendChild(name_inp);
    var val_inp = document.createElement("input");
    val_inp.type = "text";
    val_inp.name = "field_val" + next;
    val_inp.placeholder = "Value?";
    container.appendChild(val_inp);

    // Append a line break
    container.appendChild(document.createElement("br"));
}