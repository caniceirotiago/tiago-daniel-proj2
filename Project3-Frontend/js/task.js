/* JavaScript File - all the code in the world  */
/* Switch to strict mode to get more useful errors
 when you make mistakes. */
'use strict';

import * as language from "./language.js";
import * as username from "./username.js";
import * as theme from "./theme.js";
import * as logout from "./logout.js"
import { loadPhoto } from "./UserPhoto.js";

language.listenerLanguageBtns(); // adds listener to the language buttons
/**************************************************************************************************************************************************************************************/ 
/* DOMcl sets username, changes theme *** */
/**************************************************************************************************************************************************************************************/ 
document.addEventListener('DOMContentLoaded', function() {
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
function submitActionLisnter(){
     var form = document.getElementById('taskForm'); // obtem o forumulário de criação de uma task!
    form.addEventListener('submit', function(event) { //Adiciona actionListner em caso de submissão
        event.preventDefault(); // previne que o formulário seja enviado da forma default
        var title = document.getElementById('title').value; //obtem o titulo da task
        var description = document.getElementById('description').value; //obtem a descrição da task
        var priority = document.getElementById('priority').value;
        var startDate = document.getElementById('date-start').value;
        var endDate = document.getElementById('date-end').value;
        if(title && description) { // se o titulo e a descrição não estiverem vazios    
            addTask(title, description, priority, startDate, endDate); // adiciona uma task com o titulo e a descrição
            addTaskBE(title, description, priority, startDate, endDate);
            
        }    
    });
}
/**************************************************************************************************************************************************************************************/ 
/* TASK CREATION */
/**************************************************************************************************************************************************************************************/
function addTask(title, description, priority, startDate, endDate) { // adiciona uma task com o titulo e a descrição
    let task = { // cria um objeto task
        id: getNextTaskId(),
        title: title,
        description: description,
        priority: priority,
        status: "todo",
        startDate: startDate,
        endDate: endDate
    };
    
    let tasks = JSON.parse(localStorage.getItem('tasks')) || []; // obtem as tasks do localStorage
    tasks.push(task); // adiciona a task ao array de tasks
    localStorage.setItem('tasks', JSON.stringify(tasks)); // guarda as tasks no localStorage
    //window.location.href = 'homepage.html'; // redireciona para a página principal
};
/**************************************************************************************************************************************************************************************/ 
/* function getNextTaskId() */
/**************************************************************************************************************************************************************************************/
function getNextTaskId() {
    const tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    const maxId = tasks.reduce((max, task) => Math.max(max, task.id || 0), 0);
    return maxId + 1;
};

/**************************************************************************************************************************************************************************************/
/**************************************************************************************************************************************************************************************/

async function addTaskBE(title, description, priority, startDate, endDate){
    let task = { // cria um objeto task
        id: -1,
        title: title,
        description: description,
        priority: priority,
        status: 100,
        username : localStorage.getItem("username"),
        startDate: startDate,
        endDate: endDate
    };
    console.log(task);
    await fetch('http://localhost:8080/Project3-Backend/rest/task/create',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : localStorage.getItem("username"),
            'password': localStorage.getItem("password"),
        },
        body: JSON.stringify(task)
        }
        ).then(function (response) {
        if (response.status == 201) {
            alert('task is added successfully :)');
            window.location.href = "index.html";
        } else if (response.status == 401) {
            alert('username not loged in');
        } else if (response.status == 403) {
            alert('Acess Denied');
        }
        });
}