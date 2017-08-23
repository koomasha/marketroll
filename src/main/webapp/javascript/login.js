/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global __TOKEN_COOKIE__ */

function fbLogin() {
    var endpoint = 'authentication/login';
    ajaxPost(null, endpoint, false, fbLoginCallback);
}

function fbLoginCallback(response)
{
    window.location.href = response.url;
}

function fbGetAccessToken(code) {
    var endpoint = 'authentication/getaccestoken';
    var data = code;
    ajaxPost(data, endpoint, false, receiveToken);
}

function receiveToken(response) {
    createCookie(__TOKEN_COOKIE__, response.token, __DAYS_COOKIE_VALID__);
    createCookie(__USERID_COOKIE__, response.userID, __DAYS_COOKIE_VALID__);
}


