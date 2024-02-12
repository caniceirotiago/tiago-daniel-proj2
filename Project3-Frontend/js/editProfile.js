import * as validation from "./userFieldsValidation.js";

let userData = null;

document.addEventListener("DOMContentLoaded", function() {
    const currentUserUsername = localStorage.getItem("username");
    const currentUserpassword = localStorage.getItem("password");
    fetchUserData(currentUserUsername,currentUserpassword);
    editProfileFormListner();
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

/*preciso de fazer um metodo que seja patch para enviar para o backend a informação sobre a edição, 
repara que terá todos os campos menos a password. O serviço se chamará edituserdata, e temos de enviar 
a informação do username que está neste momento guardado no localstorage para que seja confirmado no 
backend se o utilizador tem acesso, deverá ter as mensagens de erro adequadas*/

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


