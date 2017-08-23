/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global __API_SERVER__ */

function ajaxPost(data, endpoint, isSecured, callback) {
    var url = __API_SERVER__ + endpoint;
    var headers = {};
    if (isSecured) {
        addTokenToHeader(headers);
    }
    if (!isSet(data)) {
        data = {};
    }
    $.ajax({
        url: url,
        headers: headers,
        method: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: data,
        success: function (response) {
            sendResponseToTestAPI(response);
            if (isSet(callback)) {
                callback(response);
            }
        },
        error: function (error) {
            sendResponseToTestAPI(error);
        }
    });
}

function addTokenToHeader(headers) {
    var token = readCookie(__TOKEN_COOKIE__);
    if (!isSet(token)) {
       throw "Please relogin"; 
    }
    headers.Authorization = 'Bearer ' + token;
}
