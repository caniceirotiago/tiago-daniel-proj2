import * as validation from "./userFieldsValidation.js";

document.addEventListener("DOMContentLoaded", function() {
    const registrationForm = document.getElementById('registrationForm');

    registrationForm.addEventListener('submit', function(event) {
        // Previne o envio do formulário antes da validação
        event.preventDefault();
        // Se todas as validações passaram, permite o envio do formulário
        if (isValid()) {
            addUser(registrationForm);
        }
    });
});

function isValid(){
    if (!validation.validateUsername()) {
        return false;
    }
    if (!validation.validatePassword()) {
        return false;
    }
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
async function addUser(form){
    let user = {
        'id' : '0',
        'username' : form.username.value,
        'password': form.password.value,
        'phoneNumber': form.phone.value,
        'email': form.email.value,
        'firstName': form.firstname.value,
        'lastName': form.lastname.value,
        'photoURL': form.photo.value,
    };
    console.log(user);
    await fetch('http://localhost:8080/tiago-daniel-proj2/rest/user/add',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
        }
        ).then(function (response) {
        if (response.status == 200) {
            alert('user is added successfully :)');
            window.location.href = "index.html";
        } else if (response.status == 409) {
            alert('username or email already exists :)');
        } else {
            alert('something went wrong :(');
        }
        });
}
