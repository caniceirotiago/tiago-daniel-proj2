/* JavaScript File - all the code in the world  */
/* Switch to strict mode to get more useful errors
 when you make mistakes. */
'use strict';

import * as language from "./language.js";
import * as theme from "./theme.js";

language.listenerLanguageBtns(); // adds listener to the language buttons
/**************************************************************************************************************************************************************************************/ 
/* DOMcl sets username, changes theme *** */
/**************************************************************************************************************************************************************************************/ 
document.addEventListener('DOMContentLoaded', function() {
    language.underlineLangFlag();
    theme.loadTheme();
    loginActionListners();
});
/**************************************************************************************************************************************************************************************/ 
/* FORM FOR LOGIN LISTENER */ // index.html // <form id="loginForm" action="homepage.html">
/**************************************************************************************************************************************************************************************/
function loginActionListners(){
    // declare variable: var nextStatus is not recommended after IE6, best practice is let keyword
    let form = document.getElementById('loginForm'); // obtains the loginForm
    // adds an EventListener to the form, on click, triggers the function that follows
    form.addEventListener('submit', function(event) { 
        let username = document.getElementById('username').value; // obtains username inserted text
        let password = document.getElementById('password').value;
        let errorElement = document.getElementById('errorLogin'); // obtains the error element for later message insertion
        let errorMsg = 'Mandatory field. Size 6-12, please.';
        if (localStorage.getItem('language')==='pt')
            errorMsg='Campo obrigatório. Tamanho 6-12, por favor';

        if (isUsernameInvalid(username) || isUsernameSmall(username)) {
            event.preventDefault(); // prevents that the form be set/submitted without any fields filled out (just username for now)
            errorElement.innerText = errorMsg; // sets the error message
        } else {

            loginAttempt(username,password);
            errorElement.innerText=""; // clear the error message 
        }
    });    
}
/**************************************************************************************************************************************************************************************/ 
/* FUNCTION isUsernameInvalid(username) - checks if username is empty or null
/**************************************************************************************************************************************************************************************/
function isUsernameInvalid(username) {
    if (username === "" || username === null)
        return true;
    return false;
};
/**************************************************************************************************************************************************************************************/ 
/* FUNCTION isUsernameSmall(username) - checks if username is under 6 letters
/**************************************************************************************************************************************************************************************/
function isUsernameSmall(username) {
    if (username.length<6) {
        console.log("username is smoll");
        return true;
    }
    return false;
};
/**************************************************************************************************************************************************************************************/
/**************************************************************************************************************************************************************************************/


/* Pedido de login ao backend */ 
async function loginAttempt(username, password){
    let user = {
        'username' : username,
        'password': password,
    };
    console.log(user);
    await fetch('http://localhost:8080/Project3-Backend/rest/user/login',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
            credentials: 'include',
            body: JSON.stringify(user)
        }
        ).then(function (response) {
        if (response.status == 200) {
            localStorage.setItem('username', username); // saves data into localStorage       
            window.location.href = "homepage.html"   
        } else if (response.status == 401) {
            alert('Login Failed');
        } else {
            alert('something went wrong :(');
        }
        });
}
























