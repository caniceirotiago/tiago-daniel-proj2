import * as validation from "./userFieldsValidation.js";
import * as fetchPhotoNameAndRedirect from "./fetchPhotoAndName.js";

let userData = null;

document.addEventListener("DOMContentLoaded", function() {
    const currentUserUsername = localStorage.getItem("username");
    const currentUserpassword = localStorage.getItem("password");
    fetchUserData(currentUserUsername,currentUserpassword);
    editProfileFormListner();
    editPasswordFormListner();
});

function editProfileFormListner(){
    const editProfileForm = document.getElementById('editProfileForm');
    editProfileForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (editUserisValid()) {
            editUserData();

        }
    });
}
function editPasswordFormListner(){
    const editPasswordForm = document.getElementById('editPasswordForm');
    editPasswordForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (passwordIsValid()) {
            editPassword();
        }
    });
}
async function fetchUserData(username, password) {
    await fetch('http://localhost:8080/tiago-daniel-proj2/rest/user/userinfo',
    {
        method: 'GET',
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : username,
            'password': password,
            'credentials': 'include'
        },
        
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
    document.getElementById("phone-field").placeholder = userData.phoneNumber;
    document.getElementById("email-field").placeholder = userData.email;
    document.getElementById("firstname-field").placeholder = userData.firstName;
    document.getElementById("lastname-field").placeholder = userData.lastName;
    document.getElementById("photo-field").placeholder = userData.photoURL;
}
function editUserisValid(){
    if(document.getElementById("phone-field") !== null && document.getElementById("phone-field").value !== ""){
        if (!validation.validatePhone()) {
        return false;
        }
    }
    if(document.getElementById("email-field") !== null && document.getElementById("email-field").value !== ""){
       if (!validation.validateEmail()) {
        return false;
        } 
    }
    if(document.getElementById("firstname-field") !== null && document.getElementById("firstname-field").value !== ""
     && document.getElementById("lastname-field") !== null && document.getElementById("lastname-field").value !== ""){
       if (!validation.validateName()) {
        return false;
        }
    }
    if(document.getElementById("photo-field") !== null && document.getElementById("photo-field").value !== ""){
        if (!validation.validatephotoURL()) {
        return false; 
        }
    }
   
    
    return true;
}
function passwordIsValid(){
    if (!validation.validatePassword()) {
        return false;
    }
    return true;
}

async function editUserData(){
    let user = {};
    if(document.getElementById("phone-field").value !== "" && document.getElementById("phone-field").value !== null){
        user.phoneNumber = document.getElementById("phone-field").value;
    }
    if(document.getElementById("email-field").value !== "" && document.getElementById("email-field").value !== null){
        user.email = document.getElementById("email-field").value;
    }
    if(document.getElementById("firstname-field").value !== "" && document.getElementById("firstname-field").value !== null){
        user.firstName = document.getElementById("firstname-field").value;
    }
    if(document.getElementById("lastname-field").value !== "" && document.getElementById("lastname-field").value !== null){
        user.lastName = document.getElementById("lastname-field").value;
    }
    if(document.getElementById("photo-field").value !== "" && document.getElementById("photo-field").value !== null){
        user.photoURL = document.getElementById("photo-field").value;
    }
    console.log(user);
    await fetch('http://localhost:8080/tiago-daniel-proj2/rest/user/edituserdata',
    {
        method: 'PATCH',
        headers:
    {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'username' : localStorage.getItem("username"),
        'password': localStorage.getItem("password")
    },
    body: JSON.stringify(user)
    }
    ).then(function (response) {
    if (response.status == 200) {
        fetchPhotoNameAndRedirect.fetchPhotoNameAndRedirect(localStorage.getItem("username"),localStorage.getItem("password"));
        alert("User data updated successfully");
        window.location.href = "editProfile.html";
    } else {
        alert("Error updating user data");
    }
});
}
async function editPassword(){
    let userNewPassword = {
        'password': document.getElementById("old-password-field").value,
        'newPassword': document.getElementById("password-field").value
    };
        await fetch('http://localhost:8080/tiago-daniel-proj2/rest/user/edituserpassword',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : localStorage.getItem("username"),
            'password': localStorage.getItem("password")
        },
        body: JSON.stringify(userNewPassword)
        }
        ).then(function (response) {
        if (response.status == 200) {
            alert("User password updated successfully");
            window.location.href = "index.html";
            localStorage.removeItem("username");
            localStorage.removeItem("password");
            loocalStorage.removeItem("imageUrl");
        } else {
            alert("Error updating user password");
        }
    });
}
