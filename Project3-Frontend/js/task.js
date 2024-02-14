/* JavaScript File - all the code in the world  */
/* Switch to strict mode to get more useful errors
 when you make mistakes. */
"use strict";

import * as language from "./language.js";
import * as username from "./username.js";
import * as theme from "./theme.js";
import * as logout from "./logout.js";
import { loadPhoto } from "./UserPhoto.js";

language.listenerLanguageBtns(); // adds listener to the language buttons
/**************************************************************************************************************************************************************************************/
/* DOMcl sets username, changes theme *** */
/**************************************************************************************************************************************************************************************/
document.addEventListener("DOMContentLoaded", function () {
  username.setUsername(); // set username on loading
  theme.loadTheme(); // loads up the previously set theme
  language.underlineLangFlag();
  logout.clickOnLogout();
  loadPhoto();
  submitActionLisnter();
});

/**************************************************************************************************************************************************************************************/
/*  TASK SUBMISSION */
/**************************************************************************************************************************************************************************************/

function submitActionLisnter() {
  var form = document.getElementById("taskForm"); // obtem o forumulário de criação de uma task!
  form.addEventListener("submit", function (event) {
    //Adiciona actionListner em caso de submissão
    event.preventDefault(); // previne que o formulário seja enviado da forma default
    var title = document.getElementById("title").value; //obtem o titulo da task
    var description = document.getElementById("description").value; //obtem a descrição da task
    var priority = document.getElementById("priority").value; //obtem a prioridade da task
    var startDate = document.getElementById("date-start").value;
    var endDate = document.getElementById("date-end").value;

    // verificar se a data de inicio é anterior à data de fim
    if (startDate && endDate) {
      if (startDate > endDate) {
        alert("Start date must be before or equal end date");
        // interrompe a função para o utilizador corrigir o erro
        return;
      }
    }

    // title até 20 caracteres e description até 180 caracteres
    if (title.length > 20) {
      alert("Title must be less than 20 characters");
      return;
    }
    if (description.length > 180) {
      alert("Description must be less than 180 characters");
      return;
    }
    if (title && description) {
      // se o titulo e a descrição não estiverem vazios
      addTaskBE(title, description, priority, startDate, endDate);
    } else {
      alert("Title and Description are mandatory fields");
    }
  });
}

/**************************************************************************************************************************************************************************************/
/**************************************************************************************************************************************************************************************/

async function addTaskBE(title, description, priority, startDate, endDate) {
  let task = {
    // cria um objeto task
    title: title,
    description: description,
    priority: priority,
    status: 100,
    username: localStorage.getItem("username"),
  };
  if (startDate) {
    task.startDate = startDate;
  }
  if (endDate) {
    task.endDate = endDate;
  }
  console.log(task);
  await fetch("http://localhost:8080/Project3-Backend/rest/task/create", {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      username: localStorage.getItem("username"),
      password: localStorage.getItem("password"),
    },
    body: JSON.stringify(task),
  }).then(function (response) {
    if (response.status == 201) {
      alert("task is added successfully :)");
      window.location.href = "homepage.html";
    } else if (response.status == 401) {
      alert("username not logged in");
    } else if (response.status == 403) {
      alert("Acess Denied");
    } else if (response.status == 400) {
      alert("Bad request. Lacks mandatory fields Title or Description");
    }
  });
}
