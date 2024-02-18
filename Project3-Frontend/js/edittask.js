/* JavaScript File - all the code in the world  */
/* Switch to strict mode to get more useful errors
 when you make mistakes. */
'use strict';

import * as language from "./language.js";
import * as username from "./username.js";
import * as theme from "./theme.js";
import * as logout from "./logout.js"
import * as photoUser from "./UserPhoto.js"
import * as validation from "./taskFieldsValidation.js"

let taskId = -1;

language.listenerLanguageBtns(); // adds listener to the language buttons
/**************************************************************************************************************************************************************************************/ 
/* DOMcl sets username, changes theme *** */
/**************************************************************************************************************************************************************************************/ 
document.addEventListener('DOMContentLoaded', function() {
    username.setUsername(); // set username on loading
    theme.loadTheme(); // loads up the previously set theme
    language.underlineLangFlag();
    logout.clickOnLogout();
    photoUser.loadPhoto();
    loadTaskId();
    editButtonActionListner();
    submitActionListnerCreation();
});
/**************************************************************************************************************************************************************************************/ 
/* DISPLAY TASK PART I - Finds task by ID - EDITTASK.HTML - fetches the Task that was passed through URL, finds it in localStorage JSON, and displays it */
/**************************************************************************************************************************************************************************************/

async function loadTaskId() {
    const urlParams = new URLSearchParams(window.location.search);
    taskId = Number(urlParams.get('taskId'));
    if (!taskId) {
        console.error('Task ID is missing');
        return;
    }

    // Recupera as credenciais do localStorage
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    
    try {
        const response = await fetch(`http://localhost:8080/Project3-Backend/rest/task/${taskId}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'username': username,
                'password': password,
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch task details');
        }

        const task = await response.json();
        // Preenche os campos do formulário com os dados da tarefa
        if (task) {
            document.getElementById('title').value = task.title;
            document.getElementById('description').value = task.description;
            document.getElementById('priority').value = task.priority;
            document.getElementById('date-start').value = task.startDate ? task.startDate : '';
            document.getElementById('date-end').value = task.endDate ? task.endDate : '';
        } else {
            console.error('Task not found');
        }
    } catch (error) {
        console.error('Error fetching task details:', error);
        alert('Network error or server is down. Please try again later.');
    }
}

/**************************************************************************************************************************************************************************************/ 
/* DISPLAY TASK PART II - Interactivity - EDITTASK.HTML - adds the EDIT button and it's responsiveness, on 'click' it enables editing */
/**************************************************************************************************************************************************************************************/
function editButtonActionListner(){
    const editButton = document.getElementById('edit-btn');
    const inputs = document.querySelectorAll('.editable-onClick');

    editButton.addEventListener('click', function() {
        inputs.forEach(function(input) {
            input.disabled = false;
        });
        const savebtn = document.getElementById("save-task");
        savebtn.hidden = false;
        this.disabled = true; 
    });
    
}

/**************************************************************************************************************************************************************************************/ 
/* DISPLAY TASK  PART III - Save Changes  - EDITTASK.HTML - saves the task and returns to homepage */
/**************************************************************************************************************************************************************************************/
function submitActionListnerCreation(){
    const taskForm = document.getElementById('taskForm-viewer-edition');

    taskForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Previne o comportamento padrão de submissão do formulário ...
        if(isValid()){
            editTaskBE();
            const savebtn = document.getElementById("save-task");
            savebtn.hidden = true;
        }    
    });
}
/**************************************************************************************************************************************************************************************/ 
/* function saveTask() - saves the task into backend ::: finds previous occurence, replaces it and resaves */
/**************************************************************************************************************************************************************************************/
function isValid(){
    if (!validation.validateTitle()) {
        return false;
    }
    if (!validation.validateDescription()) {
        return false;
    }
    if (!validation.validateStartDateBeforeEndDate()) {
        return false;
    }
    if (!validation.validatePriority()) {
        return false;
    }
    return true;
}
async function editTaskBE() {
    let taskUpdates = { 
        title: document.getElementById('title').value,
        description: document.getElementById('description').value, 
        priority: document.getElementById('priority').value, 
    };
    const startDate = document.getElementById('date-start').value;
    const endDate = document.getElementById('date-end').value;
    // Inclui datas apenas se forem fornecidas e se não forem manda remover no back
    if (startDate && startDate !== '') taskUpdates.startDate = startDate;
    else  taskUpdates.removeStartDate = true;
    if (endDate && endDate !== '') taskUpdates.endDate = endDate;
    else taskUpdates.removeEndDate = true;

    try {
        const response = await fetch(`http://localhost:8080/Project3-Backend/rest/task/edit/${taskId}`, {
            method: 'PATCH', // Usando PATCH para edição parcial
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'username': localStorage.getItem("username"),
                'password': localStorage.getItem("password"),
            },
            body: JSON.stringify(taskUpdates)
        });

        if (response.ok) {
                alert('Task updated successfully :)');
                const inputs = document.querySelectorAll('.editable-onClick');
                inputs.forEach(function(input) {
                input.disabled = true;
            });
            const editButton = document.getElementById('edit-btn');
            editButton.disabled = false;
        } else {
            const errorMsg = await response.json();
            alert(`Failed to update task: ${errorMsg}`);
        }
    } catch (error) {
        console.error('Error updating task:', error);
        alert('Network error or server is down. Please try again later.');
    }
}