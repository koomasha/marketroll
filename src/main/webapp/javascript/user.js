/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getMyData() {
    var endpoint = "user/getmyuser";
    ajaxPost(null, endpoint, true, null);
}

function getMyFriends() {
    var endpoint = "user/getmyfriends";
    ajaxPost(null, endpoint, true, null);
}