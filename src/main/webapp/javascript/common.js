/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global _, __RESPONSE_CONTAINER__ */

function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

function isSet(elem) {
    return (!_.isUndefined(elem) && !_.isNull(elem));
}

function sendResponseToTestAPI(response) {
    var elem = $(__RESPONSE_CONTAINER__);
    elem.text(JSON.stringify(response, null, 2));
    console.log(response);
}