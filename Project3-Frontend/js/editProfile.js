import * as validation from "./userFieldsValidation.js";

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
    await fetch('http://localhost:8080/Project3-Backend/rest/user/userinfo',
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
    document.getElementById("phone-field").value = userData.phoneNumber;
    document.getElementById("email-field").value = userData.email;
    document.getElementById("firstname-field").value = userData.firstName;
    document.getElementById("lastname-field").value = userData.lastName;
    document.getElementById("photo-field").value = userData.photoURL;
}
function editUserisValid(){
    if (!validation.validatePhone()) {
        return false;
    }
    if (!validation.validateEmail()) {
        return false;
    }
    if (!validation.validateName()) {
        return false;
    }
    if (!validation.validatephotoURL()) {
        return false;
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
    let user = {
        'phoneNumber': document.getElementById("phone-field").value,
        'email': document.getElementById("email-field").value,
        'firstName': document.getElementById("firstname-field").value,
        'lastName': document.getElementById("lastname-field").value,
        'photoURL': document.getElementById("photo-field").value,
    };
    console.log(user);
    await fetch('http://localhost:8080/Project3-Backend/rest/user/edituserdata',
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
        await fetch('http://localhost:8080/Project3-Backend/rest/user/edituserpassword',
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
