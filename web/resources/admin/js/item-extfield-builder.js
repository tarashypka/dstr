/**
 * Created by deoxys on 27.07.16.
 */

function addField() {
    var next = 0;
    while (document.getElementById("field" + next) != null) next++;
    
    // Container <div> where dynamic content will be added
    var container = document.getElementById("field-container");
    
    var div = document.createElement("div");
    div.setAttribute("id", "field" + next);
    div.setAttribute("class", "row form-group col-sm-12");
    
    var name_div = document.createElement("div");
    name_div.setAttribute("class", "col-sm-offset-2 col-sm-4");
    
    var name_inp = document.createElement("input");
    name_inp.setAttribute("type", "text");
    name_inp.setAttribute("name", "field" + next + "_name");
    name_inp.setAttribute("placeholder", "Name?");
    name_inp.setAttribute("class", "form-control");
    name_div.appendChild(name_inp);
    div.appendChild(name_div);
    
    var val_div = document.createElement("div");
    val_div.setAttribute("class", "col-sm-4");

    var val_inp = document.createElement("input");
    val_inp.setAttribute("type", "text");
    val_inp.setAttribute("name", "field" + next + "_val");
    val_inp.setAttribute("placeholder", "Value?");
    val_inp.setAttribute("class", "form-control");
    val_div.appendChild(val_inp);
    div.appendChild(val_div);
    
    var rem_div = document.createElement("div");
    rem_div.setAttribute("class", "col-sm-1");
    
    var rem_link = document.createElement("a");
    rem_link.setAttribute("href", "#aa");
    rem_link.setAttribute("name", "field" + next + "_rem");
    rem_link.setAttribute("onclick", "removeField(" + next + ")");
    rem_link.setAttribute("class", "btn btn-default");
    rem_link.innerHTML = "x";
    rem_div.appendChild(rem_link);
    div.appendChild(rem_div);
    
    container.appendChild(div);
}

function removeField(n) {

    // Container <div> from where dynamic content will be removed
    var container = document.getElementById("field-container");
    var field_div = document.getElementById("field" + n);
    container.removeChild(field_div);
}