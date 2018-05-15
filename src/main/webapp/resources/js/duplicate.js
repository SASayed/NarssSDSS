/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var i = 0;
var j = 0;
var p = 0;

function duplicate() {
    var original_C = document.getElementById('criteriaDiv' + i);
    var clone_c = original_C.cloneNode(true); // "deep" clone
    clone_c.id = "criteriaDiv" + ++i; // there can only be one element with an ID 
   // clone.onclick = duplicate; // event handlers are not cloned
    original_C.parentNode.appendChild(clone_c);


    //duplicate preference select
    var original_P = document.getElementById('prefDiv' + p);
    var clone_P = original_P.cloneNode(true); // "deep" clone
    clone_P.id = "prefDiv" + ++p; // there can only be one element with an ID
    original_P.parentNode.appendChild(clone_P);

    
    var original_W = document.getElementById('weightDiv' + j);
    var clone_w = original_W.cloneNode(true); // "deep" clone
    clone_w.id = "weightDiv" + ++j; // there can only be one element with an ID
    original_W.parentNode.appendChild(clone_w);
}
function duplicateTool() {
    var original_C = document.getElementById('criteriaDiv' + i);
    var clone_c = original_C.cloneNode(true); // "deep" clone
    clone_c.id = "criteriaDiv" + ++i; // there can only be one element with an ID 
   // clone.onclick = duplicate; // event handlers are not cloned
    original_C.parentNode.appendChild(clone_c);


    //duplicate preference select
    var original_P = document.getElementById('prefDiv' + p);
    var clone_P = original_P.cloneNode(true); // "deep" clone
    clone_P.id = "prefDiv" + ++p; // there can only be one element with an ID
    original_P.parentNode.appendChild(clone_P);

    
    var original_W = document.getElementById('weightDiv' + j);
    var clone_w = original_W.cloneNode(true); // "deep" clone
    clone_w.id = "weightDiv" + ++j; // there can only be one element with an ID
    original_W.parentNode.appendChild(clone_w);
}