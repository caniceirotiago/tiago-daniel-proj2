import * as registration from "./registration.js";

let userData = null;

document.addEventListener("DOMContentLoaded", function() {
    const registrationForm = document.getElementById('editProfileForm');
    console.log(registrationForm);
    registrationForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (registration.isValid()) {
            registrationForm.submit();
            window.location.href(index.html);
        }
    });
    const currentUserUsername = localStorage.getItem("username");
    fetchUserData(currentUserUsername);
    
});

async function fetchUserData(username) {
    await fetch('http://localhost:8080/Project3-Backend/rest/user/userinfo',
    {
        method: 'GET',
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : username
        },
        credentials: 'include',
      }
    ).then(response => response.json())
    .then(function (response){
        userData = response;
        completeFieldsWithData();
        console.log(userData);
    });

}
function completeFieldsWithData(){
    document.getElementById("username-field").value = userData.username; 
    document.getElementById("phone-field").value = userData.phoneNumber;
    document.getElementById("email-field").value = userData.email;

    document.getElementById("firstname-field").value = userData.firstName;
    document.getElementById("lastname-field").value = userData.lastName;
    document.getElementById("photo-field").value = userData.photoURL;
}

